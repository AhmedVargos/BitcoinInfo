package com.example.bitcoininfoapp.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Application providers.
 */
class MyApplicationSchedulerProvider :
    SchedulerProvider {
    override fun io() = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

}