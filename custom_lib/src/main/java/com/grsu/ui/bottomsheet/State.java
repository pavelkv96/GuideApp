package com.grsu.ui.bottomsheet;

import static com.grsu.ui.bottomsheet.BottomSheetBehaviorGoogleMaps.STATE_ANCHOR_POINT;
import static com.grsu.ui.bottomsheet.BottomSheetBehaviorGoogleMaps.STATE_COLLAPSED;
import static com.grsu.ui.bottomsheet.BottomSheetBehaviorGoogleMaps.STATE_DRAGGING;
import static com.grsu.ui.bottomsheet.BottomSheetBehaviorGoogleMaps.STATE_EXPANDED;
import static com.grsu.ui.bottomsheet.BottomSheetBehaviorGoogleMaps.STATE_HIDDEN;
import static com.grsu.ui.bottomsheet.BottomSheetBehaviorGoogleMaps.STATE_SETTLING;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @hide
 */
@IntDef({
        STATE_EXPANDED,
        STATE_COLLAPSED,
        STATE_DRAGGING,
        STATE_ANCHOR_POINT,
        STATE_SETTLING,
        STATE_HIDDEN})
@Retention(RetentionPolicy.SOURCE)
@interface State {

}
