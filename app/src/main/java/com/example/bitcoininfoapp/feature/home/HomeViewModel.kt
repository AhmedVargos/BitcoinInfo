package com.example.bitcoininfoapp.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bitcoininfoapp.data.DataManager
import com.example.bitcoininfoapp.data.models.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import com.example.bitcoininfoapp.data.models.Status
import com.example.bitcoininfoapp.data.models.UseCaseResponse
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(
    private val dataManager: DataManager
) : ViewModel() {
    private val disposableBag by lazy { CompositeDisposable() }

    private val _priceInfo: MutableLiveData<UseCaseResponse<BitcoinStatusResponse>> =
        MutableLiveData()
    val priceInfoLiveData: LiveData<UseCaseResponse<BitcoinStatusResponse>>
        get() = _priceInfo

    private val _chartsInfo: MutableLiveData<UseCaseResponse<List<ChartDetailsResponse>>> =
        MutableLiveData()
    val chartsInfoLiveData: LiveData<UseCaseResponse<List<ChartDetailsResponse>>>
        get() = _chartsInfo

    fun loadPriceInfo() {
        _priceInfo.value =
            UseCaseResponse(responseStatus = Status.LOADING)

        disposableBag.add(
            dataManager.getBitcoinInfo()
                .subscribe({ priceInfoResponse ->
                    _priceInfo.value =
                        UseCaseResponse(responseStatus = Status.SUCCESS, data = priceInfoResponse)
                }, { error ->
                    _priceInfo.value =
                        UseCaseResponse(responseStatus = Status.FAILURE, errorData = error)
                })
        )
    }

    fun loadChartsInfo() {
        _chartsInfo.value =
            UseCaseResponse(responseStatus = Status.LOADING)

        disposableBag.add(
            dataManager.getChartInfo()
                .subscribe({ priceInfoResponse ->
                    _chartsInfo.value =
                        UseCaseResponse(responseStatus = Status.SUCCESS, data = priceInfoResponse)
                }, { error ->
                    _chartsInfo.value =
                        UseCaseResponse(responseStatus = Status.FAILURE, errorData = error)
                })
        )
    }

    override fun onCleared() {
        disposableBag.clear()
        super.onCleared()
    }

}