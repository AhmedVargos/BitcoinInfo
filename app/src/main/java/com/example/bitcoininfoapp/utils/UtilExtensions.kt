package com.example.bitcoininfoapp.utils

import android.text.format.DateFormat
import android.view.View
import com.example.bitcoininfoapp.utils.schedulers.SchedulerProvider
import io.reactivex.Single
import java.util.*

/**
 * Extension method to provide show keyboard for View.
 */
fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

/**
 * Extension method to provide show keyboard for View.
 */
fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}


/**
 * Use SchedulerProvider configuration for Single
 */
fun <T> Single<T>.with(schedulerProvider: SchedulerProvider): Single<T> =
    this.observeOn(schedulerProvider.ui()).subscribeOn(schedulerProvider.io())


/**
 * Extension method to generate the date of a timestamp if the format of "d MMM"
 */
fun Long.getDateStringWithFullMonthName(): String {
    return DateFormat.format("d MMM", Date(this * 1000)).toString()
}