package com.example.bitcoininfoapp.data

import com.example.bitcoininfoapp.data.models.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import io.reactivex.Single

interface DataManager {
    fun getBitcoinInfo(): Single<BitcoinStatusResponse>
    fun getChartsInfo(): Single<List<ChartDetailsResponse>>
}