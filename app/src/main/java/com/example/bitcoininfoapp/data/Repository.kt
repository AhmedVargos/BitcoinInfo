package com.example.bitcoininfoapp.data

import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import io.reactivex.Flowable

interface Repository {
    fun getBitcoinInfo(): Flowable<BitcoinStatusResponse>
    fun getChartsInfo(): Flowable<List<ChartDetailsResponse>>
}