package com.an.rxnetworkevent.rest;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface RestApi {

    @GET("/android-asset/login.json")
    Observable<Response<Object>> login();
}
