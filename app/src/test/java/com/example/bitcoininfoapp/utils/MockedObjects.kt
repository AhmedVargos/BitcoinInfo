package com.example.bitcoininfoapp.utils

import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import com.example.bitcoininfoapp.data.local.db.entities.Values

fun makeBitcoinStatusResponse(): BitcoinStatusResponse {
    return BitcoinStatusResponse(8000.0,
        1571908761000, 415155156.0, 5415418888.0f)
}


fun makeChartsResponse(): List<ChartDetailsResponse> {
    val charts = mutableListOf<ChartDetailsResponse>()

    repeat(3){
        charts.add(makeSingleChartItem())
    }

    return charts
}

fun makeSingleChartItem() = ChartDetailsResponse(
            status = "valid",
            name = "Market prices",
            description = "A description for the chart",
            unit = "USD",
            period = "2 Weeks",
            values = mutableListOf(
                Values(
                    timestamp = 1571908761000f,
                    value = 50.0f
                )
            )
        )

