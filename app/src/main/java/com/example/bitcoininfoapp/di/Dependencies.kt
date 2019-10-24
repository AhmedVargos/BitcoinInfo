package com.example.bitcoininfoapp.di

import com.example.bitcoininfoapp.BuildConfig
import com.example.bitcoininfoapp.data.DataManager
import com.example.bitcoininfoapp.data.DataManagerImpl
import com.example.bitcoininfoapp.data.remote.RemoteRepo
import com.example.bitcoininfoapp.data.remote.RemoteRepository
import com.example.bitcoininfoapp.data.remote.bitcoin_info.BitcoinServiceImpl
import com.example.bitcoininfoapp.feature.home.HomeActivity
import com.example.bitcoininfoapp.feature.home.HomeViewModel
import com.example.bitcoininfoapp.utils.Constants
import com.example.bitcoininfoapp.utils.schedulers.MyApplicationSchedulerProvider
import com.example.bitcoininfoapp.utils.schedulers.SchedulerProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModules = module {
    // Tells Koin how to create an singleton instance of Retrofit
    single { createRetrofitInstance(get()) }
    // Tells Koin how to create an singleton instance of OkHttpClient
    single { createHttpClient() }

    scope(named(Constants.API_CLIENT_SCOPE)){
        // Tells Koin how to create an singleton instance of BitcoinEndPoints
        scoped {
            get<Retrofit>().create(BitcoinServiceImpl.BitcoinEndPoints::class.java)
        }
    }
}

val bitcoinAppModules = module {
    // Tells Koin how to create an instance of our RemoteRepo
    single<RemoteRepo> { RemoteRepository() }
    // Tells Koin how to create an instance of SchedulerProvider
    single<SchedulerProvider> { MyApplicationSchedulerProvider() }
    // Tells Koin how to create an instance of DataManager
    single<DataManager> { DataManagerImpl(get(), get()) }

    scope(named<HomeActivity>()) {
        // Specific viewModel pattern to tell Koin how to build HomeViewModel
        viewModel {
            HomeViewModel(get())
        }
    }

}

val liveModules = listOf(bitcoinAppModules, networkModules)

private fun createRetrofitInstance(okHttpClient: OkHttpClient): Retrofit? {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

private fun createHttpClient(): OkHttpClient {
    val httpClientBuilder = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(loggingInterceptor)
    }
    return httpClientBuilder.build()
}
