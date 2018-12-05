package com.an.rxnetworkevent.loader;


import android.app.Activity;

public class LoaderHelper {

    private static LoaderHelper instance;
    public static LoaderHelper getInstance() {
        if(instance == null) instance = new LoaderHelper();
        return instance;
    }

    private LoaderDialog loaderDialog;

    public void displayLoader(Activity activity) {
        loaderDialog = new LoaderDialog(activity);
        if(!activity.isFinishing()) loaderDialog.show();
    }

    public void dismissDialog() {
        if(loaderDialog != null) {
            loaderDialog.dismiss();
            instance = null;
        }
    }
}
