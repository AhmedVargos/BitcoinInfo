package com.example.bitcoininfoapp.data

import com.example.bitcoininfoapp.data.local.LocalDataSource
import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import com.example.bitcoininfoapp.data.remote.RemoteDataSource
import com.example.bitcoininfoapp.utils.Constants
import com.example.bitcoininfoapp.utils.schedulers.SchedulerProvider
import com.example.bitcoininfoapp.utils.with
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val schedulerProvider: SchedulerProvider
) : Repository {

    /**
     * Merge both the local DB and network source to get the data from each of them and updates
     * the stream.
     */
    override fun getBitcoinInfo(): Flowable<BitcoinStatusResponse> =
        Flowable.mergeDelayError(bitcoinInfoNetwork(), localDataSource.getBitcoinStatus())
            .with(schedulerProvider)


    /**
     * request the bitcoin info from [remoteDataSource] in a flowable manner, and returns empty object
     * for errors.
     */
    private fun bitcoinInfoNetwork() =
        remoteDataSource.getBitcoinInformation()
            .doAfterSuccess {
                Thread {
                    localDataSource.insertBitcoinInfo(it)
                }.start()
            }.toFlowable()
            .onErrorReturn { BitcoinStatusResponse() }


    /**
     * Merge both the local DB and network source to get the data from each of them and updates
     * the stream.
     */
    override fun getChartsInfo(): Flowable<List<ChartDetailsResponse>> =
        Flowable.mergeDelayError(chartsNetwork(), localDataSource.getCharts())
            .with(schedulerProvider)


    /**
     * request the 2 different charts from [remoteDataSource] and returns a list of them in a flowable manner,
     * handles the error as an empty list.
     */
    private fun chartsNetwork() = Single.zip(
        remoteDataSource.getBitcoinCharts(Constants.CHART_TYPE_MARKET_PRICE),
        remoteDataSource.getBitcoinCharts(Constants.CHART_TYPE_TOTAL_BITCOIN),
        BiFunction { firstChartInfo: ChartDetailsResponse, secondChartInfo: ChartDetailsResponse ->
            listOf(firstChartInfo, secondChartInfo)
        }
    ).doAfterSuccess {
        Thread {
            localDataSource.insertChartsDetails(it)
        }.start()
    }.toFlowable()
        .onErrorReturn { listOf() }
}
