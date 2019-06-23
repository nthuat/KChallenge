package com.ntt.kchallenge.api

import android.os.Parcelable
import com.ntt.kchallenge.data.model.Address
import com.ntt.kchallenge.data.model.Company
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: Address,
    val phone: String,
    val website: String,
    val company: Company
) : Parcelable