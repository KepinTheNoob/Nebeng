package com.bintangjaya.cashierapp.data.repository

import com.example.nebeng.data.model.Rides
import com.example.nebeng.data.model.Users
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

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
                "rating" to 0,
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
        return auth.currentUser
    }

    fun getCurrentUserId(): String {
        return auth.currentUser?.uid.toString()
    }

    suspend fun getCurrentUserData(): Result<Users> {
        return try {
            val uid = getCurrentUser()?.uid ?: return Result.failure(Exception("No current user"))

            val snapshot = db.collection("users")
                .document(uid)
                .get()
                .await()

            if(snapshot.exists()) {
                val user = snapshot.toObject(Users::class.java)
                    ?: return Result.failure(Exception("Failed to parse user"))
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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