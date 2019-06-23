package com.ntt.kchallenge.datasource

import com.ntt.kchallenge.data.Result
import com.ntt.kchallenge.data.database.DatabaseHelper
import com.ntt.kchallenge.data.model.User
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val databaseHelper: DatabaseHelper) {

    fun login(username: String, password: String): Result<User> {
        return try {
            val exist = databaseHelper.validateUser(username, password)
            if (exist) Result.Success(User(username, password))
            else Result.Error(IOException("Username or password is incorrect!"))
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }
}

