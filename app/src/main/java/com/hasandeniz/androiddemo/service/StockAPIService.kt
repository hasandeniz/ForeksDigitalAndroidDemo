package com.hasandeniz.androiddemo.service

import com.hasandeniz.androiddemo.model.StockData
import com.hasandeniz.androiddemo.model.StockListAndSelectableHeaderFields
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//service class for api
class StockAPIService {
    private val BASE_URL = "https://sui7963dq6.execute-api.eu-central-1.amazonaws.com/default/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(StockAPI::class.java)

    fun getStockData() : Single<StockData> {
        return api.getStockData()
    }

    fun getStockDetailsAndHeaderFields() : Single<StockListAndSelectableHeaderFields>{
        return api.getStockDetailsAndFields()
    }
}