package com.example.numpang

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.numpang.data.Screen
import com.example.numpang.ui.screen.home.Home
import com.example.numpang.ui.screen.login.Login
import com.example.numpang.ui.screen.profile.Profile
import com.example.numpang.ui.screen.rider.Rider
import com.example.numpang.ui.screen.signup.Register
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainContent(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val authState by authViewModel.authState.observeAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.name,
        modifier = modifier
    ) {
        composable(Screen.Login.name) {
            Login(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(Screen.Signup.name) {
            Register(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(Screen.Home.name) {
            Home(
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable(Screen.Profile.name) {
            Profile(
                authViewModel = authViewModel,
                onEditClick = { navController.navigate("editProfile") },
            )
        }
        composable(Screen.Rider.name) {
            Rider(
                onRideClick = { ride ->
                    println("Clicked on ${ride.origin} → ${ride.destination}")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    context: Context,
    activity: Activity,
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
//
//    SetStatusBarColor()
//
//    DisposableEffect(Unit) {
//        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
//            val user = firebaseAuth.currentUser
//            if (user == null) {
//                navController.navigate(Screen.Login.name) {
//                    popUpTo(0)
//                }
//            } else {
//                navController.navigate(Screen.Home.name) {
//                    popUpTo(0)
//                }
//            }
//        }
//        auth.addAuthStateListener(listener)
//
//        onDispose {
//            auth.removeAuthStateListener(listener)
//        }
//    }

    MainContent(
        navController = navController,
        authViewModel = authViewModel,
    )
}
