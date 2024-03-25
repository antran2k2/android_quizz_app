package com.antv.mock.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableInt
import com.antv.mock.MainActivity
import com.antv.mock.QuizActivity

class ResultViewModel(val context: Context, val correct: Int, val wrong: Int) {

    val countCorrect = ObservableInt()
    val countWrong = ObservableInt()
    init {
        countCorrect.set(correct)
        countWrong.set(wrong)
    }


    fun onPlayAgainClicked() {
        Intent(context, QuizActivity::class.java).also {
            context.startActivity(it)
            (context as Activity).finish()
        }
    }
    fun onExitClicked() {
        Intent(context, MainActivity::class.java).also {
            context.startActivity(it)
            (context as Activity).finish()
        }
    }
}