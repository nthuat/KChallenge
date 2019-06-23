package com.ntt.kchallenge.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ntt.kchallenge.api.ApiClient
import com.ntt.kchallenge.data.model.UserResponse
import com.ntt.kchallenge.utils.LoadDataState

class UserDataSource : PageKeyedDataSource<Int, UserResponse>() {

    val loadDataState = MutableLiveData<LoadDataState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UserResponse>) {
        var count = 1
        ApiClient.createTypicodeClient().getUsers()
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
        ApiClient.createTypicodeClient().getUsers()
            .doOnSubscribe { loadDataState.postValue(LoadDataState.LOADING) }
            .subscribe({ result ->
                if (result != null) {
                    loadDataState.postValue(LoadDataState.LOADED)
                    val key = if (params.key == 5) null else params.key + 1
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
        val msg = if (throwable?.message != null) throwable.message else "Something went wrong!"
        handleError(msg!!)
    }

    private fun handleError(msg: String) {
        loadDataState.postValue(LoadDataState(LoadDataState.Status.FAILED, msg))
    }
}