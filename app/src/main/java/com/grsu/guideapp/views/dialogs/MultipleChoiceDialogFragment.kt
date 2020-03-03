package com.grsu.guideapp.views.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.grsu.guideapp.R
import com.grsu.guideapp.activities.SharedViewModel
import com.grsu.guideapp.utils.extensions.bundleOf
import timber.log.Timber

class MultipleChoiceDialogFragment : DialogFragment(), OnMultiChoiceClickListener, OnClickListener {

    private lateinit var model: SharedViewModel

    private var code: Int = 0
    private var label: Int = 0
    private var showSelectAll = false
    private lateinit var items: MutableList<String>
    private lateinit var checkedItems: MutableList<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        if (savedInstanceState != null) {
            savedInstanceState.getBundle(key_data)!!.apply {
                code = getInt(key_code)
                label = getInt(key_label)
                items = getStringArrayList(key_items) ?: mutableListOf()
                checkedItems =
                    (getBooleanArray(key_checked_items) ?: booleanArrayOf()).toMutableList()
                showSelectAll = getBoolean(key_show_select_all, false)
            }

        } else {
            val args = MultipleChoiceDialogFragmentArgs.fromBundle(requireArguments())
            code = args.requestCode
            label = args.label
            showSelectAll = args.showSelectAll
            items = args.titles.toMutableList()
            val checked = args.choices.toMutableList()

            checkedItems = items.associateWith { checked.contains(it) }
                .map { it.value }
                .toMutableList()

            if (showSelectAll) {
                items.add(0, getString(R.string.select_all))
                if (args.choiceSelectAll) {
                    checkedItems.fill(true)
                }
                checkedItems.add(0, args.choiceSelectAll)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (dialog as? AlertDialog)?.listView?.also {
            checkedItems.forEachIndexed { i, value -> it.setItemChecked(i, value) }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(label)
            .setMultiChoiceItems(items.toTypedArray(), null, this)
            .setPositiveButton(android.R.string.ok, this)
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int, checked: Boolean) {
        if (showSelectAll) {
            val listView = (dialog as AlertDialog).listView
            if (which == 0) {
                checkedItems.fill(checked)
                for (i in 0 until listView.count) {
                    listView.setItemChecked(i, checked)
                }
            } else {
                checkedItems[which] = checked
                listView.setItemChecked(which, checked)

                if (checkedItems.size - 1 == listView.checkedItemCount) {
                    checkedItems[0] = checked
                    listView.setItemChecked(0, checked)
                }
            }
        } else {
            val listView = (dialog as AlertDialog).listView
            checkedItems[which] = checked
            listView.setItemChecked(which, checked)
        }

        Timber.e("Chose ${items[which]}")
    }

    override fun onSaveInstanceState(outState: Bundle) {

        val bundle = bundleOf(
            key_code to code,
            key_label to label,
            key_items to items,
            key_checked_items to checkedItems.toBooleanArray(),
            key_show_select_all to showSelectAll
        )
        outState.apply { putBundle(key_data, bundle) }

        super.onSaveInstanceState(outState)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val result = if (showSelectAll) {
            items.filterIndexed { i, _ -> checkedItems[i] }.let { it.subList(1, it.size) }
        } else items

        model.choiceDialogResult(DialogResult(code, TypeDialog.MULTIPLE, result.toTypedArray()))
    }

    companion object {
        private const val key_data = "key_multi_choice_dialog_fragment"
        private const val key_code = "key_code"
        private const val key_label = "key_label"
        private const val key_items = "key_items"
        private const val key_checked_items = "key_checked_items"
        private const val key_show_select_all = "key_show_select_all"
    }
}