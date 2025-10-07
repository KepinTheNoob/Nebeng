package com.example.nebeng.data

import androidx.annotation.StringRes
import com.example.nebeng.R

enum class Screen(@StringRes val title: Int) {
    Login(title = R.string.login),
    Signup(title = R.string.signup),
    Home(title = R.string.home),
    Profile(title = R.string.profile),
    Rider(title = R.string.rider),
    Driver(title = R.string.driver),
    RideDetail(title = R.string.rideDetail)
}