package com.example.mybudgetpal

import android.content.Context
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun QuoteScreen(navController: NavController) {
    val context = LocalContext.current

    // Set this to the number of quote images you have (e.g., 5 for 1.png to 5.png)
    val totalQuotes = 5

    // Helper to get drawable resource ID by name
    fun getDrawableIdByName(name: String, context: Context): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

    // List of drawable IDs for 1.png, 2.png, ..., totalQuotes.png
    val quoteImageIds = remember {
        (1..totalQuotes).map { index ->
            getDrawableIdByName(index.toString(), context)
        }
    }

    var currentIndex by remember { mutableStateOf(0) }
    var animateTrigger by remember { mutableStateOf(0) }

    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }

    // Cycle through images every 2 seconds
    LaunchedEffect(currentIndex) {
        delay(2000)
        currentIndex = (currentIndex + 1) % quoteImageIds.size
        animateTrigger++
    }

    // Pop-in animation for each image
    LaunchedEffect(animateTrigger) {
        scale.snapTo(0.8f)
        alpha.snapTo(0f)
        scale.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
        alpha.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
    }

    // After 6 seconds, check auth and navigate
    LaunchedEffect(Unit) {
        delay(6000)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            navController.navigate("home") { popUpTo("quotes") { inclusive = true } }
        } else {
            navController.navigate("login") { popUpTo("quotes") { inclusive = true } }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = quoteImageIds[currentIndex]),
            contentDescription = "Money Saving Quote",
            modifier = Modifier
                .size(320.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    alpha = alpha.value
                }
        )
    }
}
