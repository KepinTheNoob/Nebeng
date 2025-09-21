package com.example.numpang.data.model

import java.util.Date

data class Ride(
    val id: String = "",
    val driverId: String = "",
    val origin: String = "",
    val destination: String = "",
    val date: Date? = null,
    val seatsAvailable: Int = 0,
    val pricePerSeat: Double = 0.0,
    val passengers: List<String> = emptyList(),
    val createdAt: Date? = null,
    val status: RideStatus = RideStatus.ACTIVE
)

enum class RideStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}