package com.example.bitcoininfoapp.data.local

import com.example.bitcoininfoapp.data.local.db.AppDatabase
import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import io.reactivex.Flowable
import io.reactivex.Single

class LocalDataSourceImpl(
    private val appDatabase: AppDatabase
) : LocalDataSource {

    override fun insertBitcoinInfo(bitcoinInfo: BitcoinStatusResponse) =
        appDatabase.bitCoinStatusDao().insertBitcoinInfo(bitcoinInfo)


    override fun insertChartsDetails(chartDetails: List<ChartDetailsResponse>) =
        appDatabase.chartsInfoDao().insertChartDetails(chartDetails)


    override fun getBitcoinStatus(): Flowable<BitcoinStatusResponse> =
        appDatabase.bitCoinStatusDao().getBitcoinStatus()


    override fun getCharts(): Flowable<List<ChartDetailsResponse>> =
        appDatabase.chartsInfoDao().getCharts()

}