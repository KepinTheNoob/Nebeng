package com.example.numpang.ui.screen.driver

import androidx.compose.foundation.clickable
import com.example.numpang.ui.screen.rider.RidesViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numpang.data.model.Rides

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Driver(
    viewModel: RidesViewModel = viewModel(),
    onBackClick: () -> Unit,
    onAddRideClick: () -> Unit,
    onRideClick: (Rides) -> Unit
) {
    val rides by viewModel.rides.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Rides") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddRideClick) {
                        Icon(Icons.Default.Add, contentDescription = "Add Ride")
                    }
                }
            )
        }
    ) { padding ->
        if (rides.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("You haven’t created any rides yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rides, key = { it.driverId }) { ride ->
                    RideCardDriver(ride = ride, onClick = { onRideClick(ride) })
                }
            }
        }
    }
}

@Composable
fun RideCardDriver(
    ride: Rides,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("${ride.origin.address} → ${ride.destination.address}", style = MaterialTheme.typography.titleMedium)
            Text("Seats: ${ride.seatsAvailable}")
            Text("Price: Rp${ride.price}")
            Text("Status: ${ride.status}")
        }
    }
}
