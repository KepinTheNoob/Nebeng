package com.example.numpang.ui.screen.rider

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.numpang.data.model.Rides
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rider(
    viewModel: RidesViewModel = viewModel(),
    onRideClick: (Rides) -> Unit,
    onBackClick: () -> Unit
) {
    val rides by viewModel.rides.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Avaiable Rides") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        RideList(
            rides = rides,
            onRideClick = onRideClick,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun RideList(
    rides: List<Rides>,
    onRideClick: (Rides) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Available Rides", style = MaterialTheme.typography.titleLarge)

        if (rides.isEmpty()) {
            Text(
                text = "No rides available",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items = rides, key = { it.driverId }) { ride ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRideClick(ride) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "${ride.origin} → ${ride.destination}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("Seats: ${ride.seatsAvailable}")
                            Text("Price: Rp${ride.price}")
                        }
                    }
                }
            }
        }
    }
}