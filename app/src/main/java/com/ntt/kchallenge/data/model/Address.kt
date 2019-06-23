package com.ntt.kchallenge.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: Location
) : Parcelable {
    fun fullAddress(): String {
        return "$suite, $street, $city, $zipcode"
    }
}