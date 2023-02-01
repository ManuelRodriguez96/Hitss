package com.hitss.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * This function observes a `LiveData` object and performs an action when its value changes.
 * The action is passed as a lambda parameter and is performed using the `let` function.
 * The observation is lifecycle-aware and is automatically removed when the `LifecycleOwner` is destroyed.
 *
 * @param liveData the `LiveData` object to observe.
 * @param action the action to be performed when the value of `liveData` changes.
 */
fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

/**
 * This function observes a `LiveData` object that holds `SingleEvent` values and performs an action when its value changes.
 * The action is passed as a lambda parameter and is performed using the `let` function.
 * The observation is lifecycle-aware and is automatically removed when the `LifecycleOwner` is destroyed.
 *
 * @param liveData the `LiveData` object to observe.
 * @param action the action to be performed when the value of `liveData` changes.
 */
fun <T> LifecycleOwner.observeEvent(liveData: LiveData<SingleEvent<T>>, action: (t: SingleEvent<T>) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}
