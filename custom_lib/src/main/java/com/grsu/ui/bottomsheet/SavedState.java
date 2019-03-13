package com.grsu.ui.bottomsheet;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

class SavedState extends View.BaseSavedState {

    @State
    final int state;

    private SavedState(Parcel source) {
        super(source);
        // noinspection ResourceType
        state = source.readInt();
    }

    SavedState(Parcelable superState, @State int state) {
        super(superState);
        this.state = state;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(state);
    }

    public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
        @Override
        public SavedState createFromParcel(Parcel source) {
            return new SavedState(source);
        }

        @Override
        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };
}

