package com.grsu.guideapp.ui.custom.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.grsu.guideapp.ui.activities.SharedViewModel

class SingleChoiceDialogFragment : DialogFragment() {

    private var code: Int = 0
    private var label: Int = 0
    private var items: Array<String> = arrayOf()
    private var checkedItem = 0
    private lateinit var sharedModel: SharedViewModel
    private lateinit var result: DialogResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = SingleChoiceDialogFragmentArgs.fromBundle(requireArguments())
        sharedModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        code = savedInstanceState?.getInt(key_code) ?: args.requestCode
        label = args.label
        items = args.titles

        checkedItem = savedInstanceState?.getInt(key_checked_item, 0)
            ?: items.toSet().indexOfFirst { it == args.choices }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return with(MaterialAlertDialogBuilder(requireContext())) {
            setTitle(label)
            setCancelable(false)
            setSingleChoiceItems(items, checkedItem) { _, which -> checkedItem = which }
            setNegativeButton(android.R.string.cancel, null)
            setPositiveButton(android.R.string.ok) { _, _ ->
                result = DialogResult(code, TypeDialog.SINGLE, arrayOf(items[checkedItem]))
                sharedModel.choiceDialogResult(result)
            }
            create()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(key_code, code)
        outState.putInt(key_checked_item, checkedItem)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val key_code = "single_choice_dialog_fragment_code"
        private const val key_checked_item = "single_choice_dialog_fragment_checked_item"
    }
}