package com.appblog.appblog.data.remote.camera

import android.graphics.Bitmap
import com.appblog.appblog.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID

class CameraDataSource {

    suspend fun uploadPhoto(imageBitmap: Bitmap, description: String){

        val user = FirebaseAuth.getInstance().currentUser
        val randomName = UUID.randomUUID().toString()
        val imageRef = FirebaseStorage.getInstance().reference.child("${user?.uid}/posts/$randomName")
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val downloarUrl =
            imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()
        user?.let {
            it.displayName?.let { displayName ->

                FirebaseFirestore.getInstance().collection("posts").add(Post(profile_name = displayName,
                profile_picture = it.photoUrl.toString(),
                post_image = downloarUrl,
                post_description = description))
            }

        }


    }

}