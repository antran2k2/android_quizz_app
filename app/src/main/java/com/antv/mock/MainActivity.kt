package com.antv.mock

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.antv.mock.model.Question
import com.antv.mock.repo.QuestionRepo
import com.google.firebase.auth.FirebaseAuth

val TAG = "AnTVLog"

class MainActivity : AppCompatActivity() {

    val questionRepository = QuestionRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setTitle(R.string.header)


        val logOutButton = findViewById<Button>(R.id.buttonLogout)

        val startQuizButton = findViewById<Button>(R.id.buttonStart)

        val questions = listOf(
            Question("What is the capital of France?",  "London", "Berlin", "Paris","Rome", "c"),
            Question("Which planet is known as the Red Planet?", "Mars", "Venus", "Jupiter", "Saturn", "a"),
            Question("What is the chemical symbol for water?",  "CO2", "O2", "N2","H2O", "d"),
            Question("Who wrote 'Romeo and Juliet'?",  "Charles Dickens","William Shakespeare", "Jane Austen", "Mark Twain", "b"),
            Question("What is the largest mammal in the world?", "Blue whale", "Elephant", "Giraffe", "Hippopotamus", "a"),
            Question("What is the tallest mountain in the world?", "Mount Everest", "K2", "Kangchenjunga", "Lhotse", "a"),
            Question("Which country is famous for kangaroos?",  "Brazil", "Canada", "India","Australia", "d"),
            Question("What is the chemical symbol for gold?",  "Ag", "Au","Fe", "Pt", "b"),
            Question("Who painted the Mona Lisa?",  "Vincent van Gogh", "Pablo Picasso","Leonardo da Vinci", "Michelangelo", "c"),
            Question("What is the largest organ of the human body?", "Skin", "Heart", "Brain", "Liver", "a")
        )

        logOutButton.setOnClickListener {

//            questions.forEach {
//                questionRepository.addQuestion(it)
//            }

            FirebaseAuth.getInstance().signOut()
            navigateToLogin()
            finish()
        }
        startQuizButton.setOnClickListener {
            navigateToQuiz()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToQuiz() {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }


}