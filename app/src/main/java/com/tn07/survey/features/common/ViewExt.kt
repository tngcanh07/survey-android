package com.tn07.survey.features.common

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets

/**
 * Created by toannguyen
 * Jul 19, 2021 at 14:43
 */

fun ConstraintLayout.LayoutParams.applySystemBarInsets(
    systemBarInsets: Insets,
    initialStartMargin: Int = 0,
    initialTopMargin: Int = 0,
    initialRightMargin: Int = 0,
    initialBottomMargin: Int = 0
) {
    marginStart = systemBarInsets.left + initialStartMargin
    marginEnd = systemBarInsets.right + initialRightMargin
    bottomMargin = systemBarInsets.bottom + initialBottomMargin
    topMargin = systemBarInsets.top + initialTopMargin
}