package com.example.bitcoininfoapp.di

import com.example.bitcoininfoapp.BuildConfig
import com.example.bitcoininfoapp.data.ApplicationSchedulerProvider
import com.example.bitcoininfoapp.data.DataManager
import com.example.bitcoininfoapp.data.DataManagerImpl
import com.example.bitcoininfoapp.data.SchedulerProvider
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
    // Tells Koin how to create an instance of our sources
    single { RemoteRepository() as RemoteRepo }
    single { Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(get())
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build() }

    single {
        val httpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }
        httpClientBuilder.build()
    }

    single {
        get<Retrofit>().create(BitcoinServiceImpl.BitcoinEndPoints::class.java)
    }

    factory {ApplicationSchedulerProvider() as SchedulerProvider}
    // Tells Koin how to create an instance of DataManagerImpl
    factory { DataManagerImpl(get(), get()) as DataManager}
    // Specific viewModel pattern to tell Koin how to build HomeViewModel
    viewModel {
        HomeViewModel(get())
    }
}