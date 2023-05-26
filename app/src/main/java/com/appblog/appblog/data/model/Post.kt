package com.appblog.appblog.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Post (
                 @Exclude @JvmField
                 val id: String = "",
                 @ServerTimestamp
                 var created_at: Date? = null,
                 val post_image: String ="",
                 val post_description: String = "",
                 val poster: Poster? = null,
                 val likes: Long = 0,
                 @Exclude @JvmField
                 val liked: Boolean = false
                 )

data class Poster (val username: String? = "", val uid: String? = null, val profile_picture: String = "")