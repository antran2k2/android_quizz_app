package com.antv.mock.repo.impl

import android.util.Log
import com.antv.mock.model.Question
import com.antv.mock.model.Score
import com.antv.mock.repo.QuestionRepo
import com.antv.mock.ui.home.TAG
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class QuestionRepoImpl: QuestionRepo {

    private val db = Firebase.firestore
    override suspend fun getQuestions(): List<Question> {
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



    override fun saveResult(result: Score) {

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