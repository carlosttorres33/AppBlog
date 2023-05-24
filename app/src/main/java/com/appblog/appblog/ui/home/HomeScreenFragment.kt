package com.appblog.appblog.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.appblog.appblog.R
import com.appblog.appblog.core.Result
import com.appblog.appblog.data.remote.home.HomeScreenDataSource
import com.appblog.appblog.databinding.FragmentHomeScreenBinding
import com.appblog.appblog.domain.home.HomeScreenRepoImpl
import com.appblog.appblog.presentation.HomeScreenViewModel
import com.appblog.appblog.presentation.HomeScreenViewModelFactory
import com.appblog.appblog.ui.home.adapter.HomeScreenAdapter

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(
            HomeScreenRepoImpl(
                HomeScreenDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        viewModel.fetchLastestPosts().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Succes -> {
                    binding.rvHome.adapter = HomeScreenAdapter(result.data)
                    binding.progressBar.visibility = View.GONE
                }

                is Result.Faliure -> {
                    Toast.makeText(
                        requireContext(),
                        "Ocurrió un problema: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.GONE
                }

            }
        })

    }

}