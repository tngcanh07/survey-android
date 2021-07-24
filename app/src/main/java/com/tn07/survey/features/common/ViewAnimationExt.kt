package com.tn07.survey.features.common

import android.animation.Animator
import android.view.View

/**
 * Created by toannguyen
 * Jul 24, 2021 at 16:00
 */
fun View.fadeInAnimation(
    duration: Int = resources.getInteger(android.R.integer.config_mediumAnimTime),
    onAnimationStart: (animation: Animator?) -> Unit = {},
    onAnimationEnd: (animation: Animator?) -> Unit = {},
    onAnimationCancel: (animation: Animator?) -> Unit = {},
    onAnimationRepeat: (animation: Animator?) -> Unit = {}
) {
    alpha = 0f
    visibility = View.VISIBLE

    animate()
        .alpha(1f)
        .setDuration(duration.toLong())
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                onAnimationStart(animation)
            }

            override fun onAnimationEnd(animation: Animator?) {
                onAnimationEnd(animation)
            }

            override fun onAnimationCancel(animation: Animator?) {
                onAnimationCancel(animation)
            }

            override fun onAnimationRepeat(animation: Animator?) {
                onAnimationRepeat(animation)
            }
        })
        .start()
}