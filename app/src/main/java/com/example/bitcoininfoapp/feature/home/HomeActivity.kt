package com.example.bitcoininfoapp.feature.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.bitcoininfoapp.R
import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import com.example.bitcoininfoapp.data.models.ResultResponse
import com.example.bitcoininfoapp.data.models.Status
import com.example.bitcoininfoapp.utils.gone
import com.example.bitcoininfoapp.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.toolbar
import org.koin.android.ext.android.inject


class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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

    /**
     * Bind with [homeViewModel] and handle the response state
     */
    private fun initViewModels() {
        homeViewModel.getPriceInfoLiveData().observe(this, Observer (this::priceInformationStateHandler))
        homeViewModel.getChartsInfoLiveData().observe(this, Observer (this::chartResponseStateHandler))

        homeViewModel.loadPriceInfo()
        homeViewModel.loadChartsInfo()
    }

    private fun priceInformationStateHandler(responseUseCase: ResultResponse<BitcoinStatusResponse>) {
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
    }

    private fun chartResponseStateHandler(responseUseCase: ResultResponse<List<ChartDetailsResponse>>) {
        when (responseUseCase.responseStatus) {
            Status.LOADING -> {
                chartRetryBtn.gone()
                chartsGroup.gone()
                chartsProgress.visible()
            }
            Status.SUCCESS -> {
                chartRetryBtn.gone()
                chartsProgress.gone()
                chartsGroup.visible()
                fillChartsList(responseUseCase.data)
            }
            Status.FAILURE -> {
                chartRetryBtn.visible()
                chartsProgress.gone()
                chartsGroup.gone()
                showDefaultError()
            }
        }
    }

    /**
     * Fills the charts list with [data]
     */
    private fun fillChartsList(data: List<ChartDetailsResponse>?) {
        with(chartsList) {
            layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChartsRecyclerAdapter(data ?: mutableListOf())
            isNestedScrollingEnabled = false
        }

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(chartsList)
        indicator.attachToRecyclerView(chartsList, pagerSnapHelper)
    }

    /**
     * Fills the price views [data]
     */
    private fun fillPriceInfoViews(data: BitcoinStatusResponse?) {
        data?.let {
            bitcoinPriceValue.text =
                getString(R.string.text_for_price, String.format("%,.2f", it.marketPrice, true))
            bitcoinVolumeValue.text =
                getString(R.string.text_for_price, String.format("%,.2f", it.tradeVolume, true))
            bitcoinHashRateValue.text = getString(
                R.string.text_for_hash_rate,
                String.format("%,d", it.hashRate.toLong(), true)
            )
        }
    }

    /**
     * Makes a toast to show the default error
     */
    private fun showDefaultError() {
        Toast.makeText(this, R.string.error_happened_text, Toast.LENGTH_SHORT).show()
    }
}
