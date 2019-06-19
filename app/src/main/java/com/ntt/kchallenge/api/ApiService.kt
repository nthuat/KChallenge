package com.ntt.kchallenge.api

import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {

    @GET("/users")
    fun getUsers(): Observable<List<UsersApiResponse>>
}