package com.example.bitcoininfoapp.data

import com.example.bitcoininfoapp.data.models.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import com.example.bitcoininfoapp.data.remote.RemoteRepo
import com.example.bitcoininfoapp.utils.Constants
import com.example.bitcoininfoapp.utils.with
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class DataManagerImpl(
    private val remoteRepo: RemoteRepo,
    private val schedulerProvider: SchedulerProvider
) : DataManager {

    override fun getBitcoinInfo(): Single<BitcoinStatusResponse> {
        return remoteRepo.getBitcoinStatus()
            .with(schedulerProvider)
    }

    override fun getChartInfo(): Single<List<ChartDetailsResponse>> {

        return Single.zip(
            remoteRepo.getBitcoinCharts(Constants.CHART_TYPE_MARKET_PRICE),
            remoteRepo.getBitcoinCharts(Constants.CHART_TYPE_TOTAL_BITCOIN),
            BiFunction { t1: ChartDetailsResponse, t2: ChartDetailsResponse ->
                listOf(t1, t2)
            }
        ).with(schedulerProvider)
    }

}
