package com.antv.mock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.antv.mock.databinding.ActivityResultBinding
import com.antv.mock.viewModel.ResultViewModel

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val countCorrect = intent.getIntExtra("correct", 0)
        val countWrong = intent.getIntExtra("wrong", 0)

        val binding = ActivityResultBinding.inflate(layoutInflater)

        binding.result = ResultViewModel(this, countCorrect, countWrong)

        setContentView(binding.root)
    }
}