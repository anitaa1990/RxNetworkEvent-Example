package com.an.rxnetworkevent.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.an.rxnetworkevent.rest.RestApi;
import com.an.rxnetworkevent.rest.RestApiService;

import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel {

    private MutableLiveData<String> usernameErrorValidationState = new MutableLiveData<>();
    private MutableLiveData<String> passwordErrorValidationState = new MutableLiveData<>();

    private RestApi restApiService;
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.restApiService = RestApiService.create(application.getApplicationContext());
    }


    public MutableLiveData<String> getUsernameErrorValidationState() {
        return usernameErrorValidationState;
    }

    public MutableLiveData<String> getPasswordErrorValidationState() {
        return passwordErrorValidationState;
    }

    public void validateLoginDetails(String username, String password) {
        if(username == null || username.isEmpty()) {
            getUsernameErrorValidationState().postValue("Invalid username");

        } else if(password == null || password.isEmpty()) {
            getPasswordErrorValidationState().postValue("Invalid Password");

        } else {
            //login!
            loginUser(username, password);
        }
    }

    private void loginUser(String username, String password) {
        displayLoader();

        restApiService.login()
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    hideLoader();

                    if(response.isSuccessful()) {
                        responseSuccess();
                    } else responseFailure();

                }, e -> hideLoader());
    }
}
