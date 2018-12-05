package com.an.rxnetworkevent.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.an.rxnetworkevent.R;
import com.an.rxnetworkevent.rest.NetworkEvent;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        NetworkEvent.getInstance().register(this,
                networkState -> {
                    switch (networkState) {
                        case NO_INTERNET:
                            displayErrorDialog(getString(R.string.generic_no_internet_title),
                                    getString(R.string.generic_no_internet_desc));
                            break;

                        case NO_RESPONSE:
                            displayErrorDialog(getString(R.string.generic_http_error_title),
                                    getString(R.string.generic_http_error_desc));
                            break;

                        case UNAUTHORISED:
                            //redirect to login screen - if session expired
                            Toast.makeText(getApplicationContext(), R.string.error_login_expired, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            break;
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetworkEvent.getInstance().unregister(this);
    }


    public void displayErrorDialog(String title,
                                   String desc) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(desc)
                .setCancelable(false)
                .setPositiveButton("Ok",
                        (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                .show();
    }

}
