package com.antv.mock

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.antv.mock.databinding.ActivityQuizBinding
import com.antv.mock.viewModel.QuizViewModel

class QuizActivity : AppCompatActivity() {

    val viewModel: QuizViewModel by viewModels<QuizViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        viewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        viewModel.context = this

        val binding = ActivityQuizBinding.inflate(layoutInflater)
        binding.quiz = viewModel
        setContentView(binding.root)
    }
}