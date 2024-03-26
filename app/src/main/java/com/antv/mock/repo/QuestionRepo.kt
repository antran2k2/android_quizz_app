package com.antv.mock.repo

import com.antv.mock.model.Question
import com.antv.mock.model.Score

interface QuestionRepo {
    suspend fun getQuestions(): List<Question>

    fun saveResult(result: Score)


}