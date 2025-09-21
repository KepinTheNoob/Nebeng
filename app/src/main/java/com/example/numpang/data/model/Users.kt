package com.example.numpang.data.model

import java.util.Date

enum class Role {
    Rider,
    Driver,
    Admin
}

data class Users (
    val name: String = "",
    val nim: String = "",
    val phone: String = "",
    val email: String = "",
    val photoUrl: String? = "",
    val rating: Int? = 0,
    val ridesOfferedCount: Int = 0,
    val createdAt: Date? = null,
    val role: Role = Role.Rider,
    val verified: Boolean = true,
)