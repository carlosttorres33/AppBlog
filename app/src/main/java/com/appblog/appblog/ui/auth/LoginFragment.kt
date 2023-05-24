package com.appblog.appblog.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.appblog.appblog.R
import com.appblog.appblog.core.Result
import com.appblog.appblog.data.remote.auth.AuthDataSource
import com.appblog.appblog.databinding.FragmentLoginBinding
import com.appblog.appblog.domain.auth.AuthRepoImpl
import com.appblog.appblog.presentation.auth.AuthViewModel
import com.appblog.appblog.presentation.auth.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        isUserLoggedIn()
        doLogin()
        goToSingUpPage()

    }

    private fun isUserLoggedIn() {
        firebaseAuth.currentUser?.let {
            findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
        }

    }

    private fun doLogin() {
        binding.btnSingin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            validateCredentials(email, password)
            singIn(email, password)

        }
    }

    private fun goToSingUpPage(){
        binding.txtSingup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun validateCredentials(email: String, password: String) {
        if (email.isEmpty()) {
            binding.editTextEmail.error = "E-mail is empty"
            return
        }

        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password is empty"
            return
        }

    }

    private fun singIn(email: String, password: String) {

        viewModel.singIn(email, password).observe(viewLifecycleOwner, Observer { result ->
            when(result){

                is Result.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSingin.isEnabled = false
                }

                is Result.Succes ->{
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
                }

                is Result.Faliure ->{
                    binding.progressBar.visibility = View.GONE
                    binding.btnSingin.isEnabled = true
                    Toast.makeText(requireContext(), "error: ${result.exception}", Toast.LENGTH_SHORT).show()
                }

            }

        })

    }

}