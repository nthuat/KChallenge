package com.ntt.kchallenge.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ntt.kchallenge.api.UserResponse

class UserDataFactory : DataSource.Factory<Int, UserResponse>() {
    val mutableLiveData: MutableLiveData<UserDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, UserResponse> {
        val userDataSource = UserDataSource()
        mutableLiveData.postValue(userDataSource)
        return userDataSource
    }

}