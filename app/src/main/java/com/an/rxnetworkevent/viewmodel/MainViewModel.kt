package com.an.rxnetworkevent.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.an.rxnetworkevent.rest.RestApi
import com.an.rxnetworkevent.rest.RestApiService

import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : BaseViewModel(application) {

    private val usernameErrorValidationState = MutableLiveData<String>()
    private val passwordErrorValidationState = MutableLiveData<String>()

    private val restApiService: RestApi = RestApiService.create(application.applicationContext)

    fun getPasswordErrorValidationState(): MutableLiveData<String> {
        return passwordErrorValidationState
    }

    fun getUsernameErrorValidationState(): MutableLiveData<String> {
        return usernameErrorValidationState
    }

    fun validateLoginDetails(username: String?, password: String?) {
        if (username == null || username.isEmpty()) {
            usernameErrorValidationState.postValue("Invalid username")

        } else if (password == null || password.isEmpty()) {
            passwordErrorValidationState.postValue("Invalid Password")

        } else {
            //login!
            loginUser(username, password)
        }
    }

    private fun loginUser(username: String, password: String) {
        displayLoader()

        restApiService.login()
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    hideLoader()

                    if (response.isSuccessful) {
                        responseSuccess()
                    } else
                        responseFailure()

                }, { e -> hideLoader() })
    }
}
