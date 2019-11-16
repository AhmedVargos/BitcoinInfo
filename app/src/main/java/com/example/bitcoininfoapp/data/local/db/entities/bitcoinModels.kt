package com.example.bitcoininfoapp.data.local.db.entities

import androidx.room.*
import com.squareup.moshi.Json

@Entity(tableName = "bitcoin_status_table")
data class BitcoinStatusResponse(
    @field:Json(name = "market_price_usd") val marketPrice: Double = 0.0,
    @PrimaryKey
    @field:Json(name = "timestamp") val timestamp: Long = 0,
    @field:Json(name = "trade_volume_usd") val tradeVolume: Double = 0.0,
    @field:Json(name = "hash_rate") val hashRate: Float = 0.0f
)

@Entity(tableName = "chart_details_table")
data class ChartDetailsResponse(
    val status: String = "",
    @PrimaryKey
    val name: String = "",
    val unit: String = "",
    val period: String = "",
    val description: String = "",
    @TypeConverters(ValuesTypeConverter::class)
    val values: List<Values> = mutableListOf()
)

data class Values(
    @field:Json(name = "x")val timestamp: Float = 0.0f,
    @field:Json(name = "y")val value: Float = 0.0f
)