package com.antv.mock

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.antv.mock.databinding.ActivityLoginBinding
import com.antv.mock.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity() {



    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()


        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = LoginViewModel(this)

        binding.loginViewModel = loginViewModel
    }
    override fun onStart() {
        super.onStart()
        val currentUser = loginViewModel.checkCurrentUser()
        if (currentUser != null) {
            loginViewModel.navigateToMain()
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.onActivityResult(requestCode, resultCode, data)
    }


}