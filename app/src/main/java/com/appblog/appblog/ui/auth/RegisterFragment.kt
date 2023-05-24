package com.appblog.appblog.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.appblog.appblog.R
import com.appblog.appblog.core.Result
import com.appblog.appblog.data.remote.auth.AuthDataSource
import com.appblog.appblog.databinding.FragmentRegisterBinding
import com.appblog.appblog.domain.auth.AuthRepoImpl
import com.appblog.appblog.presentation.auth.AuthViewModel
import com.appblog.appblog.presentation.auth.AuthViewModelFactory


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        singUp()

    }

    private fun singUp(){

        binding.btnSingup.setOnClickListener {

            val username = binding.editTextUsername.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

            if (validateUserData(username, email, password, confirmPassword)) return@setOnClickListener

            createUser(username, email, password)

        }

    }

    private fun createUser(username: String, email: String, password: String) {
        viewModel.singUp(username,email, password).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSingup.isEnabled = false
                }

                is Result.Succes ->{
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)

                }

                is Result.Faliure ->{
                    binding.progressBar.visibility = View.GONE
                    binding.btnSingup.isEnabled = true
                    Toast.makeText(requireContext(), "Error: ${result.exception}", Toast.LENGTH_SHORT).show()

                }

            }
        })
    }

    private fun validateUserData(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (username.isEmpty()) {
            binding.editTextUsername.error = "Username is empty"
            return true
        }

        if (email.isEmpty()) {
            binding.editTextEmail.error = "Email is empty"
            return true
        }

        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password is empty"
            return true
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "Password is empty"
            return true
        }

        if (password != confirmPassword) {
            binding.editTextConfirmPassword.error = "Password does not match"
            binding.editTextPassword.error = "Password does not match"
            return true
        }
        return false
    }

}