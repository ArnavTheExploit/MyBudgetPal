package com.example.mybudgetpal

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Animations
    val logoAnim = remember { Animatable(0.7f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        logoAlpha.animateTo(1f, tween(1100, easing = FastOutSlowInEasing))
        logoAnim.animateTo(1f, tween(1100, easing = FastOutSlowInEasing))
        delay(300)
        textAlpha.animateTo(1f, tween(1000, easing = FastOutSlowInEasing))
        delay(1200)
        navController.navigate("quotes") { popUpTo("splash") { inclusive = true } }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Fullscreen background image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop, // Ensures full screen coverage
            modifier = Modifier.fillMaxSize()
        )

        // Center logo and app name
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo with scale and fade-in
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .shadow(16.dp, RoundedCornerShape(28.dp))
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoAnim.value
                        scaleY = logoAnim.value
                    }
            )
            Spacer(modifier = Modifier.height(24.dp))
            // App name with fade-in and smooth text motion
            Text(
                text = "MyBudgetPal",
                fontSize = 36.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = TextStyle(textMotion = TextMotion.Animated), // Smooth text animation
                modifier = Modifier
                    .graphicsLayer { alpha = textAlpha.value }
                    .shadow(4.dp)
            )
        }

        // Bottom credit
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .graphicsLayer { alpha = textAlpha.value },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "from",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "@ArnavPaniya",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
