package com.example.bitcoininfoapp.data.local

import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import io.reactivex.Flowable

interface LocalDataSource {
    fun insertBitcoinInfo(bitcoinInfo: BitcoinStatusResponse)
    fun getBitcoinStatus(): Flowable<BitcoinStatusResponse>
    fun insertChartsDetails(chartDetails: List<ChartDetailsResponse>)
    fun getCharts(): Flowable<List<ChartDetailsResponse>>
}