package com.an.rxnetworkevent.loader;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;

import com.an.rxnetworkevent.R;

public class LoaderDialog extends Dialog {

    private boolean cancellable = false;

    public LoaderDialog(Context context, int theme) {
        super(context, theme);
        setDialogView();
    }

    public LoaderDialog(Context context) {
        super(context, R.style.full_screen_dialog);
        setDialogView();
    }

    public void setDialogView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.view_loader);
        setCancelable(cancellable);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
    }
}