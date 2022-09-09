package com.hasandeniz.androiddemo.model

import com.google.gson.annotations.SerializedName

data class StockListAndSelectableHeaderFields (
    @SerializedName("mypageDefaults")
    val detail: List<StockList>,
    @SerializedName("mypage")
    val headerField: List<HeaderFields>
)

data class StockList(
    val cod: String,
    val gro: String,
    val tke: String,
    val def: String,
)

data class HeaderFields(
    val name: String,
    val key: String
)