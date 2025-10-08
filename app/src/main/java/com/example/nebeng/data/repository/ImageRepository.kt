package com.example.nebeng.data.repository

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class ImageRepository {
    fun uploadPic(
        imageFile: File,
        uploadPreset: String,
        onResult: (String?) -> Unit
    ) {
        val client = OkHttpClient()

        val uid = AuthRepository().getCurrentUserId()

        val formData = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", imageFile.name, imageFile.asRequestBody("image/*".toMediaTypeOrNull()))
            .addFormDataPart("upload_preset", uploadPreset)
            .addFormDataPart("public_id", "users/$uid/profile") // UID as key
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/dz9p6wwzt/image/upload")
            .post(formData)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onResult(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        onResult(null)
                        return
                    }
                    val json = JSONObject(response.body?.string() ?: "{}")
                    val photoUrl = json.optString("secure_url", null)
                    onResult(photoUrl)
                }
            }
        })
    }
}
