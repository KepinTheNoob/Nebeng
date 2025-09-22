package com.example.numpang.data.model

import java.util.Date

data class Transactions(
    val id: String = "",
    val rideId: String = "",
    val riderId: String = "",
    val amount: Double = 0.0,
    val seatsBooked: Int = 1,
    val status: TransactionStatus = TransactionStatus.PENDING,
    val createdAt: Date? = null,
)

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    CANCELLED,
    REFUNDED
}
