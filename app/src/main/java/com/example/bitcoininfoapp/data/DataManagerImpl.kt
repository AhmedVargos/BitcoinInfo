package com.example.bitcoininfoapp.data

import com.example.bitcoininfoapp.data.models.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import com.example.bitcoininfoapp.data.remote.RemoteRepo
import com.example.bitcoininfoapp.utils.with
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class DataManagerImpl(
    private val remoteRepo: RemoteRepo,
    private val schedulerProvider: SchedulerProvider
) : DataManager{
    private val disposableBag by lazy { CompositeDisposable() }

    override fun getBitcoinInfo(): Single<BitcoinStatusResponse> {
        return remoteRepo.getBitcoinStatus()
            .with(schedulerProvider)
    }

    override fun getChartInfo(chartName: String): Single<ChartDetailsResponse> {
        return remoteRepo.getBitcoinCharts(chartName)
            .with(schedulerProvider)
    }

    fun clearResources() {
        disposableBag.clear()
    }
}
