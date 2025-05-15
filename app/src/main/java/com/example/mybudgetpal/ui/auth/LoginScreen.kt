package com.example.mybudgetpal.ui.auth

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mybudgetpal.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val font = FontFamily(Font(R.font.cs_blacken))
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    // Auto-navigate if already logged in
    LaunchedEffect(Unit) {
        if (auth.currentUser != null) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Centered card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center)
        ) {
            Surface(
                shape = RoundedCornerShape(40.dp),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 40.dp, horizontal = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login",
                        fontSize = 32.sp,
                        fontFamily = font,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            loginWithEmailPassword(auth, email, password, context, navController)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2156C9))
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 18.sp,
                            fontFamily = font,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Don’t have an account?",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sign Up",
                            color = Color(0xFF2156C9),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .clickable { navController.navigate("signup") }
                        )
                    }
                }
            }
        }
    }
}

fun loginWithEmailPassword(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: Context,
    navController: NavController
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                Toast.makeText(context, task.exception?.message ?: "Login failed. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
}
