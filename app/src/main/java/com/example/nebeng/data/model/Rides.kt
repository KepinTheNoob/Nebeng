package com.example.nebeng.data.model

data class Location(
    val address: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

data class Rides(
    val id: String = "",
    val driverId: String = "",
    val origin: Location = Location(),
    val destination: Location = Location(),
    val dateTime: com.google.firebase.Timestamp? = null,
    val price: Double = 0.0,
    val seatsAvailable: Int = 0,
    val passengers: List<String> = emptyList(),
    val createdAt: com.google.firebase.Timestamp? = null,
    val status: String = "open" // open | full | cancelled
)