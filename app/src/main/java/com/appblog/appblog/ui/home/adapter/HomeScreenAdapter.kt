package com.appblog.appblog.ui.home.adapter

import android.content.Context
import android.util.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appblog.appblog.core.BaseViewHolder
import com.appblog.appblog.core.TimeAgo
import com.appblog.appblog.data.model.Post
import com.appblog.appblog.databinding.PostItemViewBinding
import com.bumptech.glide.Glide

class HomeScreenAdapter(private val postList: List<Post>): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = PostItemViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)

        return HomeScreenViewHolder(itemBinding, parent.context)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is HomeScreenViewHolder -> holder.bind(postList[position])
        }
    }

    private inner class HomeScreenViewHolder(
        val binding : PostItemViewBinding,
        val context : Context):
        BaseViewHolder<Post>(binding.root) {
        override fun bind(item: Post) {

            setupProfileInfo(item)
            addPostTimestamp(item)
            setUpPostImage(item)
            setUpPostDescription(item)

        }

        private fun setupProfileInfo(post: Post){
            Glide.with(context).load(post.profile_picture).centerCrop().into(binding.profilePicture)
            binding.profileName.text = post.profile_name
        }

        private fun addPostTimestamp(post: Post){
            val createdAt = (post.created_at?.time?.div(1000L))?.let {
                TimeAgo.getTimeAgo(it.toInt())
            }
            binding.postTimestamp.text = "$createdAt"
        }

        private fun setUpPostImage(post: Post){
            Glide.with(context).load(post.post_image).centerCrop().into(binding.postImage)
        }

        private fun setUpPostDescription(post: Post){
            if (post.post_description.isEmpty()){
                binding.postDescription.visibility = View.GONE
            }else{
                binding.postDescription.text = post.post_description
            }
        }

    }

}