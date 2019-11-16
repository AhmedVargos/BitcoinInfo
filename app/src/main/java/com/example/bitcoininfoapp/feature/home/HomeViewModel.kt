package com.example.bitcoininfoapp.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bitcoininfoapp.data.Repository
import com.example.bitcoininfoapp.data.local.db.entities.BitcoinStatusResponse
import com.example.bitcoininfoapp.data.local.db.entities.ChartDetailsResponse
import com.example.bitcoininfoapp.data.models.ResultResponse
import com.example.bitcoininfoapp.data.models.Status
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(
    private val repository: Repository
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

    /**
     * get the price info from the Repo and updates the stream with result, starts with loading state.
     */
    fun loadPriceInfo() {
        _priceInfo.value =
            ResultResponse(responseStatus = Status.LOADING)

        disposableBag.add(
            repository.getBitcoinInfo()
                .subscribe({ priceInfoResponse ->
                    if (priceInfoResponse.timestamp == 0.toLong())
                        _priceInfo.value = ResultResponse(responseStatus = Status.FAILURE)
                    else
                        _priceInfo.value =
                            ResultResponse(
                                responseStatus = Status.SUCCESS,
                                data = priceInfoResponse
                            )
                }, { error ->
                    if (_priceInfo.value?.responseStatus == Status.LOADING)
                        _priceInfo.value =
                            ResultResponse(responseStatus = Status.FAILURE, errorData = error)
                })
        )
    }


    /**
     * get the charts from the Repo and updates the stream with result, starts with loading state.
     */
    fun loadChartsInfo() {
        _chartsInfo.value =
            ResultResponse(responseStatus = Status.LOADING)

        disposableBag.add(
            repository.getChartsInfo()
                .subscribe({ priceInfoResponse ->
                    if (priceInfoResponse.isNullOrEmpty())
                        _chartsInfo.value = ResultResponse(responseStatus = Status.FAILURE)
                    else
                        _chartsInfo.value =
                            ResultResponse(
                                responseStatus = Status.SUCCESS,
                                data = priceInfoResponse
                            )
                }, { error ->
                    if (_chartsInfo.value?.responseStatus == Status.LOADING)
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