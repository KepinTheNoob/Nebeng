package com.example.numpang.ui.screen.rider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numpang.data.model.Rides
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RidesViewModel : ViewModel() {
    private val _rides = MutableStateFlow<List<Rides>>(emptyList())
    val rides: StateFlow<List<Rides>> = _rides

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchRides()
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
}
