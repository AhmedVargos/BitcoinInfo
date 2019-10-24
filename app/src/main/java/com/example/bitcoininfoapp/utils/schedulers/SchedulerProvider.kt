package com.example.bitcoininfoapp.utils.schedulers

import io.reactivex.Scheduler

/**
 * Rx Scheduler Providers.
 */
interface SchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}