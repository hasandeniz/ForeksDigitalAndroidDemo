package com.hasandeniz.androiddemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.hasandeniz.androiddemo.model.*
import com.hasandeniz.androiddemo.service.StockAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class StockViewModel : ViewModel() {

    private val _headerFieldsLiveData = MutableLiveData<List<HeaderFields>>()
    private val _stockListLiveData = MutableLiveData<List<StockList>>()
    private val _stockDataLiveData = MutableLiveData<List<StockDataDetails>>()
    val selectableHeaderFieldsLiveData: LiveData<List<HeaderFields>> = _headerFieldsLiveData.distinctUntilChanged()
    val stockListLiveData: LiveData<List<StockList>> = _stockListLiveData.distinctUntilChanged()
    val stockDataLiveData: LiveData<List<StockDataDetails>> = _stockDataLiveData.distinctUntilChanged()

    private val stockAPIService = StockAPIService()
    private val disposable = CompositeDisposable()

    //function that makes call to get stock list and selectable field list from api
    fun stockListAndFieldsFromAPI(){
        //adding api calls to disposable to able to get rid of them when their job is over
        disposable.add(
            stockAPIService.getStockDetailsAndHeaderFields()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StockListAndSelectableHeaderFields>(){
                    override fun onSuccess(t: StockListAndSelectableHeaderFields) {
                        _headerFieldsLiveData.value = t.headerField
                        _stockListLiveData.value = t.detail
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
        )
    }

    //function that makes call to get stock data from api
    fun getStockDataFromAPI(){
        //adding api calls to disposable to able to get rid of them when their job is over
        disposable.add(
            stockAPIService.getStockData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StockData>() {
                    override fun onSuccess(t: StockData) {
                        _stockDataLiveData.value = t.stockDataDetail
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
        )
    }


    //function to clear disposable in order to use memory better
    fun clearDisposable(){
        disposable.clear()
    }


}