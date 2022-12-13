@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package angelandroidapps.utils

import android.animation.*
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.Keep
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import kotlin.math.max

// * Created by Angel on 12/6/2019 3:06 PM.
// * Originally created for project "ContinuousLineArt".
// * Copyright (c) 2019 Angel. All rights reserved.
@Keep
object AnimUtils {

    const val CENTER = 0
    const val LEFT = 1
    const val RIGHT = 2
    const val TOP = 1
    const val BOTTOM = 2

    fun spin(it: View?, duration: Long = 300): Animator {
        return ObjectAnimator.ofFloat(it, View.ROTATION, 0.0f, 360.0f).also {
            it.duration = duration
            it.interpolator = LinearInterpolator()
        }
    }

    fun pulse(it: View?, duration: Long = 300, repeatCount: Int = 1): Animator {
        val pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.4f)
        val pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.4f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0.8f)

        return ObjectAnimator.ofPropertyValuesHolder(it, pvhScaleX, pvhScaleY, alpha).also {
            it.duration = duration
            it.repeatCount = repeatCount
            it.repeatMode = ObjectAnimator.REVERSE

        }

    }

    fun tipRotateCcw(it: View?, degrees: Float = 45f, duration: Long = 500): Animator {

        val pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f)
        val pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f)
        val pvhRotate = PropertyValuesHolder.ofFloat(View.ROTATION, 0f, -degrees)

        return ObjectAnimator.ofPropertyValuesHolder(it, pvhRotate, pvhScaleX, pvhScaleY).also {
            it.duration = duration
            it.repeatCount = 1
            it.repeatMode = ValueAnimator.REVERSE
        }
    }

    fun tipRotateCw(view: View?, degrees: Float = 45f, duration: Long = 500): Animator {

        val pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f)
        val pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f)
        val pvhRotate = PropertyValuesHolder.ofFloat(View.ROTATION, 0f, degrees)

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhRotate, pvhScaleX, pvhScaleY).also {
            it.duration = duration
            it.repeatCount = 1
            it.repeatMode = ValueAnimator.REVERSE
        }
    }

    fun slideInToEnd(view: View, duration: Long = 500): Animator {

        return ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f)
            .also {

                it.doOnStart {
                    view.pivotX = 0f
                    view.visibility = View.VISIBLE
                }
                it.interpolator = OvershootInterpolator()
                it.duration = duration
            }
    }

    fun slideOutFromEnd(view: View, duration: Long = 500): Animator {
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 1f, 0f)
            .also {
                it.doOnStart {
                    view.pivotX = 0f
                    view.visibility = View.VISIBLE
                }
                it.doOnEnd {
                    view.visibility = View.GONE
                }
                it.interpolator = OvershootInterpolator()
                it.duration = duration
            }
    }

    fun fadeOut(it: View, duration: Long): Animator {
        return ObjectAnimator.ofFloat(it, View.ALPHA, 1f, 0f)
            .also {
                it.duration = duration
                it.interpolator = AccelerateDecelerateInterpolator()
            }
    }

    fun fadeIn(it: View, duration: Long): Animator {
        return ObjectAnimator.ofFloat(it, View.ALPHA, it.alpha, 1f)
            .also {
                it.duration = duration
                it.interpolator = AccelerateDecelerateInterpolator()
            }
    }


    fun animateShow(view: View, prevAnim: Animator?, duration: Long): Animator {

        prevAnim?.cancel()
        val pvAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, view.alpha, 1f)
        val pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, view.scaleX, 1f)
        val pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, view.scaleY, 1f)
        var isCanceled = false

        return ObjectAnimator.ofPropertyValuesHolder(view, pvAlpha, pvhScaleX, pvhScaleY).also {
            it.duration = duration
            it.interpolator = OvershootInterpolator()

            it.doOnCancel { isCanceled = true }
            it.doOnStart { view.visibility = View.VISIBLE }
            it.doOnEnd {
                if (!isCanceled) {
                    view.visibility = View.VISIBLE
                }
            }
            it.start()
        }
    }


    fun animateHide(drawer: View, prevAnim: Animator?, duration: Long): Animator {

        prevAnim?.cancel()
        val pvAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, drawer.alpha, 0f)
        val pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, drawer.scaleX, 0f)
        val pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, drawer.scaleY, 0f)
        var isCanceled = false
        return ObjectAnimator.ofPropertyValuesHolder(drawer, pvAlpha, pvhScaleX, pvhScaleY).also {
            it.duration = duration

            it.doOnCancel { isCanceled = true }
            it.doOnStart { drawer.visibility = View.VISIBLE }
            it.doOnEnd {
                if (!isCanceled) {
                    drawer.visibility = View.GONE
                }
            }
            it.start()
        }
    }


    fun circularReveal(
        view: View, duration: Long = 300,
        startX: Int = LEFT, startY: Int = TOP,
        onEndCallback: (() -> Unit)?
    ): Animator {

        val x = getXLocation(startX, view)
        val y = getYLocation(startY, view)
        val startRadius = 0f
        val endRadius = max(view.width, view.height) * 1.2f
        return ViewAnimationUtils.createCircularReveal(
            view, x, y,
            startRadius, endRadius
        )
            .also {
                it.duration = duration
                // it.interpolator = AccelerateDecelerateInterpolator()
                it.doOnStart {
                    view.alpha = 1f
                }
                it.doOnEnd {
                    onEndCallback?.invoke()
                }
            }
    }

    private fun getYLocation(startY: Int, it: View): Int {
        return when (startY) {
            TOP -> 0
            CENTER -> it.height / 2
            BOTTOM -> it.height
            else -> 0
        }
    }

    private fun getXLocation(startX: Int, it: View): Int {
        return when (startX) {
            LEFT -> 0
            CENTER -> it.width / 2
            RIGHT -> it.width
            else -> 0
        }
    }

    //SLIDE DOWN SHOW/HIDE
    fun animSlideInFromTop(view: View, duration: Long = 200) {
        if (view.visibility != View.VISIBLE) {
            view.alpha = 0f
            view.translationY = -view.height.toFloat()
            view.visibility = View.VISIBLE
            view.animate()
                .setDuration(duration)
                .alpha(1f)
                .translationY(0f)
                .setListener(null)
        }
    }

    fun animSlideInFromBottom(view: View, duration: Long = 200) {

        if (view.visibility != View.VISIBLE) {
            view.alpha = 0f
            view.translationY = view.height.toFloat()
            view.visibility = View.VISIBLE
            view.animate()
                .setDuration(duration)
                .alpha(1f)
                .translationY(0f)
                .setListener(null)
        }
    }

    fun animSlideOutFromTop(view: View, duration: Long = 200) {
        if (view.visibility == View.VISIBLE) {
            view.animate()
                .setDuration(duration)
                .alpha(0f)
                .translationY(-view.height.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.GONE
                    }
                })
        }
    }

    fun animSlideOutFromBottom(view: View, duration: Long = 200) {
        if (view.visibility == View.VISIBLE) {
            view.animate()
                .setDuration(duration)
                .alpha(0f)
                .translationY(view.height.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.GONE
                    }
                })
        }
    }

    //SLIDE LEFT/RIGHT

    fun animSlideInFromLeft(view: View, duration: Long = 200) {
        if (view.visibility != View.VISIBLE) {
            view.alpha = 0f
            view.translationX = -view.height.toFloat()
            view.visibility = View.VISIBLE
            view.animate()
                .setDuration(duration)
                .alpha(1f)
                .translationX(0f)
                .setListener(null)
        }
    }

    fun animSlideInFromRight(view: View, duration: Long = 200) {
        if (view.visibility != View.VISIBLE) {
            view.alpha = 0f
            view.translationX = view.height.toFloat()
            view.visibility = View.VISIBLE
            view.animate()
                .setDuration(duration)
                .alpha(1f)
                .translationX(0f)
                .setListener(null)
        }
    }

    fun animSlideOutFromLeft(view: View, duration: Long = 200) {
        if (view.visibility == View.VISIBLE) {
            view.animate()
                .setDuration(duration)
                .alpha(0f)
                .translationX(-view.height.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.GONE
                    }
                })
        }
    }

    fun animSlideOutFromRight(view: View, duration: Long = 200) {
        if (view.visibility == View.VISIBLE) {
            view.animate()
                .setDuration(duration)
                .alpha(0f)
                .translationX(view.width.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.GONE
                    }
                })
        }
    }

}