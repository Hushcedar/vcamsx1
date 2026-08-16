package com.axiom.vcam.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.axiom.vcam.ui.screens.AppsScreen
import com.axiom.vcam.ui.screens.HomeScreen

@Composable
fun VCamNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(onNavigateToApps = { nav.navigate("apps") }) }
        composable("apps") { AppsScreen(onBack = { nav.popBackStack() }) }
    }
}
