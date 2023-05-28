package com.appblog.appblog.ui.home.adapter

import android.content.Context
import android.util.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.appblog.appblog.R
import com.appblog.appblog.core.BaseViewHolder
import com.appblog.appblog.core.TimeAgo
import com.appblog.appblog.data.model.Post
import com.appblog.appblog.databinding.PostItemViewBinding
import com.bumptech.glide.Glide

class HomeScreenAdapter(private val postList: List<Post>, private val onPostClickListener: OnPostClickListener) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var postClickListener: OnPostClickListener? = null

    init {
        postClickListener = onPostClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            PostItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeScreenViewHolder(itemBinding, parent.context)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is HomeScreenViewHolder -> holder.bind(postList[position])
        }
    }

    private inner class HomeScreenViewHolder(
        val binding: PostItemViewBinding,
        val context: Context
    ) :
        BaseViewHolder<Post>(binding.root) {
        override fun bind(item: Post) {

            setupProfileInfo(item)
            addPostTimestamp(item)
            setUpPostImage(item)
            setUpPostDescription(item)
            tintHeartIcon(item)
            setUpLikeCount(item)
            setClickLikeAction(item)

        }

        private fun setupProfileInfo(post: Post) {
            Glide.with(context).load(post.poster?.profile_picture).centerCrop()
                .into(binding.profilePicture)
            binding.profileName.text = post.poster?.username
        }

        private fun addPostTimestamp(post: Post) {
            val createdAt = (post.created_at?.time?.div(1000L))?.let {
                TimeAgo.getTimeAgo(it.toInt())
            }
            binding.postTimestamp.text = "$createdAt"
        }

        private fun setUpPostImage(post: Post) {
            Glide.with(context).load(post.post_image).centerCrop().into(binding.postImage)
        }

        private fun setUpPostDescription(post: Post) {
            if (post.post_description.isEmpty()) {
                binding.postDescription.visibility = View.GONE
            } else {
                binding.postDescription.text = post.post_description
            }
        }

        private fun tintHeartIcon(post: Post) {

            if (!post.liked) {
                binding.btnLike.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.baseline_favorite_border_24
                    )
                )
                binding.btnLike.setColorFilter(ContextCompat.getColor(context, R.color.black))
            } else {
                binding.btnLike.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.baseline_favorite_24
                    )
                )
                binding.btnLike.setColorFilter(ContextCompat.getColor(context, R.color.red_like))
            }

        }

        private fun setUpLikeCount(post: Post) {
            if (post.likes > 0) {
                binding.likeCount.visibility = View.VISIBLE
                binding.likeCount.text = "${post.likes} likes"
            } else {
                binding.likeCount.visibility = View.GONE
            }
        }

        private fun setClickLikeAction(post: Post){
            binding.btnLike.setOnClickListener {
                if (post.liked) post.apply { liked = false } else post.apply { liked = true }
                tintHeartIcon(post)
                postClickListener?.onLikeButtonClick(post, post.liked)
            }
        }

    }

}

interface OnPostClickListener {
    fun onLikeButtonClick(post: Post, liked: Boolean) {

    }
}