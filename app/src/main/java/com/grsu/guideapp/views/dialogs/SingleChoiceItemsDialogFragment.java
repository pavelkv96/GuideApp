package com.grsu.guideapp.views.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import com.grsu.guideapp.R;
import com.grsu.guideapp.project_settings.Constants;
import java.util.Arrays;

public class SingleChoiceItemsDialogFragment extends DialogFragment implements OnClickListener {

    private OnChoiceItemListener listener;

    public interface OnChoiceItemListener {

        void choiceItem(String itemValue);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnChoiceItemListener) getActivity();
        } catch (ClassCastException ignored) {
            listener = (OnChoiceItemListener) getParentFragment();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int checkedItem = 0;

        if (getArguments() != null) {
            String anInt = String.valueOf(getArguments().getInt(Constants.KEY_CHECKED_ITEM));
            String[] intArray = getResources().getStringArray(R.array.single_items);
            checkedItem = Arrays.asList(intArray).indexOf(anInt);
        }

        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.dialog_radius_in_meters));
        builder.setSingleChoiceItems(R.array.single_items, checkedItem, null);
        builder.setPositiveButton(getResources().getString(R.string.ok), this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogs, int i) {
        Integer pos = ((AlertDialog) dialogs).getListView().getCheckedItemPosition();
        String value = getResources().getStringArray(R.array.single_items)[pos];
        listener.choiceItem(value);
    }

    public static SingleChoiceItemsDialogFragment newInstance(int checkedItem) {
        SingleChoiceItemsDialogFragment fragment = new SingleChoiceItemsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_CHECKED_ITEM, checkedItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String getTags(){
        return SingleChoiceItemsDialogFragment.class.getSimpleName();
    }
}
