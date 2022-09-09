package com.hasandeniz.androiddemo.service

import com.hasandeniz.androiddemo.model.StockData
import com.hasandeniz.androiddemo.model.StockListAndSelectableHeaderFields
import io.reactivex.Single
import retrofit2.http.GET

//interface for api
interface StockAPI {

    //function to get stock list and selectable field list from api
    @GET("ForeksMobileInterviewSettings")
    fun getStockDetailsAndFields(): Single<StockListAndSelectableHeaderFields>


    //function to get data for stocks from api
    @GET("ForeksMobileInterview?fields=las,pdd,ddi,low,hig,buy,sel,pdc,cei,flo,gco&stcs=XU100.I.BIST~XU050.I.BIST~XU030.I.BIST~USD/TRL~EUR/TRL~EUR/USD~XAU/USD~XGLD~BRENT")
    fun getStockData(): Single<StockData>

}