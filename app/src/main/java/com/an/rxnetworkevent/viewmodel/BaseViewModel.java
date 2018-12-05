package com.an.rxnetworkevent.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.an.rxnetworkevent.state.LoadingState;
import com.an.rxnetworkevent.state.ResponseState;

public class BaseViewModel extends AndroidViewModel {

    private LoadingState loadingState = new LoadingState();
    private MutableLiveData<LoadingState> loadingStateMutableLiveData = new MutableLiveData<>();

    private ResponseState responseState = new ResponseState();
    private MutableLiveData<ResponseState> responseStateMutableLiveData = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    protected void displayLoader() {
        loadingState.setLoading(true);
        loadingStateMutableLiveData.postValue(loadingState);
    }

    protected void hideLoader() {
        loadingState.setLoading(false);
        loadingStateMutableLiveData.postValue(loadingState);
    }

    protected void responseSuccess() {
        responseState.setResponseSuccessful(true);
        responseStateMutableLiveData.postValue(responseState);
    }

    protected void responseFailure() {
        responseState.setResponseSuccessful(false);
        responseStateMutableLiveData.postValue(responseState);
    }

    public MutableLiveData<LoadingState> getLoadingState() {
        return loadingStateMutableLiveData;
    }

    public MutableLiveData<ResponseState> getResponseState() {
        return responseStateMutableLiveData;
    }
}
