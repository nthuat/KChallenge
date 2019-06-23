package com.ntt.kchallenge.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ntt.kchallenge.api.ApiClient
import com.ntt.kchallenge.data.model.UserResponse
import com.ntt.kchallenge.utils.LoadDataState

class UserDataSource : PageKeyedDataSource<Int, UserResponse>() {

    private val MAX_PAGE = 10

    private val apiClient = ApiClient.createTypicodeClient()
    val loadDataState = MutableLiveData<LoadDataState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UserResponse>) {
        var count = 1
        apiClient.getUsers()
            .doOnSubscribe { loadDataState.postValue(LoadDataState.LOADING) }
            .subscribe({ result ->
                if (result != null) {
                    loadDataState.postValue(LoadDataState.LOADED)
                    callback.onResult(result, null, count++)
                } else {
                    handleError("Result is null!")
                }
            }, { throwable ->
                handleError(throwable)
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UserResponse>) {
        apiClient.getUsers()
            .doOnSubscribe { loadDataState.postValue(LoadDataState.LOADING) }
            .subscribe({ result ->
                if (result != null) {
                    loadDataState.postValue(LoadDataState.LOADED)
                    val key = if (params.key == MAX_PAGE) null else params.key + 1
                    callback.onResult(result, key)
                } else {
                    handleError("Result is null!")
                }
            }, { throwable ->
                handleError(throwable)
            })

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserResponse>) {
    }

    private fun handleError(throwable: Throwable) {
        val msg = throwable.message ?: "Something went wrong!"
        handleError(msg)
    }

    private fun handleError(msg: String) {
        loadDataState.postValue(LoadDataState(LoadDataState.Status.FAILED, msg))
    }
}