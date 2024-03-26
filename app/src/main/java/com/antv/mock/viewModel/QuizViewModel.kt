package com.antv.mock.viewModel

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.antv.mock.model.Question
import com.antv.mock.model.Score
import com.antv.mock.repo.impl.QuestionRepoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val TIME_LIMIT = 10


class QuizViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuizViewModel() as T
    }
}

class QuizViewModel: ViewModel() {


    private val questionRepo = QuestionRepoImpl()

    val questions = ObservableArrayList<Question>()

    val correctAnswer = ObservableInt()
    val wrongAnswer = ObservableInt()

    val isLoading = ObservableBoolean()

    val countCorrect = ObservableInt()

    val countWrong = ObservableInt()

    var done = false

    val time = ObservableInt()

    val timeIsUp = ObservableBoolean()

    private var timerJob: Job? = null


    init {
        loadQuestions()

    }
    val thisQuestion: ObservableField<Question> = ObservableField()

    private fun loadQuestions() {
        isLoading.set(true)
        CoroutineScope(Dispatchers.IO).launch {
            val questionsList = questionRepo.getQuestions()
            delay(1000)
            questions.clear()
            questions.addAll(questionsList)
            isLoading.set(false)
            thisQuestion.set(questions[0])
            startTimer()
        }
    }

    fun checkAnswer(answer: String) {

        if (done) return

        when (thisQuestion.get()?.answer) {
            "a" -> correctAnswer.set(1)
            "b" -> correctAnswer.set(2)
            "c" -> correctAnswer.set(3)
            "d" -> correctAnswer.set(4)
        }


        if (answer == thisQuestion.get()?.answer) {
            countCorrect.set(countCorrect.get() + 1)
        } else {
            countWrong.set(countWrong.get() + 1)
            when (answer) {
                "a" -> wrongAnswer.set(1)
                "b" -> wrongAnswer.set(2)
                "c" -> wrongAnswer.set(3)
                "d" -> wrongAnswer.set(4)
            }
        }
        done = true

    }
    private fun onTimeFinished() {

        if (done) return

        timeIsUp.set(true)
        checkAnswer("")

    }


    var index = 0
    fun onClickNext(context: Context, navController: NavController) {
        if (!done) {
            checkAnswer("")
        }

        if (index < questions.size - 1) {
            thisQuestion.set(questions[index + 1])
            wrongAnswer.set(0)
            correctAnswer.set(0)
            timeIsUp.set(false)


            done = false
            index++
            startTimer()
        } else {
            onClickFinish(context, navController)
        }
    }


    private fun startTimer() {
        time.set(TIME_LIMIT)
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            var remainingTime = TIME_LIMIT
            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
                time.set(remainingTime)
            }
            onTimeFinished()
        }
    }

    fun onClickFinish(context: Context, navController: NavController) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setMessage(
                "Congratulations!\nYou have finished the quiz.\n" +
                        "Do you want to see the result?"
            )
            .setTitle("Quiz game")

            .setPositiveButton("PLAY AGAIN") { dialog, which ->
                onPlayAgain()
            }
            .setNegativeButton("SEE RESULT") { dialog, which ->
                val score = Score(countCorrect.get(),countWrong.get()) // Ví dụ với correct = 5 và wrong = 3
                val bundle = Bundle().apply {
                    putParcelable("score_key", score)
                }
                navController.navigate(
                    com.antv.mock.R.id.action_quizFragment_to_resultFragment,
                    bundle
                )

            }
            .show()
    }


    private fun onPlayAgain() {
        countCorrect.set(0)
        countWrong.set(0)
        wrongAnswer.set(0)
        correctAnswer.set(0)
        index = 0
        done = false
        timeIsUp.set(false)
        loadQuestions()
    }

}