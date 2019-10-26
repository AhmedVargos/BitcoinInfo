package com.example.bitcoininfoapp.data

import com.example.bitcoininfoapp.data.remote.RemoteRepo
import com.example.bitcoininfoapp.utils.TestSchedulersProvider
import com.example.bitcoininfoapp.utils.makeBitcoinStatusResponse
import com.example.bitcoininfoapp.utils.makeSingleChartItem
import com.example.bitcoininfoapp.utils.schedulers.SchedulerProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.Mockito

class DataManagerImplTest : AutoCloseKoinTest() {

    private val dataManager by inject<DataManager>()

    private val testModules = module {
        factory<SchedulerProvider> { TestSchedulersProvider() }
        factory {
            declareMock<RemoteRepo> {
                Mockito.doAnswer {
                    Single.just(makeBitcoinStatusResponse())
                }.whenever(this).getBitcoinInformation()


                Mockito.doAnswer {
                    Single.just(makeSingleChartItem())
                }.whenever(this).getBitcoinCharts(any())
            }
        }

        factory<DataManager> { DataManagerImpl(get(), get()) }
    }

    @Before
    fun setup(){
        startKoin {
            modules(testModules)
        }
    }

    @Test
    fun `getBitcoinInfo - with valid creation and data - should return single`(){
        val result = dataManager.getBitcoinInfo().blockingGet()
        assert(result == makeBitcoinStatusResponse())
    }

    @Test
    fun `getChartsInfo - with valid creation and data - should return single`(){
        val result = dataManager.getChartsInfo().blockingGet()
        assert(result == listOf(makeSingleChartItem(), makeSingleChartItem()))
    }
}