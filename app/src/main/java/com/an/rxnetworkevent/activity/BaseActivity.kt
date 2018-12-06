package com.an.rxnetworkevent.activity

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import com.an.rxnetworkevent.R
import com.an.rxnetworkevent.rest.NetworkEvent
import com.an.rxnetworkevent.rest.NetworkState
import io.reactivex.functions.Consumer

open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        NetworkEvent.register(this, Consumer {
            when (it) {
                NetworkState.NO_INTERNET -> displayErrorDialog(getString(R.string.generic_no_internet_title),
                        getString(R.string.generic_no_internet_desc))

                NetworkState.NO_RESPONSE -> displayErrorDialog(getString(R.string.generic_http_error_title),
                        getString(R.string.generic_http_error_desc))

                NetworkState.UNAUTHORISED -> {
                    //redirect to login screen - if session expired
                    Toast.makeText(applicationContext, R.string.error_login_expired, Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        NetworkEvent.unregister(this)
    }


    fun displayErrorDialog(title: String,
                           desc: String) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(desc)
                .setCancelable(false)
                .setPositiveButton("Ok"
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .show()
    }
}