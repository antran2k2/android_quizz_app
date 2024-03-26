package com.antv.mock.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.antv.mock.R
import com.antv.mock.databinding.FragmentHomeBinding
import com.antv.mock.viewModel.AuthViewModel
import com.antv.mock.viewModel.AuthViewModelFactory

class HomeFragment  : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.home = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()


        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            navController.navigate(R.id.action_homeFragment_to_authActivity)
            activity?.finish()
        }
        binding.buttonStartQuiz.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_quizFragment)
        }

    }


}