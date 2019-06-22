package com.ntt.kchallenge.utils

class LoadDataState(val status: Status, val msg: String) {
    enum class Status { LOADING, SUCCESS, FAILED }

    companion object {
        val LOADED = LoadDataState(Status.SUCCESS, "Success")
        val LOADING = LoadDataState(Status.LOADING, "Loading")
    }
}