package com.antv.mock

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.antv.mock.databinding.ActivitySignUpBinding
import com.antv.mock.viewModel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = SignUpViewModel(this)

    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}