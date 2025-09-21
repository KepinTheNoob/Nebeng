package com.example.numpang.ui.screen.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import com.example.numpang.AuthState
import com.example.numpang.AuthViewModel
import com.example.numpang.data.Screen
import kotlin.text.ifEmpty

@Composable
fun Register(authViewModel: AuthViewModel, navController: NavController) {
    var showPassword by remember { mutableStateOf(value = false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Authenticated -> navController.navigate(Screen.Home.name) {
                popUpTo(Screen.Signup.name) { inclusive = true }
            }
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Let’s create your account!", fontSize = 26.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(101.5.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.width(300.dp),
            label = { Text(emailError.ifEmpty { "Email" }, color = if(emailError.isNotEmpty()) Red else Unspecified) }
        )

        Spacer(modifier = Modifier.height(43.5.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.width(300.dp),
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            },
            label = { Text(passwordError.ifEmpty { "Password" },
            color = if(passwordError.isNotEmpty()) Red else Unspecified) }
        )

        Spacer(modifier = Modifier.height(43.5.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.width(300.dp),
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            },
            label = { Text(confirmPasswordError.ifEmpty { "Confirm password" },
            color = if(confirmPasswordError.isNotEmpty()) Red else Unspecified) }
        )

        Spacer(modifier = Modifier.height(39.dp))

        Button(
            onClick = {
                emailError =
                    if(email.isBlank()) "Email is required!"
                    else if(!email.contains("@")) "Email must contain '@'!"
                    else ""
                passwordError =
                    if(password.isBlank()) "Password is required!"
                    else if(password.length < 8) "Password must have at least 8 characters!"
                    else ""
                confirmPasswordError =
                    if(confirmPassword.isBlank()) "Confirm is required!"
                    else if(confirmPassword != password) "Must be same with password!"
                    else ""
                if(emailError.isEmpty() && passwordError.isEmpty() && confirmPasswordError.isEmpty()) {
                    authViewModel.signup(email, password)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFADF9B2),
                contentColor = Color.Black
            ),
            modifier = Modifier.width(300.dp).height(54.dp),
            shape = RoundedCornerShape(24)
        ) {
            Text("Register", fontSize = 24.sp  )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                navController.navigate(Screen.Login.name)
            },
            enabled = authState.value != AuthState.Loading,
        ) {
            Text(text = "Already have an account?")
        }
    }
}