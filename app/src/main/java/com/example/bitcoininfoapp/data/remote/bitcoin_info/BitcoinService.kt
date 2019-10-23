package com.example.bitcoininfoapp.data.remote.bitcoin_info

import com.example.bitcoininfoapp.data.models.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import io.reactivex.Single

interface BitcoinService {
    fun getBitcoinStatus(): Single<BitcoinStatusResponse>
    fun getBitcoinCharts(chartName: String): Single<ChartDetailsResponse>

}