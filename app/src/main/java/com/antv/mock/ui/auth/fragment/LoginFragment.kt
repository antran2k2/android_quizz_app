package com.antv.mock.ui.auth.fragment

import android.content.Intent
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
import com.antv.mock.databinding.FragmentLoginBinding
import com.antv.mock.viewModel.AuthViewModel
import com.antv.mock.viewModel.AuthViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

    }
    override fun onStart() {
        super.onStart()
        viewModel.checkCurrentUser()?.let { _ ->
            navController.navigate(R.id.action_login_to_home)
            activity?.finish()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.loginViewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        viewModel.userLiveData.apply {
            observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    navController.navigate(R.id.action_login_to_home)
                    activity?.finish()
                }
            }
        }


        binding.loginButton.setOnClickListener {
            viewModel.handleLogin()
        }

        binding.loginWithGoogle.setOnClickListener {
            viewModel.signInWithGoogle(this)
        }

        binding.signUpText.setOnClickListener {
            navController.navigate(R.id.action_login_to_sign_up)
        }

        binding.forgotPassword.setOnClickListener {
            viewModel.onForgotPasswordClicked()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        viewModel.onActivityResult(requestCode, resultCode, data)

    }
}