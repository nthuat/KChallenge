package com.ntt.kchallenge.api

import com.ntt.kchallenge.data.model.Address
import com.ntt.kchallenge.data.model.Company

data class UsersApiResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: Address,
    val phone: String,
    val website: String,
    val company: Company
)