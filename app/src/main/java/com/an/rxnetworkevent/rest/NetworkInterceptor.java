package com.an.rxnetworkevent.rest;

import android.content.Context;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkInterceptor implements Interceptor {

    /*
     * Step 1: We pass the context instance here,
     * since we need to get the ConnectivityStatus
     * to check if there is internet.
     * */
    private Context context;
    private NetworkEvent networkEvent;
    public NetworkInterceptor(Context context) {
        this.context = context;
        this.networkEvent = NetworkEvent.getInstance();
    }

    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();

        /*
         * Step 2: We check if there is internet
         * available in the device. If not, pass
         * the networkState as NO_INTERNET.
         * */

        if (!ConnectivityStatus.isConnected(context)) {
            networkEvent.publish(NetworkState.NO_INTERNET);

        } else {
            try {
                /*
                 * Step 3: Get the response code from the
                 * request. In this scenario we are only handling
                 * unauthorised and server unavailable error
                 * scenario
                 * */
                Response response = chain.proceed(request);
                switch (response.code()) {
                    case 401:
                        networkEvent.publish(NetworkState.UNAUTHORISED);
                        break;

                    case 503:
                        networkEvent.publish(NetworkState.NO_RESPONSE);
                        break;
                }
                return response;

            } catch (IOException e) {
                networkEvent.publish(NetworkState.NO_RESPONSE);
            }
        }
        return null;
    }
}