package com.hasandeniz.androiddemo.model

import com.google.gson.annotations.SerializedName

data class StockData(
    @SerializedName("l")
    val stockDataDetail: List<StockDataDetails>,
    @SerializedName("z")
    val z: Int = 0
    )

data class StockDataDetails(
    val tke: String?,
    val las: String?,
    val pdd: String?,
    val ddi: String?,
    val low: String?,
    val hig: String?,
    val buy: String?,
    val sel: String?,
    val pdc: String?,
    val cei: String?,
    val flo: String?,
    val gco: String?,
    val clo: String?,
)
