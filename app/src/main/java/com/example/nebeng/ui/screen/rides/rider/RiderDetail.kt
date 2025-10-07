package com.example.nebeng.ui.screen.rides.rider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nebeng.data.model.Rides
import com.example.nebeng.data.model.Users

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RideDetailContent(
//    rides: Rides,
//    driver: Users,
//    onBookClick: (Rides) -> Unit
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Ride Details") }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "${rides.origin} -> ${rides.destination}",
//                style = MaterialTheme.typography.headlineSmall
//            )
//
//            Text("Date: ${rides.dateTime}")
//            Text("Seats Available: ${rides.seatsAvailable}")
//            Text("Price: ${rides.price}")
//
//            Divider()
//
//            Text(
//                text = "Driver: ${driver.name}",
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            Text("Rating: ${driver.rating}")
//
//            Spacer(Modifier.weight(1f)) // push button to bottom
//
//            Button(
//                onClick = { onBookClick(rides) },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Book Ride")
//            }
//        }
//    }
//}


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.nebeng.ui.screen.rides.RidesViewModel

@Composable
fun RideDetailScreen(
    rideId: String,
    rideViewModel: RidesViewModel,
    onBookClick: (Rides) -> Unit
) {
    var ride by remember { mutableStateOf<Rides?>(null) }
    var driver by remember { mutableStateOf<Users?>(null) }

    LaunchedEffect(rideId) {
        val fetchedRide = rideViewModel.getRideById(rideId)
        ride = fetchedRide
        if (fetchedRide != null) {
            driver = rideViewModel.getDriverById(fetchedRide.driverId)
        }
    }

    if (ride != null && driver != null) {
        RideDetailContent(
            rides = ride!!,
            driver = driver!!,
            onBookClick = onBookClick
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun RideDetailContent(
    rides: Rides,
    driver: Users,
    onBookClick: (Rides) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Ride info
        Text(text = "Ride Details", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        Text("From: ${rides.origin.address}")
        Text("To: ${rides.destination.address}")
        Text("Seats Available: ${rides.seatsAvailable}")
        Text("Price: Rp${rides.price}")
        Text("Status: ${rides.status}")
        Spacer(modifier = Modifier.height(16.dp))

        // Driver info
        Text(text = "Driver Info", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Name: ${driver.name}")
        Text("Email: ${driver.email}")
        Text("Phone: ${driver.phone}")
        Spacer(modifier = Modifier.height(16.dp))

        // Book button
        Button(
            onClick = { onBookClick(rides) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Book Ride")
        }
    }
}

