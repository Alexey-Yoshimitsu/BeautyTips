package com.example.myapplication.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.diary.Reaction
import com.example.myapplication.diary.Slot
import androidx.appcompat.widget.AppCompatEditText

class DiaryItemDialog : DialogFragment(R.layout.dialog_diary_item) {

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        isCancelable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val isEdit = requireArguments().getBoolean(ARG_IS_EDIT, false)
        val slot = requireArguments().getString(ARG_SLOT)?.let { Slot.valueOf(it) }
        val itemId = requireArguments().getLong(ARG_ID, -1L)

        val titleEt = view.findViewById<AppCompatEditText>(R.id.inputTitle)
        val noteEt = view.findViewById<AppCompatEditText>(R.id.inputNote)
        val reactionSpinner = view.findViewById<Spinner>(R.id.inputReaction)

        val reactions = listOf(
            "Без реакции" to Reaction.NONE,
            "Хорошо" to Reaction.GOOD,
            "Нейтрально" to Reaction.NEUTRAL,
            "Плохо" to Reaction.BAD
        )
        reactionSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            reactions.map { it.first }
        )

        // Префилл для режима редактирования
        view.findViewById<TextView>(R.id.popupTitle).text =
            if (isEdit) "Редактировать средство" else "Добавить средство"
        view.findViewById<TextView>(R.id.btnSave).text =
            if (isEdit) "Сохранить" else "Добавить"

        titleEt.setText(requireArguments().getString(ARG_TITLE).orEmpty())
        noteEt.setText(requireArguments().getString(ARG_NOTE).orEmpty())
        val preset = requireArguments().getString(ARG_REACTION)?.let { Reaction.valueOf(it) } ?: Reaction.NONE
        reactionSpinner.setSelection(reactions.indexOfFirst { it.second == preset }.coerceAtLeast(0))

        // Закрытие по фону и крестику
        view.findViewById<View>(R.id.rootDim).setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.panel).setOnClickListener { /* гасим клик, чтобы не закрылось */ }
        view.findViewById<View>(R.id.closeBtn).setOnClickListener { dismiss() }

        // Кнопки
        view.findViewById<View>(R.id.btnCancel).setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnSave).setOnClickListener {
            val titleText = titleEt.text?.toString()?.trim().orEmpty()
            if (titleText.isEmpty()) { titleEt.error = "Введите название"; return@setOnClickListener }
            val selected = reactions[reactionSpinner.selectedItemPosition].second

            parentFragmentManager.setFragmentResult(
                RESULT_KEY,
                bundleOf(
                    "action" to "save",
                    "isEdit" to isEdit,
                    "id" to itemId,
                    "slot" to slot?.name,
                    "title" to titleText,
                    "note" to noteEt.text?.toString()?.trim(),
                    "reaction" to selected.name
                )
            )
            dismiss()
        }
    }

    companion object {
        private const val ARG_IS_EDIT = "arg_is_edit"
        private const val ARG_ID = "arg_id"
        private const val ARG_SLOT = "arg_slot"
        private const val ARG_TITLE = "arg_title"
        private const val ARG_NOTE = "arg_note"
        private const val ARG_REACTION = "arg_reaction"

        const val RESULT_KEY = "diary_item_result"

        fun newAdd(slot: Slot) = DiaryItemDialog().apply {
            arguments = bundleOf(
                ARG_IS_EDIT to false,
                ARG_SLOT to slot.name
            )
        }

        fun newEdit(id: Long, slot: Slot, title: String, note: String?, reaction: Reaction) = DiaryItemDialog().apply {
            arguments = bundleOf(
                ARG_IS_EDIT to true,
                ARG_ID to id,
                ARG_SLOT to slot.name,
                ARG_TITLE to title,
                ARG_NOTE to note,
                ARG_REACTION to reaction.name
            )
        }
    }
}
