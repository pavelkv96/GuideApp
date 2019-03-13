package com.grsu.ui.bottomsheet;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Callback for monitoring events about bottom sheets.
 */
public interface BottomSheetCallback {

    /**
     * Called when the bottom sheet changes its state.
     *
     * @param bottomSheet The bottom sheet view.
     * @param newState The new state. This will be one of {@link #STATE_DRAGGING}, {@link
     * #STATE_SETTLING}, {@link #STATE_ANCHOR_POINT}, {@link #STATE_EXPANDED}, {@link
     * #STATE_COLLAPSED}, or {@link #STATE_HIDDEN}.
     */
    void onStateChanged(@NonNull View bottomSheet, @State int newState);

    /**
     * Called when the bottom sheet is being dragged.
     *
     * @param bottomSheet The bottom sheet view.
     * @param slideOffset The new offset of this bottom sheet within its range, from 0 to 1 when it
     * is moving upward, and from 0 to -1 when it moving downward.
     */
    void onSlide(@NonNull View bottomSheet, float slideOffset);
}

