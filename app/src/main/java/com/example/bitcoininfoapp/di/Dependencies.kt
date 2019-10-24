package com.example.bitcoininfoapp.di

import com.example.bitcoininfoapp.BuildConfig
import com.example.bitcoininfoapp.utils.schedulers.MyApplicationSchedulerProvider
import com.example.bitcoininfoapp.data.DataManager
import com.example.bitcoininfoapp.data.DataManagerImpl
import com.example.bitcoininfoapp.utils.schedulers.SchedulerProvider
import com.example.bitcoininfoapp.data.remote.RemoteRepo
import com.example.bitcoininfoapp.data.remote.RemoteRepository
import com.example.bitcoininfoapp.data.remote.bitcoin_info.BitcoinServiceImpl
import com.example.bitcoininfoapp.feature.home.HomeViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Suppress("USELESS_CAST")
val appModules = module {
    // Tells Koin how to create an instance of our RemoteRepo
    single { RemoteRepository() as RemoteRepo }
    // Tells Koin how to create an singleton instance of Retrofit
    single { Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(get())
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build() }

    // Tells Koin how to create an singleton instance of OkHttpClient
    single {
        val httpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }
        httpClientBuilder.build()
    }
    // Tells Koin how to create an singleton instance of BitcoinEndPoints
    single {
        get<Retrofit>().create(BitcoinServiceImpl.BitcoinEndPoints::class.java)
    }
    // Tells Koin how to create an instance of SchedulerProvider
    factory { MyApplicationSchedulerProvider() as SchedulerProvider }
    // Tells Koin how to create an instance of DataManager
    factory { DataManagerImpl(get(), get()) as DataManager}
    // Specific viewModel pattern to tell Koin how to build HomeViewModel
    viewModel {
        HomeViewModel(get())
    }
}