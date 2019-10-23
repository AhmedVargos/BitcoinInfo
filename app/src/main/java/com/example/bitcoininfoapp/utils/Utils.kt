package com.example.bitcoininfoapp.utils

import android.text.format.DateFormat
import com.example.bitcoininfoapp.data.SchedulerProvider
import io.reactivex.Single
import java.util.*


/**
 * Use SchedulerProvider configuration for Single
 */
fun <T> Single<T>.with(schedulerProvider: SchedulerProvider): Single<T> =
    this.observeOn(schedulerProvider.ui()).subscribeOn(schedulerProvider.io())


fun getDateStringWithFullMonthName(milliSecondDate: Long): String {
    return DateFormat.format("d MMM", Date(milliSecondDate * 1000)).toString()
}

