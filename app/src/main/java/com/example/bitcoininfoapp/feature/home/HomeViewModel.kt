package com.example.bitcoininfoapp.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bitcoininfoapp.data.DataManager
import com.example.bitcoininfoapp.data.models.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import com.example.bitcoininfoapp.data.models.ResultResponse
import com.example.bitcoininfoapp.data.models.Status
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(
    private val dataManager: DataManager
) : ViewModel() {
    private val disposableBag by lazy { CompositeDisposable() }

    private val _priceInfo: MutableLiveData<ResultResponse<BitcoinStatusResponse>> =
        MutableLiveData()

    private val _chartsInfo: MutableLiveData<ResultResponse<List<ChartDetailsResponse>>> =
        MutableLiveData()

    fun getPriceInfoLiveData(): LiveData<ResultResponse<BitcoinStatusResponse>> {
        return _priceInfo
    }

    fun getChartsInfoLiveData(): LiveData<ResultResponse<List<ChartDetailsResponse>>> {
        return _chartsInfo
    }

    fun loadPriceInfo() {
        _priceInfo.value =
            ResultResponse(responseStatus = Status.LOADING)

        disposableBag.add(
            dataManager.getBitcoinInfo()
                .subscribe({ priceInfoResponse ->
                    _priceInfo.value =
                        ResultResponse(responseStatus = Status.SUCCESS, data = priceInfoResponse)
                }, { error ->
                    _priceInfo.value =
                        ResultResponse(responseStatus = Status.FAILURE, errorData = error)
                })
        )
    }

    fun loadChartsInfo() {
        _chartsInfo.value =
            ResultResponse(responseStatus = Status.LOADING)

        disposableBag.add(
            dataManager.getChartsInfo()
                .subscribe({ priceInfoResponse ->
                    _chartsInfo.value =
                        ResultResponse(responseStatus = Status.SUCCESS, data = priceInfoResponse)
                }, { error ->
                    _chartsInfo.value =
                        ResultResponse(responseStatus = Status.FAILURE, errorData = error)
                })
        )
    }

    override fun onCleared() {
        disposableBag.clear()
        super.onCleared()
    }

}