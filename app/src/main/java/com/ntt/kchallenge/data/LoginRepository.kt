package com.ntt.kchallenge.data

import com.ntt.kchallenge.data.model.User
import com.ntt.kchallenge.datasource.LoginDataSource

class LoginRepository(val dataSource: LoginDataSource) {

    fun login(username: String, password: String): Result<User> {
        // handle login
        return dataSource.login(username, password)
    }

}
