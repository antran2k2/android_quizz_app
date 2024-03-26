package com.antv.mock.viewModel

import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel


class ResultViewModel: ViewModel() {

    val countCorrect = ObservableInt()
    val countWrong = ObservableInt()
    init {
        countCorrect.set(1)
        countWrong.set(1)
    }


}