package com.example.bitcoininfoapp.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.bitcoininfoapp.data.Repository
import com.example.bitcoininfoapp.data.models.ResultResponse
import com.example.bitcoininfoapp.data.models.Status
import com.example.bitcoininfoapp.utils.makeBitcoinStatusResponse
import com.example.bitcoininfoapp.utils.makeChartsResponse
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.Mockito

class HomeViewModelTest : AutoCloseKoinTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val homeViewModel by inject<HomeViewModel>()
    private val testModules = module {
        single {
            declareMock<Repository> {
                Mockito.doAnswer {
                    Flowable.just(makeBitcoinStatusResponse())
                }.whenever(this).getBitcoinInfo()

                Mockito.doAnswer {
                    Flowable.just(makeChartsResponse())
                }.whenever(this).getChartsInfo()
            }
        }

        viewModel { HomeViewModel(get()) }
    }

    @Before
    fun setup() {
        startKoin {
            modules(testModules)
        }

    }

    @Test
    fun `loadPriceInfo - with valid response - should emit the success status to the stream`() {
        //Arrange
        val testObserver = homeViewModel.getPriceInfoLiveData().test()
        //Act
        homeViewModel.loadPriceInfo()
        //Assert
        testObserver.assertHistorySize(2)
        testObserver.assertValue(
            ResultResponse(
                Status.SUCCESS,
                data = makeBitcoinStatusResponse()
            )
        )
    }

    @Test
    fun `loadPriceInfo - with failure - should emit the failure status to stream`() {
        //Arrange
        val throwable = Throwable("", null)
        declareMock<Repository> {
            Mockito.doAnswer {
                Flowable.error<Throwable>(throwable)
            }.whenever(this).getBitcoinInfo()
        }

        val testObserver = homeViewModel.getPriceInfoLiveData().test()

        //Act
        homeViewModel.loadPriceInfo()

        //Assert
        testObserver.assertHistorySize(2)
        testObserver.assertValue(
            ResultResponse(
                responseStatus = Status.FAILURE,
                errorData = throwable
            )
        )
    }

    @Test
    fun `loadChartsInfo - with valid response of charts - should emit the success status `() {
        //Arrange
        val testObserver = homeViewModel.getChartsInfoLiveData().test()

        //Act
        homeViewModel.loadChartsInfo()

        //Assert
        testObserver.assertHistorySize(2)
        testObserver.assertValue(
            ResultResponse(
                responseStatus = Status.SUCCESS,
                data = makeChartsResponse()
            )
        )
    }

    @Test
    fun `loadChartInfo - with failure - should emit the failure status`() {
        //Arrange
        val throwable = Throwable("", null)
        declareMock<Repository> {
            Mockito.doAnswer {
                Flowable.error<Throwable>(throwable)
            }.whenever(this).getChartsInfo()
        }

        val testObserver = homeViewModel.getChartsInfoLiveData().test()

        //Act
        homeViewModel.loadChartsInfo()

        //Assert
        testObserver.assertHistorySize(2)
        testObserver.assertValue(
            ResultResponse(
                responseStatus = Status.FAILURE,
                errorData = throwable
            )
        )
    }

}
