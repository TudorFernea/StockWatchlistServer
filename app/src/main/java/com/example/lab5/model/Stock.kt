package com.example.lab5.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stock(

    var id: Long?,
    var symbol: String,
    var desc: String,
    var buy_price: Double?,
    var sell_price: Double?): Parcelable{

}