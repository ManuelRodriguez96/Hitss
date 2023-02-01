package com.hitss.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Loads an image from the specified URL using Picasso library.
 * @param url The URL of the image to be loaded.
 * @throws Exception in case of any errors during loading.
 * The exception message is logged using Android's Log class.
 */
fun ImageView.loadImageFromPicasso(url: String) {
    try {
        Picasso.get().load(url).into(this)
    } catch (e: Exception) {
        Log.d("Piccasso App", e.cause.toString())
    }
}

/**
 * Adds entry animation to the view
 * @property duration animation duration in milliseconds, default 1000ms
 */
fun View.entryAnimation() {
    ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
    ).apply {
        duration = 1000
        start()
    }
}

/**
 * This function sets a bouncing animation on a `View` object.
 * The animation scales the view down and then back up to its original size.
 * The view is disabled during the animation and re-enabled when it ends.
 * Finally, a `callback` function is invoked after the animation is finished.
 *
 * @param callback the function to be called after the animation is finished.
 */
fun View.setBounce(callback: () -> Unit) {
    this.isEnabled = false
    ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat(View.SCALE_X,0.6f),
        PropertyValuesHolder.ofFloat(View.SCALE_Y,0.6f)
    ).apply {
        duration = 100
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                ObjectAnimator.ofPropertyValuesHolder(
                    this@setBounce,
                    PropertyValuesHolder.ofFloat(View.SCALE_X,1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y,1f)
                ).apply {
                    duration = 100
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {

                        }

                        override fun onAnimationEnd(animation: Animator) {
                            this@setBounce.isEnabled = true
                            callback.invoke()
                        }

                        override fun onAnimationCancel(animation: Animator) {

                        }

                        override fun onAnimationRepeat(animation: Animator) {

                        }
                    })
                    start()
                }
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        start()
    }
}