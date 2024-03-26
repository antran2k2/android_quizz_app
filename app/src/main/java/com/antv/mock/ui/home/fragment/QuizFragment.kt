package com.antv.mock.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.antv.mock.databinding.FragmentQuizBinding
import com.antv.mock.viewModel.QuizViewModel
import com.antv.mock.viewModel.QuizViewModelFactory

class QuizFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentQuizBinding
    private val viewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(inflater)

        binding.quiz = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()


        binding.buttonNext.setOnClickListener {

            viewModel.onClickNext(requireContext(), navController)
        }

        binding.buttonFinish.setOnClickListener {
            Log.d("ANTVLOG", "onClickFinish")
            viewModel.onClickFinish(requireContext(), navController)
        }

    }


}