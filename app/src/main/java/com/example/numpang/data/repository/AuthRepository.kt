package com.bintangjaya.cashierapp.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository (
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("User not found"))

            db.collection("users")
                .document(user.uid)
                .update("lastLogin", Timestamp.now())
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        nim: String,
        phone: String,
        photoUrl: String,
    ): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("User register failed"))

            val userDoc = hashMapOf(
                "name" to name,
                "nim" to nim,
                "phone" to phone,
                "email" to email,
                "photoUrl" to photoUrl,
                "rating" to null,
                "ridesOfferedCount" to 0,
                "createdAt" to Timestamp.now(),
                "role" to "Rider",
                "verified" to false
            )

            db.collection("users")
                .document(user.uid)
                .set(userDoc)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return  auth.currentUser
    }

    suspend fun getUserRole(uid: String): Result<String> {
        return try {
            val snapshot = db.collection("users")
                .document(uid)
                .get()
                .await()

            val role = snapshot.getString("role") ?: return Result.failure(Exception("Role not found"))
            Result.success(role)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}