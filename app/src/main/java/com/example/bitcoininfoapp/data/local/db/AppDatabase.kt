package com.example.bitcoininfoapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import com.example.bitcoininfoapp.data.local.db.entities.ValuesTypeConverter


@Database(entities = [BitcoinStatusResponse::class, ChartDetailsResponse::class], version = 1)
@TypeConverters(ValuesTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bitCoinStatusDao(): BitCoinStatusDao
    abstract fun chartsInfoDao(): ChartsInfoDao
}