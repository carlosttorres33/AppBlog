package com.appblog.appblog.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appblog.appblog.R
import com.appblog.appblog.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileBinding.bind(view)
        val user = FirebaseAuth.getInstance().currentUser
        Glide.with(this).load(user?.photoUrl).centerCrop().into(binding.imgProfile)
        binding.txtProfileName.text = user?.displayName

    }

}