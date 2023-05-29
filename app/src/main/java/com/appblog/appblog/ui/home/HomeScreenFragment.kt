package com.appblog.appblog.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.appblog.appblog.R
import com.appblog.appblog.core.Result
import com.appblog.appblog.core.hide
import com.appblog.appblog.core.show
import com.appblog.appblog.data.model.Post
import com.appblog.appblog.data.remote.home.HomeScreenDataSource
import com.appblog.appblog.databinding.FragmentHomeScreenBinding
import com.appblog.appblog.domain.home.HomeScreenRepoImpl
import com.appblog.appblog.presentation.HomeScreenViewModel
import com.appblog.appblog.presentation.HomeScreenViewModelFactory
import com.appblog.appblog.ui.home.adapter.HomeScreenAdapter
import com.appblog.appblog.ui.home.adapter.OnPostClickListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen), OnPostClickListener {

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

        /*/Metodo con StateFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.lastestPost.collect{ result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.show()
                        }

                        is Result.Succes -> {
                            binding.progressBar.hide()
                            if (result.data.isEmpty()){
                                binding.emptyContiner.show()
                                return@collect
                            }else{
                                binding.emptyContiner.hide()
                            }
                            binding.rvHome.adapter = HomeScreenAdapter(result.data, this)

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
                }
            }
        }*/

        viewModel.fetchLastestPosts().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }

                is Result.Succes -> {
                    binding.progressBar.hide()
                    if (result.data.isEmpty()){
                        binding.emptyContiner.show()
                        return@Observer
                    }else{
                        binding.emptyContiner.hide()
                    }
                    binding.rvHome.adapter = HomeScreenAdapter(result.data, this)

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

    override fun onLikeButtonClick(post: Post, liked: Boolean) {
        viewModel.registerLikeButtonState(post.id, liked).observe(viewLifecycleOwner){result ->
            when (result) {
                is Result.Loading -> {

                }

                is Result.Succes -> {

                }

                is Result.Faliure -> {
                    Toast.makeText(
                        requireContext(),
                        "Ocurrió un problema: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

}