package com.example.bitcoininfoapp.utils

import com.example.bitcoininfoapp.utils.schedulers.SchedulerProvider
import io.reactivex.schedulers.Schedulers

class TestSchedulersProvider: SchedulerProvider {
    override fun io() = Schedulers.trampoline()

    override fun ui() = Schedulers.trampoline()
}