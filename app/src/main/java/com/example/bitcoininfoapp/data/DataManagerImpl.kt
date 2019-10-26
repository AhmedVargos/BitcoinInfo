package com.example.bitcoininfoapp.data

import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import com.example.bitcoininfoapp.data.remote.RemoteRepo
import com.example.bitcoininfoapp.utils.Constants
import com.example.bitcoininfoapp.utils.schedulers.SchedulerProvider
import com.example.bitcoininfoapp.utils.with
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class DataManagerImpl(
    private val remoteRepo: RemoteRepo,
    private val schedulerProvider: SchedulerProvider
) : DataManager {

    /**
     * request the bitcoin info from [remoteRepo]
     */
    override fun getBitcoinInfo() = remoteRepo.getBitcoinInformation()
        .with(schedulerProvider)

    /**
     * request the 2 different charts from [remoteRepo] and returns a list of them
     */
    override fun getChartsInfo() = Single.zip(
        remoteRepo.getBitcoinCharts(Constants.CHART_TYPE_MARKET_PRICE),
        remoteRepo.getBitcoinCharts(Constants.CHART_TYPE_TOTAL_BITCOIN),
        BiFunction { firstChartInfo: ChartDetailsResponse, secondChartInfo: ChartDetailsResponse ->
            listOf(firstChartInfo, secondChartInfo)
        }
    ).with(schedulerProvider)


}
