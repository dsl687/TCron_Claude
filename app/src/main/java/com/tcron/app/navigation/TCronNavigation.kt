package com.tcron.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tcron.feature.home.HomeScreen
import com.tcron.feature.terminal.TerminalScreen

@Composable
fun TCronNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToTerminal = {
                    navController.navigate("terminal")
                },
                onNavigateToCreateTask = {
                    navController.navigate("create_task")
                },
                onNavigateToTaskDetail = { taskId ->
                    navController.navigate("task_detail/$taskId")
                }
            )
        }
        
        composable("terminal") {
            TerminalScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("create_task") {
            // TODO: Implement CreateTaskScreen
        }
        
        composable("task_detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            // TODO: Implement TaskDetailScreen
        }
        
        composable("dashboard") {
            // TODO: Implement DashboardScreen
        }
        
        composable("settings") {
            // TODO: Implement SettingsScreen
        }
        
        composable("notifications") {
            // TODO: Implement NotificationsScreen
        }
    }
}