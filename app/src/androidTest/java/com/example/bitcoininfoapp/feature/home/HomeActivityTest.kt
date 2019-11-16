package com.example.bitcoininfoapp.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bitcoininfoapp.R
import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import com.example.bitcoininfoapp.data.models.ResultResponse
import com.example.bitcoininfoapp.data.models.Status
import com.example.bitcoininfoapp.makeBitcoinStatusResponse
import com.example.bitcoininfoapp.makeChartsResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class HomeActivityTest : KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel
    @Before
    fun setUp() {
        homeViewModel = Mockito.mock(HomeViewModel::class.java)

        loadKoinModules(module {
            viewModel(override = true) {
                homeViewModel
            }
        })

        //Default behavior
        Mockito.doAnswer {
            MutableLiveData<ResultResponse<BitcoinStatusResponse>>()
        }.`when`(homeViewModel).getPriceInfoLiveData()


        Mockito.doAnswer {
            MutableLiveData<ResultResponse<List<ChartDetailsResponse>>>()
        }.`when`(homeViewModel).getChartsInfoLiveData()
    }

    @Test
    fun appLunchesSuccessfully() {
        ActivityScenario.launch(HomeActivity::class.java)
    }

    @Test
    fun onLaunchCheckInitialViewsAreVisible() {
        //Arrange
        Mockito.doAnswer {
            val priceTestLiveData = MutableLiveData<ResultResponse<BitcoinStatusResponse>>()
            priceTestLiveData.value =
                ResultResponse(responseStatus = Status.LOADING)

            priceTestLiveData
        }.`when`(homeViewModel).getPriceInfoLiveData()


        Mockito.doAnswer {
            val chartsTestLiveData = MutableLiveData<ResultResponse<List<ChartDetailsResponse>>>()
            chartsTestLiveData.value =
                ResultResponse(responseStatus = Status.LOADING)

            chartsTestLiveData
        }.`when`(homeViewModel).getChartsInfoLiveData()


        //Act
        ActivityScenario.launch(HomeActivity::class.java)

        //Assert
        onView(withId(R.id.chartsLabel))
            .check(matches(isDisplayed()))

        onView(withId(R.id.backgroundView))
            .check(matches(isDisplayed()))

        onView(withId(R.id.infoCard))
            .check(matches(isDisplayed()))

        onView(withId(R.id.priceProgress))
            .check(matches(isDisplayed()))

        onView(withId(R.id.chartsProgress))
            .check(matches(isDisplayed()))
    }

    @Test
    fun onBitcoinInformationLoadedViewsAreDisplayedWithData() {
        //Arrange
        Mockito.doAnswer {
            val priceTestLiveData = MutableLiveData<ResultResponse<BitcoinStatusResponse>>()
            priceTestLiveData.value =
                ResultResponse(
                    responseStatus = Status.SUCCESS,
                    data = makeBitcoinStatusResponse()
                )

            priceTestLiveData
        }.`when`(homeViewModel).getPriceInfoLiveData()


        Mockito.doAnswer {
            val chartsTestLiveData = MutableLiveData<ResultResponse<List<ChartDetailsResponse>>>()
            chartsTestLiveData.value =
                ResultResponse(
                    responseStatus = Status.SUCCESS,
                    data = makeChartsResponse()
                )

            chartsTestLiveData
        }.`when`(homeViewModel).getChartsInfoLiveData()


        //Act
        ActivityScenario.launch(HomeActivity::class.java)

        //Assert
        onView(withId(R.id.priceDetailsGroup))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.chartsGroup))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun onBitcoinInformationFailureShowErrorViews() {
        //Arrange
        Mockito.doAnswer {
            val priceTestLiveData = MutableLiveData<ResultResponse<BitcoinStatusResponse>>()
            priceTestLiveData.value =
                ResultResponse(responseStatus = Status.FAILURE)

            priceTestLiveData
        }.`when`(homeViewModel).getPriceInfoLiveData()


        Mockito.doAnswer {
            val chartsTestLiveData = MutableLiveData<ResultResponse<List<ChartDetailsResponse>>>()
            chartsTestLiveData.value =
                ResultResponse(responseStatus = Status.FAILURE)

            chartsTestLiveData
        }.`when`(homeViewModel).getChartsInfoLiveData()


        //Act
        ActivityScenario.launch(HomeActivity::class.java)

        //Assert
        onView(withId(R.id.priceRetryBtn))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.chartRetryBtn))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun onBitcoinInformationFailureAndRetrySuccessViewsAreDisplayedWithData() {
        //Arrange
        val priceTestLiveData = MutableLiveData<ResultResponse<BitcoinStatusResponse>>()
        var priceDetailsCounter = 0

        Mockito.doAnswer {
            priceTestLiveData.value =
                ResultResponse(responseStatus = Status.FAILURE)

            priceTestLiveData
        }.`when`(homeViewModel).getPriceInfoLiveData()


        Mockito.doAnswer {
            priceDetailsCounter++
            if (priceDetailsCounter % 2 == 0) {
                priceTestLiveData.value = ResultResponse(
                    responseStatus = Status.SUCCESS,
                    data = makeBitcoinStatusResponse()
                )
            }
            null
        }.`when`(homeViewModel).loadPriceInfo()

        //Act & Assert
        ActivityScenario.launch(HomeActivity::class.java)

        onView(withId(R.id.priceRetryBtn))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.priceRetryBtn)).perform(click())

        onView(withId(R.id.priceDetailsGroup))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

    }

    @Test
    fun onChartsFailureAndRetrySuccessViewsAreDisplayedWithData() {
        //Arrange
        val chartsTestLiveData = MutableLiveData<ResultResponse<List<ChartDetailsResponse>>>()
        var chartDetailsCounter = 0

        Mockito.doAnswer {
            chartsTestLiveData.value =
                ResultResponse(responseStatus = Status.FAILURE)

            chartsTestLiveData
        }.`when`(homeViewModel).getChartsInfoLiveData()

        Mockito.doAnswer {
            chartDetailsCounter++
            if (chartDetailsCounter % 2 == 0) {
                chartsTestLiveData.value =
                    ResultResponse(
                        responseStatus = Status.SUCCESS,
                        data = makeChartsResponse()
                    )
            }
            null
        }.`when`(homeViewModel).loadChartsInfo()

        //Act & Assert
        ActivityScenario.launch(HomeActivity::class.java)


        onView(withId(R.id.chartRetryBtn))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.chartRetryBtn)).perform(click())

        onView(withId(R.id.chartsGroup))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}