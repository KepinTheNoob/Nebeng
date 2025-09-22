package com.example.numpang.data

import androidx.annotation.StringRes
import com.example.numpang.R

enum class Screen(@StringRes val title: Int) {
    Login(title = R.string.login),
    Signup(title = R.string.signup),
    Home(title = R.string.home),
}