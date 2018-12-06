package com.an.rxnetworkevent.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData

import com.an.rxnetworkevent.state.LoadingState
import com.an.rxnetworkevent.state.ResponseState

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    public val loadingState = LoadingState()
    public val loadingStateMutableLiveData = MutableLiveData<LoadingState>()

    public val responseState = ResponseState()
    public val responseStateMutableLiveData = MutableLiveData<ResponseState>()

    protected fun displayLoader() {
        loadingState.isLoading = true
        loadingStateMutableLiveData.postValue(loadingState)
    }

    protected fun hideLoader() {
        loadingState.isLoading = false
        loadingStateMutableLiveData.postValue(loadingState)
    }

    protected fun responseSuccess() {
        responseState.isResponseSuccessful = true
        responseStateMutableLiveData.postValue(responseState)
    }

    protected fun responseFailure() {
        responseState.isResponseSuccessful = false
        responseStateMutableLiveData.postValue(responseState)
    }

    fun getLoadingState(): MutableLiveData<LoadingState> {
        return loadingStateMutableLiveData
    }

    fun getResponseState(): MutableLiveData<ResponseState> {
        return responseStateMutableLiveData
    }
}
