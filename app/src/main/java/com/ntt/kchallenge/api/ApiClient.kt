package com.ntt.kchallenge.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.TlsVersion
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Collections.singletonList

class ApiClient {
    companion object {
        private const val TYPICODE_BASE_URL = "https://jsonplaceholder.typicode.com"
        private const val CARTRACK_BASE_URL = "https://apps.cartrack.com"

        fun createTypicodeClient(): ApiService {
            val okHttpClient = getBasedOkHttpClientBuilder().build()
            return getRetrofit(okHttpClient, TYPICODE_BASE_URL).create(ApiService::class.java)
        }

        fun createCartrackClient(): ApiService {
            val okHttpClient = getBasedOkHttpClientBuilder().connectionSpecs(
                singletonList(
                    ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS).tlsVersions(
                        TlsVersion.TLS_1_0,
                        TlsVersion.TLS_1_2
                    ).build()
                )
            ).build()

            return getRetrofit(okHttpClient, CARTRACK_BASE_URL).create(ApiService::class.java)
        }

        private fun getRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        private fun getBasedOkHttpClientBuilder(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
        }

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }
    }
}