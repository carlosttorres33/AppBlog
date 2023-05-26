package com.appblog.appblog.ui.camera

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.appblog.appblog.R
import com.appblog.appblog.core.Result
import com.appblog.appblog.data.remote.camera.CameraDataSource
import com.appblog.appblog.data.remote.home.HomeScreenDataSource
import com.appblog.appblog.databinding.FragmentCameraBinding
import com.appblog.appblog.domain.camera.CameraRepoImpl
import com.appblog.appblog.domain.home.HomeScreenRepoImpl
import com.appblog.appblog.presentation.HomeScreenViewModel
import com.appblog.appblog.presentation.HomeScreenViewModelFactory
import com.appblog.appblog.presentation.camera.CameraViewModel
import com.appblog.appblog.presentation.camera.CameraViewModelFactory

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var binding: FragmentCameraBinding
    private var bitmap: Bitmap? = null
    private val viewModel by viewModels<CameraViewModel> {
        CameraViewModelFactory(
            CameraRepoImpl(
                CameraDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCameraBinding.bind(view)

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }catch (e:ActivityNotFoundException){
            Toast.makeText(requireContext(), "No se encontró app para cámara", Toast.LENGTH_SHORT).show()
        }

        binding.btnUploadPhoto.setOnClickListener {

            bitmap?.let {
                viewModel.uploadPhoto(it, binding.etxtDescription.text.toString().trim()).observe(viewLifecycleOwner,{ result ->
                    when(result){
                        is Result.Loading -> {
                            Toast.makeText(requireContext(),"Uploading photo", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Succes -> {
                            findNavController().navigate(R.id.action_cameraFragment_to_homeScreenFragment)
                        }
                        is Result.Faliure -> {
                            Toast.makeText(requireContext(),"Error: ${result.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            val imageBitmap = data?.extras?.get("data") as Bitmap

            binding.postImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap

        }
    }

}