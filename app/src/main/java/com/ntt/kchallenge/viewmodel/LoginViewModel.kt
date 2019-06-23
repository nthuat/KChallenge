package com.ntt.kchallenge.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ntt.kchallenge.R
import com.ntt.kchallenge.data.LoginRepository
import com.ntt.kchallenge.data.Result
import com.ntt.kchallenge.ui.login.LoginFormState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    @SuppressLint("CheckResult")
    fun login(username: String, password: String) {
        Single.fromCallable { loginRepository.login(username, password) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _loginResult.value = result is Result.Success
            }, { _loginResult.value = false })
    }

    fun loginUsernameChanged(username: String) {
        val loginFormState = LoginFormState()
        if (!isUserNameValid(username)) {
            loginFormState.usernameError = R.string.required
        }
        _loginForm.value = loginFormState
    }

    fun loginPasswordChanged(password: String) {
        val loginFormState = LoginFormState()
        if (!isPasswordValid(password)) {
            loginFormState.passwordError = R.string.required
        }
        _loginForm.value = loginFormState
    }

    fun loginDataChanged(username: String, password: String) {
        val loginFormState = LoginFormState()
        if (!isUserNameValid(username)) {
            loginFormState.usernameError = R.string.required
        }
        if (!isPasswordValid(password)) {
            loginFormState.passwordError = R.string.required
        }
        if (isUserNameValid(username) && isPasswordValid(password)) {
            loginFormState.isDataValid = true
        }
        _loginForm.value = loginFormState
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.trim().isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.trim().isNotBlank()
    }
}
