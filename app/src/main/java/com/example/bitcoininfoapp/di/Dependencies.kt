package com.example.bitcoininfoapp.di

import android.content.Context
import androidx.room.Room
import com.example.bitcoininfoapp.BuildConfig
import com.example.bitcoininfoapp.data.Repository
import com.example.bitcoininfoapp.data.RepositoryImpl
import com.example.bitcoininfoapp.data.local.LocalDataSource
import com.example.bitcoininfoapp.data.local.LocalDataSourceImpl
import com.example.bitcoininfoapp.data.local.db.AppDatabase
import com.example.bitcoininfoapp.data.remote.RemoteDataSource
import com.example.bitcoininfoapp.data.remote.RemoteDataSourceImpl
import com.example.bitcoininfoapp.data.remote.bitcoin_info.BitcoinServiceImpl
import com.example.bitcoininfoapp.feature.home.HomeViewModel
import com.example.bitcoininfoapp.utils.Constants
import com.example.bitcoininfoapp.utils.schedulers.MyApplicationSchedulerProvider
import com.example.bitcoininfoapp.utils.schedulers.SchedulerProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


val networkModules = module {
    // Tells Koin how to create an singleton instance of Retrofit
    single { createRetrofitInstance(get()) }
    // Tells Koin how to create an singleton instance of OkHttpClient
    single { createHttpClient() }
    // Creates a scope for providing the network API end points
    scope(named(Constants.API_CLIENT_SCOPE)) {
        // Tells Koin how to create an singleton instance of BitcoinEndPoints
        scoped {
            get<Retrofit>().create(BitcoinServiceImpl.BitcoinEndPoints::class.java)
        }
    }
}

val localModules = module {
    // Tells Koin how to create an singleton instance of AppDatabase
    single { createDatabaseManager(androidContext()) }
    // Tells Koin how to create an singleton instance of LocalDataSource
    single<LocalDataSource> { LocalDataSourceImpl(get()) }
}

val bitcoinAppModules = module {
    // Tells Koin how to create an instance of our RemoteDataSource
    single<RemoteDataSource> { RemoteDataSourceImpl() }
    // Tells Koin how to create an instance of SchedulerProvider
    single<SchedulerProvider> { MyApplicationSchedulerProvider() }
    // Tells Koin how to create an instance of Repository
    single<Repository> { RepositoryImpl(get(), get(), get()) }
    // Tells koin the available view models to provide
    viewModel {
        HomeViewModel(get())
    }

}

// All the live modules to provide for the app
val liveModules = listOf(bitcoinAppModules, networkModules, localModules)

fun createDatabaseManager(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}

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
