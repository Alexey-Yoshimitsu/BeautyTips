package com.example.myapplication

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BeautyDiaryActivity : AppCompatActivity() {

    private lateinit var dateTitle: TextView
    private val cal: Calendar = Calendar.getInstance(Locale("ru"))

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
        updateDate()

        findViewById<ImageView>(R.id.prevDay).setOnClickListener {
            cal.add(Calendar.DAY_OF_MONTH, -1)
            updateDate()
        }
        findViewById<ImageView>(R.id.nextDay).setOnClickListener {
            cal.add(Calendar.DAY_OF_MONTH, 1)
            updateDate()
        }

        // TODO: обработчики нажатий на + (addMorning/addDay/addEvening)
    }

    private fun updateDate() {
        val df = SimpleDateFormat("d MMMM", Locale("ru"))
        dateTitle.text = df.format(Date(cal.timeInMillis))
    }
}
