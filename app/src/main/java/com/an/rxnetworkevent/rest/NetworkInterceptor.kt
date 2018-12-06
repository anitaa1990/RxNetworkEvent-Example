package com.an.rxnetworkevent.rest

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor(

    /*
     * Step 1: We pass the context instance here,
     * since we need to get the ConnectivityStatus
     * to check if there is internet.
     * */
    private val context: Context) : Interceptor {
    private val networkEvent: NetworkEvent = NetworkEvent

    override fun intercept(chain: Interceptor.Chain): Response? {
        val request = chain.request()

        /*
         * Step 2: We check if there is internet
         * available in the device. If not, pass
         * the networkState as NO_INTERNET.
         * */

        if (!ConnectivityStatus.isConnected(context)) {
            networkEvent.publish(NetworkState.NO_INTERNET)

        } else {
            try {
                /*
                 * Step 3: Get the response code from the
                 * request. In this scenario we are only handling
                 * unauthorised and server unavailable error
                 * scenario
                 * */
                val response = chain.proceed(request)
                when (response.code()) {
                    401 -> networkEvent.publish(NetworkState.UNAUTHORISED)

                    503 -> networkEvent.publish(NetworkState.NO_RESPONSE)
                }
                return response

            } catch (e: IOException) {
                networkEvent.publish(NetworkState.NO_RESPONSE)
            }
        }
        return null
    }
}