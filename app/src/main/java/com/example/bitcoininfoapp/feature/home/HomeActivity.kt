package com.example.bitcoininfoapp.feature.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitcoininfoapp.R
import com.example.bitcoininfoapp.data.models.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import com.example.bitcoininfoapp.data.models.Status
import com.example.bitcoininfoapp.utils.gone
import com.example.bitcoininfoapp.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initViewModels()
    }

    private fun initViews() {

        priceRetryBtn.setOnClickListener {
            homeViewModel.loadPriceInfo()
        }

        chartRetryBtn.setOnClickListener {
            homeViewModel.loadChartsInfo()
        }
    }

    private fun initViewModels() {
        homeViewModel.priceInfoLiveData.observe(this, Observer { responseUseCase ->
            when (responseUseCase.responseStatus) {
                Status.LOADING -> {
                    priceDetailsGroup.gone()
                    priceRetryBtn.gone()
                    priceProgress.visible()
                }
                Status.SUCCESS -> {
                    priceDetailsGroup.visible()
                    priceRetryBtn.gone()
                    priceProgress.gone()
                    fillPriceInfoViews(responseUseCase.data)
                }
                Status.FAILURE -> {
                    priceDetailsGroup.gone()
                    priceRetryBtn.visible()
                    priceProgress.gone()
                    showDefaultError()
                }
            }
        })

        homeViewModel.chartsInfoLiveData.observe(this, Observer { responseUseCase ->
            when (responseUseCase.responseStatus) {
                Status.LOADING -> {
                    chartRetryBtn.gone()
                    chartRetryBtn.gone()
                    chartsProgress.visible()
                }
                Status.SUCCESS -> {
                    chartRetryBtn.visible()
                    chartRetryBtn.gone()
                    chartsProgress.gone()

                    fillChartsList(responseUseCase.data)
                }
                Status.FAILURE -> {
                    chartRetryBtn.gone()
                    chartRetryBtn.visible()
                    chartsProgress.gone()
                    showDefaultError()
                }
            }
        })

        homeViewModel.loadPriceInfo()
        homeViewModel.loadChartsInfo()
    }

    /**
     * Fills the charts list with [data]
     */
    private fun fillChartsList(data: List<ChartDetailsResponse>?) {
        with(chartsList) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = ChartsRecyclerAdapter(data ?: mutableListOf())
            isNestedScrollingEnabled = false
        }
    }

    /**
     * Fills the price views [data]
     */
    private fun fillPriceInfoViews(data: BitcoinStatusResponse?) {
        data?.let {
            bitcoinPriceValue.text = "$${String.format("%,.2f", it.marketPrice, true)}"
        }
    }

    /**
     * Makes a toast to show the default error
     */
    private fun showDefaultError(){
        Toast.makeText(this, R.string.error_happened_text, Toast.LENGTH_SHORT).show()
    }
}
