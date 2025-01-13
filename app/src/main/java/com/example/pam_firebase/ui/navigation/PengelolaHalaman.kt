package com.example.pam_firebase.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pam_firebase.ui.view.HomeScreen
import com.example.pam_firebase.ui.view.InsertView

@Composable
fun PengelolaHalaman(
    modifier: Modifier = Modifier, // Default value to make it optional
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = modifier // Pass the modifier here
    ) {
        // Home route
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToItemEntry = {
                    navController.navigate(DestinasiInsert.route)
                },
                navigateToDetail = { nim ->
                    navController.navigate("${DestinasiDetail.route}/$nim") // Pass nim to detail screen
                }
            )
        }
        // Insert route
        composable(DestinasiInsert.route) {
            InsertView(
                onBack = { navController.popBackStack() },
                onNavigate = {
                    navController.navigate(DestinasiHome.route)
                }
            )
        }
        // Detail rout
    }
}
