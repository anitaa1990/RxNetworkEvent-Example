package com.an.rxnetworkevent.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.an.rxnetworkevent.R;
import com.an.rxnetworkevent.databinding.MainActivityBinding;
import com.an.rxnetworkevent.loader.LoaderHelper;
import com.an.rxnetworkevent.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity {

    private MainActivityBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseView();
        initialiseViewModel();
    }

    private void initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.login.setOnClickListener(view -> {
            mainViewModel.validateLoginDetails(binding.email.getText().toString(), binding.password.getText().toString());
        });
    }

    private void initialiseViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getUsernameErrorValidationState().observe(this, s -> {
            binding.email.requestFocus();
            binding.email.setError(s);
        });

        mainViewModel.getPasswordErrorValidationState().observe(this, s -> {
            binding.password.requestFocus();
            binding.password.setError(s);
        });
        mainViewModel.getLoadingState().observe(this, loadingState -> {
            if (loadingState.isLoading()) displayLoader();
            else hideLoader();
        });

        mainViewModel.getResponseState().observe(this, responseState -> {
            if (responseState.isResponseSuccessful()) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

            } else {
                Toast.makeText(getApplicationContext(), "Login not successful", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayLoader() {
        LoaderHelper.getInstance().displayLoader(this);
    }

    private void hideLoader() {
        LoaderHelper.getInstance().dismissDialog();
    }
}
