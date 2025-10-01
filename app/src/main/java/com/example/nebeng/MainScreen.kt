package com.example.nebeng

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
import com.example.nebeng.data.Screen
import com.example.nebeng.ui.screen.home.Home
import com.example.nebeng.ui.screen.login.Login
import com.example.nebeng.ui.screen.profile.Profile
import com.example.nebeng.ui.screen.rides.RidesViewModel
import com.example.nebeng.ui.screen.rides.driver.Driver
import com.example.nebeng.ui.screen.rides.rider.RideDetailScreen
import com.example.nebeng.ui.screen.rides.rider.Rider
import com.example.nebeng.ui.screen.signup.Register
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainContent(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val authState by authViewModel.authState.observeAsState()
    val ridesViewModel: RidesViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.name,
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
                viewModel = ridesViewModel,
                onRideClick = { selectedRide ->
                    navController.navigate("rideDetail/${selectedRide.id}")
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("rideDetail/{rideId}") { backStackEntry ->
            val rideId = backStackEntry.arguments?.getString("rideId") ?: return@composable
            RideDetailScreen(
                rideId = rideId,
                rideViewModel = ridesViewModel,
                onBookClick = { rideToBook ->
                    ridesViewModel.bookRide(rideToBook)
                }
            )
        }
        composable(Screen.Driver.name) {
            Driver(
                viewModel = ridesViewModel,
                onBackClick = { navController.popBackStack() },
                onAddRideClick = { },
                onRideClick = { ride -> }
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
