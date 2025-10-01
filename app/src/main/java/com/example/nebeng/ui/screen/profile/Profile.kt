package com.example.nebeng.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nebeng.AuthViewModel
import com.example.nebeng.data.model.Users

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    authViewModel: AuthViewModel,
    onEditClick: () -> Unit,
) {
    var user by remember { mutableStateOf<Users?>(null) }

    LaunchedEffect(Unit) {
        val result = authViewModel.getUserData()
        result.onSuccess { user = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
//                actions = {
//                    IconButton(onClick = onSettingsClick) {
//                        Icon(
//                            imageVector = Icons.Default.Settings,
//                            contentDescription = "Settings"
//                        )
//                    }
//                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // User Info
            Text("${user?.name}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("${user?.nim}", fontSize = 14.sp, color = Color.Gray)
            Text("${user?.phone}", fontSize = 14.sp)
            Text("${user?.email}", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⭐ Rating", fontWeight = FontWeight.SemiBold)
                    Text("${user?.rating} / 5", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBox("Offered", "${user?.ridesOfferedCount}")
                StatBox("Taken", "${user?.ridesTakenCount}")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onEditClick, modifier = Modifier.fillMaxWidth()) {
                Text("Edit Profile")
            }
        }
    }
}

@Composable
fun StatBox(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.size(140.dp, 100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(title, fontSize = 14.sp, color = Color.Gray)
        }
    }
}
