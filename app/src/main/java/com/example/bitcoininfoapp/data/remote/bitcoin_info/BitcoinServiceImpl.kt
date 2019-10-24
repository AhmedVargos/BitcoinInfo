package com.example.bitcoininfoapp.data.remote.bitcoin_info

import com.example.bitcoininfoapp.data.models.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.http.GET
import retrofit2.http.Path

class BitcoinServiceImpl : BitcoinService, KoinComponent {

    /**
     * Makes an API call to get bitcoin information
     */
    override fun getBitcoinInformation(): Single<BitcoinStatusResponse> {
        return networkService.getStatus()
    }

    /**
     * Makes an API call to get the wanted chart with [chartName]
     */
    override fun getBitcoinCharts(chartName: String): Single<ChartDetailsResponse> {
        return networkService.getChartInfo(chartName)
    }

    private val networkService : BitcoinEndPoints by inject()

    interface BitcoinEndPoints {
        @GET("stats")
        fun getStatus(): Single<BitcoinStatusResponse>

        @GET("charts/{chart_name}?timespan=5weeks&rollingAverage=8hours&format=json")
        fun getChartInfo(@Path("chart_name") name: String): Single<ChartDetailsResponse>
    }
}