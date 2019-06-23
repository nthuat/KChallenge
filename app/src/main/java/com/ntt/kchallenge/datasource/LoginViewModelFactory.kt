package com.ntt.kchallenge.datasource

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ntt.kchallenge.data.LoginRepository
import com.ntt.kchallenge.data.database.DatabaseHelper
import com.ntt.kchallenge.viewmodel.LoginViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(DatabaseHelper(context))
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
