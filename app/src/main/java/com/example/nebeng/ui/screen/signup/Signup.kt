package com.example.nebeng.ui.screen.signup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.example.nebeng.AuthState
import com.example.nebeng.AuthViewModel
import com.example.nebeng.data.Screen
import java.io.ByteArrayOutputStream
import kotlin.text.ifEmpty

private data class SignupFormState(
    val email: String = "",
    val name: String = "",
    val nim: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val selectedImageUri: Uri? = null,
    val emailError: String = "",
    val nameError: String = "",
    val nimError: String = "",
    val phoneError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
    val photoUrlError: String = ""
) {
    fun validate(): SignupFormState {
        val emailError = if (email.isBlank()) "Binusian Email is required!" else if (!email.contains("@binus")) "Email must contain '@'!" else ""
        val nameError = if (name.isBlank()) "Name is required!" else ""
        val phoneError = if (phone.isBlank()) "Phone number is required!" else if (phone.length < 7) "Phone number must have at least 8 characters!" else ""
        val passwordError = if (password.isBlank()) "Password is required!" else if (password.length < 8) "Password must have at least 8 characters!" else ""
        val confirmPasswordError = if (confirmPassword.isBlank()) "Confirm is required!" else if (confirmPassword != password) "Must be same with password!" else ""
        val photoUrlError = if (selectedImageUri == null) "Image is required!" else ""

        return this.copy(
            emailError = emailError,
            nameError = nameError,
            phoneError = phoneError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            photoUrlError = photoUrlError
        )
    }

    fun hasErrors(): Boolean {
        return emailError.isNotEmpty() || nameError.isNotEmpty() || nimError.isNotEmpty() || phoneError.isNotEmpty() ||
                passwordError.isNotEmpty() || confirmPasswordError.isNotEmpty() || photoUrlError.isNotEmpty()
    }
}

@Composable
fun Register(authViewModel: AuthViewModel, navController: NavController) {
    var showPassword by remember { mutableStateOf(false) }
    var formState by remember { mutableStateOf(SignupFormState()) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        formState = formState.copy(selectedImageUri = uri)
    }

    LaunchedEffect(authState.value) {
        when (val state = authState.value) {
            is AuthState.Authenticated -> navController.navigate(Screen.Home.name) {
                popUpTo(Screen.Signup.name) { inclusive = true }
            }
            is AuthState.Error -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Let’s create your account!", fontSize = 26.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(61.5.dp))

        OutlinedTextField(
            value = formState.email,
            onValueChange = { formState = formState.copy(email = it) },
            modifier = Modifier.width(300.dp),
            label = { Text(formState.emailError.ifEmpty { "Email" }, color = if (formState.emailError.isNotEmpty()) Color.Red else Color.Unspecified) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.name,
            onValueChange = { formState = formState.copy(name = it) },
            modifier = Modifier.width(300.dp),
            label = { Text(formState.nameError.ifEmpty { "Name" }, color = if (formState.nameError.isNotEmpty()) Color.Red else Color.Unspecified) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.phone,
            onValueChange = { formState = formState.copy(phone = it) },
            modifier = Modifier.width(300.dp),
            label = { Text(formState.phoneError.ifEmpty { "Phone" }, color = if (formState.phoneError.isNotEmpty()) Color.Red else Color.Unspecified) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Profile Picture")
        }

        formState.selectedImageUri?.let {
            AsyncImage(model = it, contentDescription = "Preview", modifier = Modifier.size(100.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        val passwordVisualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()

        OutlinedTextField(
            value = formState.password,
            onValueChange = { formState = formState.copy(password = it) },
            modifier = Modifier.width(300.dp),
            visualTransformation = passwordVisualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "toggle password visibility"
                    )
                }
            },
            label = { Text(formState.passwordError.ifEmpty { "Password" }, color = if (formState.passwordError.isNotEmpty()) Color.Red else Color.Unspecified) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.confirmPassword,
            onValueChange = { formState = formState.copy(confirmPassword = it) },
            modifier = Modifier.width(300.dp),
            visualTransformation = passwordVisualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "toggle password visibility"
                    )
                }
            },
            label = { Text(formState.confirmPasswordError.ifEmpty { "Confirm password" }, color = if (formState.confirmPasswordError.isNotEmpty()) Color.Red else Color.Unspecified) }
        )

        Spacer(modifier = Modifier.height(39.dp))

        Button(
            onClick = {
                val validatedState = formState.validate()
                formState = validatedState
                if (!validatedState.hasErrors()) {
                    validatedState.selectedImageUri?.let { uri ->
                        val base64Image = uriToBase64(context, uri)
                        authViewModel.signup(
                            validatedState.email,
                            validatedState.password,
                            validatedState.name,
                            validatedState.phone,
                            base64Image
                        )
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFADF9B2),
                contentColor = Color.Black
            ),
            modifier = Modifier.width(300.dp).height(54.dp),
            shape = RoundedCornerShape(24)
        ) {
            Text("Register", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate(Screen.Login.name) },
            enabled = authState.value != AuthState.Loading,
        ) {
            Text(text = "Already have an account?")
        }
    }
}

fun uriToBase64(context: Context, uri: Uri, maxSizeInBytes: Int = 500_000): String {
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
    } else {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    val resized = Bitmap.createScaledBitmap(bitmap, 300, 300, true)

    var quality = 80
    var byteArray: ByteArray

    do {
        val outputStream = ByteArrayOutputStream()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            resized.compress(Bitmap.CompressFormat.WEBP_LOSSY, quality, outputStream)
        } else {
            resized.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }
        byteArray = outputStream.toByteArray()
        quality -= 10
    } while (byteArray.size > maxSizeInBytes && quality > 20)

    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}