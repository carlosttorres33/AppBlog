package com.appblog.appblog.ui.auth

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.appblog.appblog.R
import com.appblog.appblog.core.Result
import com.appblog.appblog.data.remote.auth.AuthDataSource
import com.appblog.appblog.databinding.FragmentSetupProfileBinding
import com.appblog.appblog.domain.auth.AuthRepoImpl
import com.appblog.appblog.presentation.auth.AuthViewModel
import com.appblog.appblog.presentation.auth.AuthViewModelFactory


class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {

    private lateinit var binding: FragmentSetupProfileBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    private val REQUEST_IMAGE_CAPTURE = 1

    private var bitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSetupProfileBinding.bind(view)

        binding.profileImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }catch (e: ActivityNotFoundException){
                Toast.makeText(requireContext(), "No se encontró app de camara", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCreateProfile.setOnClickListener {
            val username = binding.etxtUsername.text.toString().trim()

            val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Uploading photo...").create()

            bitmap?.let {
                if (username.isNotEmpty()){
                    viewModel.updateUserProfile(imageBitmap = bitmap!!, username = username).observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                alertDialog.show()
                            }

                            is Result.Succes -> {
                                alertDialog.dismiss()
                                findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                            }

                            is Result.Faliure -> {
                                alertDialog.dismiss()
                            }
                        }
                    }
                }
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.profileImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
        }
    }

}