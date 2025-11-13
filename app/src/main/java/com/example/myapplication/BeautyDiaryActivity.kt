package com.example.myapplication

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.diary.DiaryItem
import com.example.myapplication.diary.DiaryPrefsRepository
import com.example.myapplication.diary.DiaryViewModel
import com.example.myapplication.diary.DiaryViewModelFactory
import com.example.myapplication.diary.Reaction
import com.example.myapplication.diary.Slot
import com.example.myapplication.ui.DiaryItemDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BeautyDiaryActivity : AppCompatActivity() {

    private lateinit var dateTitle: TextView
    private lateinit var morningGroup: ChipGroup
    private lateinit var dayGroup: ChipGroup
    private lateinit var eveningGroup: ChipGroup
    private lateinit var prevBtn: ImageView
    private lateinit var nextBtn: ImageView

    private val cal: Calendar = Calendar.getInstance(Locale("ru"))
    private val dfTitle = SimpleDateFormat("d MMMM", Locale("ru"))
    private val dfKey = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private val viewModel: DiaryViewModel by viewModels {
        DiaryViewModelFactory(DiaryPrefsRepository.get(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beauty_diary)

        // Подсветка слова "бьюти-дневник"
        val title = findViewById<TextView>(R.id.titleText)
        val base = "Это твой бьюти-дневник"
        val span = SpannableString(base)
        val start = base.indexOf("бьюти-дневник")
        if (start >= 0) {
            span.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary)),
                start, start + "бьюти-дневник".length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            title.text = span
        } else title.text = base

        dateTitle = findViewById(R.id.dateTitle)
        morningGroup = findViewById(R.id.morningGroup)
        dayGroup = findViewById(R.id.dayGroup)
        eveningGroup = findViewById(R.id.eveningGroup)
        prevBtn = findViewById(R.id.prevDay)
        nextBtn = findViewById(R.id.nextDay)

        prevBtn.setOnClickListener {
            cal.add(Calendar.DAY_OF_MONTH, -1)
            applyDateToVm()
        }
        nextBtn.setOnClickListener {
            // Не даём уйти в будущую дату
            val todayKey = dfKey.format(Date())
            val candidateKey = dfKey.format(Date(cal.timeInMillis + 24 * 60 * 60 * 1000))
            if (candidateKey <= todayKey) {
                cal.add(Calendar.DAY_OF_MONTH, 1)
                applyDateToVm()
            }
        }

        // Плюсы → открыть попап добавления
        findViewById<TextView>(R.id.addMorning).setOnClickListener { openAddDialog(Slot.MORNING) }
        findViewById<TextView>(R.id.addDay).setOnClickListener { openAddDialog(Slot.DAY) }
        findViewById<TextView>(R.id.addEvening).setOnClickListener { openAddDialog(Slot.EVENING) }

        // Результат из нематериального попапа
        supportFragmentManager.setFragmentResultListener(DiaryItemDialog.RESULT_KEY, this) { _, bundle ->
            val isEdit = bundle.getBoolean("isEdit")
            val titleText = bundle.getString("title")!!
            val note = bundle.getString("note")
            val reaction = Reaction.valueOf(bundle.getString("reaction")!!)

            if (isEdit) {
                val id = bundle.getLong("id")
                viewModel.update(id, titleText, note, reaction)
            } else {
                val slot = Slot.valueOf(bundle.getString("slot")!!)
                viewModel.add(slot, titleText, note, reaction)
            }
        }

        // Подписка на состояние
        collectState()

        // Стартовая дата
        applyDateToVm()
    }

    private fun collectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { ui ->
                    val parsed = dfKey.parse(ui.dateKey)!!
                    dateTitle.text = dfTitle.format(parsed)
                    updateNavButtons()
                    renderGroup(morningGroup, ui.morning)
                    renderGroup(dayGroup, ui.day)
                    renderGroup(eveningGroup, ui.evening)
                }
            }
        }
    }

    private fun renderGroup(group: ChipGroup, items: List<DiaryItem>) {
        group.removeAllViews()
        items.forEach { item ->
            val chip = Chip(this).apply {
                text = item.title
                isCloseIconVisible = true

                // Тап по чипу — редактируем в том же попапе
                setOnClickListener { openEditDialog(item) }

                // Крестик — удалить + Undo
                setOnCloseIconClickListener {
                    val deletedId = item.id
                    viewModel.delete(deletedId)
                    Snackbar.make(group, "Удалено: ${item.title}", Snackbar.LENGTH_LONG)
                        .setAction("Отмена") {
                            viewModel.add(item.slot, item.title, item.note, item.reaction)
                        }
                        .show()
                }

                // Для скринридера/tooltip
                contentDescription = buildString {
                    append(item.title)
                    item.note?.let { append(". Заметка: ").append(it) }
                    append(". Реакция: ").append(item.reaction.name)
                }
            }
            group.addView(chip)
        }
    }

    private fun openAddDialog(slot: Slot) {
        DiaryItemDialog.newAdd(slot).show(supportFragmentManager, "diary_item_add")
    }

    private fun openEditDialog(item: DiaryItem) {
        DiaryItemDialog.newEdit(item.id, item.slot, item.title, item.note, item.reaction)
            .show(supportFragmentManager, "diary_item_edit")
    }

    private fun applyDateToVm() {
        val key = dfKey.format(Date(cal.timeInMillis))
        viewModel.setDateKey(key)
        dateTitle.text = dfTitle.format(Date(cal.timeInMillis))
        updateNavButtons()
    }

    private fun updateNavButtons() {
        val todayKey = dfKey.format(Date())
        val currentKey = viewModel.dateKey.value
        val canGoForward = currentKey < todayKey
        nextBtn.isEnabled = canGoForward
        nextBtn.alpha = if (canGoForward) 1f else 0.4f
    }
}
