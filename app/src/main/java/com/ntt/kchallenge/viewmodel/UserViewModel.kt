package com.ntt.kchallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ntt.kchallenge.data.model.UserResponse
import com.ntt.kchallenge.datasource.UserDataFactory
import com.ntt.kchallenge.utils.LoadDataState
import java.util.concurrent.Executors

class UserViewModel : ViewModel() {

    private lateinit var userDataFactory: UserDataFactory
    var loadDataState: LiveData<LoadDataState>? = null
    var userLiveData: LiveData<PagedList<UserResponse>>? = null

    init {
        initData()
    }

    private fun initData() {
        userDataFactory = UserDataFactory()
        loadDataState =
            Transformations.switchMap(userDataFactory.mutableLiveData) { dataSource -> dataSource.loadDataState }

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(5)
            .build()
        userLiveData = LivePagedListBuilder(userDataFactory, config)
            .setFetchExecutor(Executors.newFixedThreadPool(5))
            .build()
    }

    fun refresh() {
        userDataFactory.mutableLiveData.value?.invalidate()
    }
}