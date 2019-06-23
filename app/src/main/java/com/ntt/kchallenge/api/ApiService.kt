package com.ntt.kchallenge.api

import com.ntt.kchallenge.data.model.Countries
import com.ntt.kchallenge.data.model.UserResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {

    @GET("/users")
    fun getUsers(): Single<List<UserResponse>>

    @GET("/countries.json")
    fun getCountries(): Single<Countries>
}