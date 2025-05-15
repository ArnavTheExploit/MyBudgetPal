package com.example.mybudgetpal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mybudgetpal.ui.auth.LoginScreen
import com.example.mybudgetpal.ui.auth.SignupScreen
import com.example.mybudgetpal.SplashScreen
import com.example.mybudgetpal.ui.theme.MyBudgetPalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyBudgetPalTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("signup") { SignupScreen(navController) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyBudgetPalTheme {
        // You can preview the login screen or others here
        // LoginScreen(rememberNavController())
    }
}
