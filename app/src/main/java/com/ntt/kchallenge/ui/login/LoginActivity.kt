package com.ntt.kchallenge.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ntt.kchallenge.R
import com.ntt.kchallenge.data.model.Country
import com.ntt.kchallenge.datasource.LoginViewModelFactory
import com.ntt.kchallenge.ui.users.UserListActivity
import com.ntt.kchallenge.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_login as btnLogin
import kotlinx.android.synthetic.main.activity_login.progress_loading as loading
import kotlinx.android.synthetic.main.activity_login.spinner_country as spinnerCountry
import kotlinx.android.synthetic.main.activity_login.til_password as tilPassword
import kotlinx.android.synthetic.main.activity_login.til_username as tilUsername
import kotlinx.android.synthetic.main.activity_login.tv_invalid_msg as tvInvalidMsg
import kotlinx.android.synthetic.main.activity_login.tv_password as tvPassword
import kotlinx.android.synthetic.main.activity_login.tv_username as tvUsername

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(this))
            .get(LoginViewModel::class.java)

        loginViewModel.countryList.observe(this, Observer {
            loading.visibility = View.GONE
            setCountrySpinner(it)
        })

        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer
            tvInvalidMsg.visibility = View.GONE

            if (loginState.isDataValid) {
                tilUsername.isErrorEnabled = false
                tilPassword.isErrorEnabled = false
            } else {
                if (loginState.usernameError != null) {
                    tilUsername.error = getString(loginState.usernameError!!)
                    tilUsername.isErrorEnabled = true
                } else {
                    tilUsername.isErrorEnabled = false
                }
                if (loginState.passwordError != null) {
                    tilPassword.error = getString(loginState.passwordError!!)
                    tilPassword.isErrorEnabled = true
                } else {
                    tilPassword.isErrorEnabled = false
                }
                if (loginState.countryError != null) {
                    tvInvalidMsg.visibility = View.VISIBLE
                    tvInvalidMsg.text = getString(loginState.countryError!!)
                } else {
                    tvInvalidMsg.visibility = View.GONE
                }
            }
        })

        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer
            loading.visibility = View.GONE
            if (loginResult) {
                navigateToHomeScreen()
            } else {
                showLoginFailed()
            }
        })

        loading.visibility = View.VISIBLE
        loginViewModel.getCountries()

        tvUsername.afterTextChanged {
            loginViewModel.loginUsernameChanged(
                tvUsername.text.toString()
            )
        }

        tvPassword.apply {
            afterTextChanged {
                loginViewModel.loginPasswordChanged(
                    tvPassword.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        handleLogin(
                            tvUsername.text.toString(),
                            tvPassword.text.toString(),
                            spinnerCountry.selectedItem as Country
                        )
                }
                false
            }
        }

        btnLogin.setOnClickListener {
            handleLogin(tvUsername.text.toString(), tvPassword.text.toString(), spinnerCountry.selectedItem as Country)
        }
    }

    private fun setCountrySpinner(countryList: List<Country>) {
        val adapter = ArrayAdapter<Country>(this, android.R.layout.simple_spinner_dropdown_item, countryList)
        spinnerCountry.adapter = adapter
        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                val index = p0?.selectedItem as Country
                Log.e("x", index.name)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                loginViewModel.loginCountryChanged(p0?.selectedItem as Country)
            }

        }
    }

    private fun handleLogin(username: String, password: String, country: Country) {
        loginViewModel.loginDataChanged(username, password, country)
        if (loginViewModel.loginFormState.value?.isDataValid == true) {
            loading.visibility = View.VISIBLE
            hideKeyboard(this, container)
            loginViewModel.login(username, password)
        }
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(this, UserListActivity::class.java))
        finish()
    }

    private fun showLoginFailed() {
        tvInvalidMsg.text = getString(R.string.invalid_info)
        tvInvalidMsg.visibility = View.VISIBLE
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun hideKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
}
