package com.grsu.guideapp.views.dialogs;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.Test;

public class MultiChoiceItemsDialogFragment extends DialogFragment implements OnClickListener,
        OnMultiChoiceClickListener {

    private OnMultiChoiceItemsListener listener;
    private Test helper;
    private Cursor cur;

    public interface OnMultiChoiceItemsListener {
        void onOk();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        helper = new Test(context);
        try {
            listener = (OnMultiChoiceItemsListener) getActivity();
        } catch (ClassCastException e) {
            listener = (OnMultiChoiceItemsListener) getParentFragment();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cur = helper.getAllTypes(getResources().getString(R.string.locale));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.dialog_types_objects));
        builder.setMultiChoiceItems(cur, cur.getColumnName(1), cur.getColumnName(2), this);
        builder.setPositiveButton(getResources().getString(android.R.string.ok), this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        listener.onOk();
        cur.close();
        helper.close();
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        //long[] ids = ((AlertDialog) dialog).getListView().getCheckedItemIds();
        cur.moveToFirst();
        cur.move(which);
        helper.changeRec(cur.getInt(0), isChecked);
        cur.requery();
        /*ListView list = ((AlertDialog) dialog).getListView();

        int checkedItemCount = list.isItemChecked(0) ? list.getCheckedItemCount() - 1
                : list.getCheckedItemCount();

        if (checkedItemCount < cur.getCount() - 1) {
            list.setItemChecked(0, false);
            //checkedItems[0] = false;
        } else {
            list.setItemChecked(0, true);
            //checkedItems[0] = true;
        }*/
    }

    public static MultiChoiceItemsDialogFragment newInstance() {
        return new MultiChoiceItemsDialogFragment();
    }

    public static String getTags() {
        return MultiChoiceItemsDialogFragment.class.getSimpleName();
    }
}