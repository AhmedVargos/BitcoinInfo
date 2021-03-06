package com.example.bitcoininfoapp.data.remote.bitcoin_info

import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import io.reactivex.Single

interface BitcoinService {
    fun getBitcoinInformation(): Single<BitcoinStatusResponse>
    fun getBitcoinCharts(chartName: String): Single<ChartDetailsResponse>

}