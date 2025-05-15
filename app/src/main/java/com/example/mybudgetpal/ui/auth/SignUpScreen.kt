package com.example.mybudgetpal.ui.auth

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mybudgetpal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun SignupScreen(navController: NavController) {
    val font = FontFamily(Font(R.font.cs_blacken))

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    fun validateInputs(): Boolean {
        emailError = email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        passwordError = password.length < 6
        confirmPasswordError = password != confirmPassword
        nameError = name.isBlank()

        return !emailError && !passwordError && !confirmPasswordError && !nameError && termsAccepted
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { task2 ->
                    if (task2.isSuccessful) {
                        Toast.makeText(context, "Google Sign-In successful", Toast.LENGTH_SHORT).show()
                        navController.navigate("home") {
                            popUpTo("signup") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, task2.exception?.message ?: "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.localizedMessage ?: "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun launchGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(context, gso)
        launcher.launch(client.signInIntent)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
                Text("Create Account", fontSize = 32.sp, fontFamily = font, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name", color = Color.White) },
                isError = nameError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.1f)),
                textStyle = TextStyle(color = Color.White)
            )
            if (nameError) Text("Name cannot be empty", color = Color.Red, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                isError = emailError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.1f)),
                textStyle = TextStyle(color = Color.White)
            )
            if (emailError) Text("Invalid email address", color = Color.Red, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.White) },
                isError = passwordError,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Text(
                        text = if (showPassword) "Hide" else "Show",
                        color = Color.LightGray,
                        modifier = Modifier.clickable { showPassword = !showPassword }
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.1f)),
                textStyle = TextStyle(color = Color.White)
            )
            if (passwordError) Text("Password must be at least 6 characters", color = Color.Red, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password", color = Color.White) },
                isError = confirmPasswordError,
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Text(
                        text = if (showConfirmPassword) "Hide" else "Show",
                        color = Color.LightGray,
                        modifier = Modifier.clickable { showConfirmPassword = !showConfirmPassword }
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.1f)),
                textStyle = TextStyle(color = Color.White)
            )
            if (confirmPasswordError) Text("Passwords do not match", color = Color.Red, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "I agree to the Terms & Conditions",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            if (!termsAccepted) {
                Text(
                    text = "You must accept the Terms & Conditions",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (validateInputs()) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, task.exception?.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Please fix the errors above.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Sign Up", fontFamily = font)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("OR", color = Color.White, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { launchGoogleSignIn() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Already have an account? Login",
                color = Color.LightGray,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }
    }
}

