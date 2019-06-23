package com.ntt.kchallenge

import android.app.Application
import com.ntt.kchallenge.data.database.DatabaseHelper
import com.ntt.kchallenge.data.model.User

class ExApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.addUser(User("test", "123"))
    }
}