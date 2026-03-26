package com.example.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.ui.HomeScreen
import com.example.myapp.ui.KnmiScreen
import com.example.myapp.ui.UsgsScreen
import com.example.myapp.ui.WeatherScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // Homescreen
        composable("home") {
            HomeScreen(
                onNavigateToWeather = { navController.navigate("weather") },
                onNavigateToKNMI = { navController.navigate("knmi") },
                onNavigateToUSGS = { navController.navigate("usgs") }
            )
        }

        // Weather
        composable("weather") {
            WeatherScreen(onBack = { navController.popBackStack() })
        }

        // KNMI aardbevingen (Nederland)
        composable("knmi") {
            KnmiScreen(onBack = { navController.popBackStack() })
        }

        // USGS aardbevingen (wereldwijd)
        composable("usgs") {
            UsgsScreen(onBack = { navController.popBackStack() })
        }
    }
}
