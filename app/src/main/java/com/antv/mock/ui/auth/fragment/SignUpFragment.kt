package com.antv.mock.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.antv.mock.R
import com.antv.mock.databinding.FragmentSignUpBinding
import com.antv.mock.viewModel.AuthViewModel
import com.antv.mock.viewModel.AuthViewModelFactory

class SignUpFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            show()
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)

        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater)

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        viewModel.userLiveData.apply {
            observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    navController.navigate(R.id.action_sign_up_to_home)
                }
            }
        }

        binding.signUpButton.setOnClickListener {
            viewModel.handleSignUp()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }


}