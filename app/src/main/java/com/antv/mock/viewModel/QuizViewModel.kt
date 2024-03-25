package com.antv.mock.viewModel

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.antv.mock.QuizActivity
import com.antv.mock.ResultActivity
import com.antv.mock.model.Question
import com.antv.mock.repo.QuestionRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val TIME_LIMIT = 10

class QuizViewModel:ViewModel() {

    lateinit var context: QuizActivity

    private val questionRepo = QuestionRepo()

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

        //        onClickNext()
    }


    var index = 0
    fun onClickNext() {
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
            onClickFinish()
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

    fun onClickFinish() {
//        timerJob?.cancel()
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setMessage("Congratulations!\nYou have finished the quiz.\n" +
                    "Do you want to see the result?")
            .setTitle("Quiz game")

            .setPositiveButton("PLAY AGAIN") { dialog, which ->
                onPlayAgain()
            }
            .setNegativeButton("SEE RESULT") { dialog, which ->
                showResult()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
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
    private fun showResult() {
        questionRepo.saveResult(countCorrect.get(), countWrong.get())
        Intent(context, ResultActivity::class.java).also {
            it.putExtra("correct", countCorrect.get())
            it.putExtra("wrong", countWrong.get())
            context.startActivity(it)
            context.finish()
        }
    }
}