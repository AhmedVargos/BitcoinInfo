package com.example.bitcoininfoapp.data

import com.example.bitcoininfoapp.data.local.LocalDataSource
import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import com.example.bitcoininfoapp.data.remote.RemoteDataSource
import com.example.bitcoininfoapp.utils.TestSchedulersProvider
import com.example.bitcoininfoapp.utils.makeBitcoinStatusResponse
import com.example.bitcoininfoapp.utils.makeSingleChartItem
import com.example.bitcoininfoapp.utils.schedulers.SchedulerProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.Mockito

class RepositoryImplTest : AutoCloseKoinTest() {

    private val dataManager by inject<Repository>()

    private val testModules = module {
        factory<SchedulerProvider> { TestSchedulersProvider() }
        factory {
            declareMock<RemoteDataSource> {
                Mockito.doAnswer {
                    Single.just(makeBitcoinStatusResponse())
                }.whenever(this).getBitcoinInformation()


                Mockito.doAnswer {
                    Single.just(makeSingleChartItem())
                }.whenever(this).getBitcoinCharts(any())
            }
        }
        factory {
            declareMock<LocalDataSource> {
                Mockito.doAnswer {
                    Flowable.empty<BitcoinStatusResponse>()
                }.whenever(this).getBitcoinStatus()


                Mockito.doAnswer {
                    Flowable.empty<List<ChartDetailsResponse>>()
                }.whenever(this).getCharts()

            }
        }

        factory<Repository> { RepositoryImpl(get(), get(), get()) }
    }

    @Before
    fun setup() {
        startKoin {
            modules(testModules)
        }
    }

    @Test
    fun `getBitcoinInfo - with valid creation and data - should return single`() {
        val result = dataManager.getBitcoinInfo().blockingFirst()
        assert(result == makeBitcoinStatusResponse())
    }

    @Test
    fun `getChartsInfo - with valid creation and data - should return single`() {
        val result = dataManager.getChartsInfo().blockingFirst()
        assert(result == listOf(makeSingleChartItem(), makeSingleChartItem()))
    }
}