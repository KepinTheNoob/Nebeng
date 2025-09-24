package com.example.numpang.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.numpang.AuthViewModel
import com.example.numpang.R
import com.example.numpang.data.model.Rides
import com.example.numpang.data.model.Users
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.numpang.data.Screen
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(authViewModel: AuthViewModel, navController: NavController) {
    var user by remember { mutableStateOf<Users?>(null) }

    LaunchedEffect(Unit) {
        val result = authViewModel.getUserData()
        result.onSuccess { user = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp)
                        )

                        Text("Numpang")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Profile.name) }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.profile),
                            contentDescription = "Profile",
//                            modifier = Modifier
//                                .size(32.dp)
//                                .padding(end = 8.dp)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Column {
                Text(
                    text = "Hi, ${user?.name ?: "Guest"} 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Where are you going today?"
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Button(
                    onClick = { navController.navigate(Screen.Driver.name) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Offer Ride")
                }

                Button(
                    onClick = { navController.navigate(Screen.Rider.name) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Find Ride")
                }
            }

            RideListScreen()
        }
    }
}

@Composable
fun RideListScreen() {
    val rides = remember { mutableStateListOf<Rides>() }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("rides")
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                rides.clear()
                snapshot?.toObjects(Rides::class.java)?.let { rides.addAll(it) }
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rides) { ride ->
            RideCard(ride)
        }
    }
}


@Composable
fun RideCard(ride: Rides) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        val formattedDate = ride.dateTime?.toDate()?.let {
            SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.getDefault()).format(it)
        } ?: "Unknown"
        Text("Date: $formattedDate")

        Column(Modifier.padding(16.dp)) {
            Text("Driver ID: ${ride.driverId} | Seats left: ${ride.seatsAvailable}")
            Text("From: ${ride.origin.address} → To: ${ride.destination.address}")
            Text("Date: $formattedDate") // convert Firestore Timestamp to Date
            Text("Price: Rp ${ride.price}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* TODO: navigate to ride details */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("View Details")
            }
        }
    }
}