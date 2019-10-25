package com.example.bitcoininfoapp.data.models

import com.squareup.moshi.Json

data class BitcoinStatusResponse(
    @field:Json(name = "market_price_usd") val marketPrice: Double,
    @field:Json(name = "timestamp") val timestamp: Long,
    @field:Json(name = "trade_volume_usd") val tradeVolume: Double,
    @field:Json(name = "hash_rate") val hashRate: Float
)

data class ChartDetailsResponse(
    val status: String,
    val name: String,
    val unit: String,
    val period: String,
    val description: String,
    val values: List<Values>
)

data class Values(
    @field:Json(name = "x")val timestamp: Float,
    @field:Json(name = "y")val value: Float
)