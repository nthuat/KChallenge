package com.ntt.kchallenge.api

import com.ntt.kchallenge.data.model.Countries
import com.ntt.kchallenge.data.model.UserResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {

    @GET("/users")
    fun getUsers(): Observable<List<UserResponse>>

    @GET("/countries.json")
    fun getCountries(): Observable<Countries>
}