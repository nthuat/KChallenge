package com.ntt.kchallenge.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ntt.kchallenge.R
import com.ntt.kchallenge.api.ApiClient
import com.ntt.kchallenge.data.LoginRepository
import com.ntt.kchallenge.data.Result
import com.ntt.kchallenge.data.model.Country
import com.ntt.kchallenge.ui.login.LoginFormState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _countryList = MutableLiveData<List<Country>>()
    val countryList: LiveData<List<Country>> = _countryList

    private val defaultCountry = Country("Select country")
    private var firstTimeSelectCountry = true

    @SuppressLint("CheckResult")
    fun login(username: String, password: String) {
        Single.fromCallable { loginRepository.login(username, password) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _loginResult.value = result is Result.Success
            }, { _loginResult.value = false })
    }

    fun getCountries() {
        val countries = ArrayList<Country>()
        countries.add(defaultCountry)
        ApiClient.createCartrackClient().getCountries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                var list = result.countries
                if (list == null) {
                    list = getOfflineCountryListData()
                }
                countries.addAll(list)
                _countryList.value = countries
            }, {
                countries.addAll(getOfflineCountryListData())
                _countryList.value = countries
            })
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

    fun loginCountryChanged(country: Country) {
        if (firstTimeSelectCountry) {
            firstTimeSelectCountry = false
            return
        }
        val loginFormState = LoginFormState()
        if (country == defaultCountry) {
            loginFormState.countryError = R.string.invalid_country
        }
        _loginForm.value = loginFormState
    }

    fun loginDataChanged(username: String, password: String, country: Country) {
        val loginFormState = LoginFormState()
        if (isUserNameValid(username) && isPasswordValid(password) && isCountryValid(country)) {
            loginFormState.isDataValid = true
        } else {
            if (!isUserNameValid(username)) {
                loginFormState.usernameError = R.string.required
            }
            if (!isPasswordValid(password)) {
                loginFormState.passwordError = R.string.required
            }
            if (!isCountryValid(country)) {
                loginFormState.countryError = R.string.invalid_country
            }
        }
        _loginForm.value = loginFormState
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.trim().isNotBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.trim().isNotBlank()
    }

    private fun isCountryValid(country: Country): Boolean {
        return country != defaultCountry
    }

    private fun getOfflineCountryListData(): List<Country> {
        val countryList = ArrayList<Country>()
        countryList.add(Country("Singapore"))
        countryList.add(Country("USA"))
        countryList.add(Country("UK"))
        countryList.add(Country("Canada"))
        countryList.add(Country("Japan"))
        countryList.add(Country("Korean"))
        countryList.add(Country("Vietnam"))
        countryList.add(Country("China"))
        countryList.add(Country("India"))
        countryList.add(Country("Mexico"))
        return countryList
    }
}
