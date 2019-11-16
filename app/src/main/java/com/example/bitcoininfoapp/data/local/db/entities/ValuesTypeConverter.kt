package com.example.bitcoininfoapp.data.local.db.entities

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType


class ValuesTypeConverter {
    @TypeConverter
    fun stringToList(value: String): List<Values> {
        val moshi = Moshi.Builder().build()

        val listMyData = newParameterizedType(List::class.java, Values::class.java)
        val adapter = moshi.adapter<List<Values>>(listMyData)

        return adapter.fromJson(value) ?: mutableListOf()
    }

    @TypeConverter
    fun listToString(list: List<Values>): String {
        val moshi = Moshi.Builder().build()
        val listMyData = newParameterizedType(List::class.java, Values::class.java)
        val adapter = moshi.adapter<List<Values>>(listMyData)

        return adapter.toJson(list)
    }
}