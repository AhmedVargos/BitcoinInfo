package com.example.bitcoininfoapp.data.local.db

import androidx.room.*
import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import io.reactivex.Flowable

@Dao
interface BitCoinStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bitcoinInfo: BitcoinStatusResponse)

    @Query("SELECT * FROM bitcoin_status_table")
    fun getBitcoinStatus(): Flowable<BitcoinStatusResponse>

    @Transaction
    fun insertBitcoinInfo(bitcoinInfo: BitcoinStatusResponse){
        insert(bitcoinInfo)
    }
}