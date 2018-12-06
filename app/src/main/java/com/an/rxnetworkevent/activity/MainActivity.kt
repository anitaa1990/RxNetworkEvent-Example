package com.an.rxnetworkevent.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast

import com.an.rxnetworkevent.R
import com.an.rxnetworkevent.databinding.MainActivityBinding
import com.an.rxnetworkevent.loader.LoaderHelper
import com.an.rxnetworkevent.viewmodel.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseView()
        initialiseViewModel()
    }

    private fun initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.login.setOnClickListener { view -> mainViewModel
                .validateLoginDetails(binding.email.text.toString(),
                        binding.password.text.toString())
        }
    }

    private fun initialiseViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.getUsernameErrorValidationState().observe(this, Observer {
            binding.email.requestFocus()
            binding.email.error = it
        })

        mainViewModel.getPasswordErrorValidationState().observe(this, Observer {
            binding.password.requestFocus()
            binding.password.error = it
        })
        mainViewModel.getLoadingState().observe(this, Observer {
            if (it!!.isLoading) displayLoader()
            else
                hideLoader()
        })

        mainViewModel.getResponseState().observe(this, Observer {
            if (it!!.isResponseSuccessful) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
            } else {
                Toast.makeText(applicationContext, "Login not successful", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayLoader() {
        LoaderHelper.getInstance().displayLoader(this)
    }

    private fun hideLoader() {
        LoaderHelper.getInstance().dismissDialog()
    }
}
