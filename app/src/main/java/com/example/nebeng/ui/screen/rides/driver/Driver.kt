package com.example.nebeng.ui.screen.rides.driver

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nebeng.data.repository.AuthRepository
import com.example.nebeng.data.model.Location
import com.example.nebeng.data.model.Rides
import com.example.nebeng.ui.screen.rides.RidesViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Driver(
    viewModel: RidesViewModel,
    onBackClick: () -> Unit,
    onAddRideClick: () -> Unit,
    onRideClick: (Rides) -> Unit
) {
    val rides by viewModel.rides.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val repository = AuthRepository()

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
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Ride")
                    }
                }
            )
        }
    ) { padding ->
        if (rides.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                Text("You haven’t created any rides yet")
                Spacer(Modifier.height(8.dp))
                Button(onClick = onAddRideClick) {
                    Text("Create a Ride")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rides, key = { it.id }) { ride ->
                    RideCardDriver(ride = ride, onClick = { onRideClick(ride) })
                }
            }
        }
    }

    if (showDialog) {
        CreateRideDialog(
            onDismiss = { showDialog = false },
            viewModel = viewModel,
            repository = repository
        )
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
        val formattedPrice = remember(ride.price) {
            NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(ride.price)
        }

        Column(Modifier.padding(16.dp)) {
            Text(
                "${ride.origin.address} → ${ride.destination.address}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(4.dp))

            Text("Seats: ${ride.seatsAvailable}", style = MaterialTheme.typography.bodyMedium)

            Text("Price: $formattedPrice", style = MaterialTheme.typography.bodyMedium)

            Text("Status: ${ride.status}")

            Spacer(Modifier.height(4.dp))

            AssistChip(
                onClick = {},
                label = { Text(ride.status) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = when (ride.status) {
                        "Active" -> MaterialTheme.colorScheme.primaryContainer
                        "Completed" -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.errorContainer
                    }
                )
            )
        }
    }
}

@Composable
fun CreateRideDialog(
    onDismiss: () -> Unit,
    viewModel: RidesViewModel,
    repository: AuthRepository
) {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var seats by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Ride") },
        text = {
            Column {
                OutlinedTextField(
                    value = origin,
                    onValueChange = { origin = it },
                    label = { Text("Origin") }
                )
                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    label = { Text("Destination") }
                )
                OutlinedTextField(
                    value = seats,
                    onValueChange = { seats = it },
                    label = { Text("Seats Available") }
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val ride = Rides(
                        driverId = repository.getCurrentUser()?.uid.toString(), // you’ll need to get this from your AuthViewModel
                        origin = Location(address = origin),
                        destination = Location(address = destination),
                        seatsAvailable = seats.toIntOrNull() ?: 1,
                        price = price.toDoubleOrNull() ?: 0.0,
                        status = "open",
                        createdAt = com.google.firebase.Timestamp.now()
                    )
                    viewModel.createRide(ride)

                    onDismiss()
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
