package com.grsu.guideapp.views.dialogs;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class CustomMultiChoiceItemsDialogFragment extends DialogFragment implements
        OnMultiChoiceClickListener {

    private CharSequence[] items = {"1000", "500", "250", "100"};
    private boolean[] checkedItems = {false, false, false, false};
    private String string = items[items.length - 1].toString();
    private OnInputListener onInputListener;

    public interface OnInputListener {

        void sendInput(String s);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException ignored) {

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity());

        builder.setTitle("Radius (in meters)").setMultiChoiceItems(items, checkedItems, this)
                .setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onInputListener.sendInput(string);
                    }
                });
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
        switch (i) {
            case 0: {
                string = items[i].toString();
            }
            case 1: {
                string = items[i].toString();
            }
            case 2: {
                string = items[i].toString();
            }
            break;
            case 3:
                string = items[i].toString();
        }
    }

}
