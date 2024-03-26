package com.antv.mock.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.antv.mock.R
import com.antv.mock.databinding.FragmentResultBinding
import com.antv.mock.model.Score
import com.antv.mock.viewModel.ResultViewModel

class ResultFragment: Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentResultBinding
    private val viewModel: ResultViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater)

        binding.result = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        showResult()

        binding.buttonPlayAgain.setOnClickListener {
            navController.navigate(R.id.action_resultFragment_to_quizFragment)
        }
        binding.buttonExit.setOnClickListener {
            navController.navigate(R.id.action_resultFragment_to_homeFragment)
        }

    }

    private fun showResult() {
        val score: Score = arguments?.getParcelable("score_key") ?: Score(0, 0)

        val correct = score.correct
        val wrong = score.wrong

        viewModel.countCorrect.set(correct)
        viewModel.countWrong.set(wrong)
    }
}