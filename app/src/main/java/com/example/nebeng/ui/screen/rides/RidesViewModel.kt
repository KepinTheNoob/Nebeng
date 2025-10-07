package com.example.nebeng.ui.screen.rides

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintangjaya.cashierapp.data.repository.AuthRepository
import com.example.nebeng.data.model.Rides
import com.example.nebeng.data.model.Users
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RidesViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
) : ViewModel() {
    private val _rides = MutableStateFlow<List<Rides>>(emptyList())
    val rides: StateFlow<List<Rides>> = _rides

    private val _driver = MutableStateFlow<Users?>(null)
    val driver: StateFlow<Users?> = _driver

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchRides()
    }

    fun createRide(ride: Rides) {
        viewModelScope.launch {
            try {
                val currentUserId = authRepository.getCurrentUserId()
                ride.copy(driverId = currentUserId)

                val newRide = db.collection("rides").document()
                val docId = newRide.id

                val ride = ride.copy(id = docId)

                newRide.set(ride).await()

                Result.success(ride)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun fetchRides() {
        viewModelScope.launch {
            db.collection("rides")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener

                    val rideList = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(Rides::class.java)
                    } ?: emptyList()

                    _rides.value = rideList
                }
        }
    }

    suspend fun getRideById(id: String): Rides? {
        return try {
            val doc = db.collection("rides").document(id).get().await()
            doc.toObject(Rides::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getDriverById(driverId: String): Users? {
        return try {
            val doc = db.collection("users").document(driverId).get().await()
            doc.toObject(Users::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun bookRide(ride: Rides) {
        viewModelScope.launch {
            try {
                val currentUserId = authRepository.getCurrentUserId()
                val rideRef = db.collection("rides").document(ride.id!!)

                rideRef.update(
                    "passengers", com.google.firebase.firestore.FieldValue.arrayUnion(currentUserId)
                ).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}