package com.example.bitcoininfoapp.data.local.db

import androidx.room.*
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ChartsInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chartDetails: List<ChartDetailsResponse>)

    @Query("SELECT * FROM chart_details_table")
    fun getCharts(): Flowable<List<ChartDetailsResponse>>

    @Transaction
    fun insertChartDetails(chartDetails: List<ChartDetailsResponse>){
        insert(chartDetails)
    }
}