package com.antv.mock.repo

import android.util.Log
import com.antv.mock.TAG
import com.antv.mock.model.Question
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class QuestionRepo {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getQuestions(): List<Question> {
        return try {
            val result = db.collection("questions").get().await()
            val questions = mutableListOf<Question>()
            for (document in result) {
//                document.toObject(Question::class.java)
                val q = document.getString("q") ?: ""
                val a = document.getString("a") ?: ""
                val b = document.getString("b") ?: ""
                val c = document.getString("c") ?: ""
                val d = document.getString("d") ?: ""
                val answer = document.getString("answer") ?: ""

                val question = Question(q, a, b, c, d, answer)
                questions.add(question)
            }
            questions.shuffled()
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            emptyList()
        }
    }

    fun addQuestion(question: Question) {
        db.collection("questions")
            .add(question)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun saveResult(correct: Int, wrong: Int) {
        val result = hashMapOf(
            "correct" to correct,
            "wrong" to wrong
        )
        db.collection("results")
            .add(result)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


}