package com.tcron.app.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tcron.core.common.ThemeManager
import com.tcron.core.common.SystemInfoManager
import com.tcron.feature.home.HomeScreen
import com.tcron.feature.home.ScriptPickerScreen
import com.tcron.feature.home.NotificationsScreen
import com.tcron.feature.home.CreatePythonScriptScreen
import com.tcron.feature.home.CreateShellScriptScreen
import com.tcron.feature.settings.SettingsScreen
import com.tcron.feature.settings.LogsScreen
import com.tcron.feature.settings.PluginsScreen
import com.tcron.feature.settings.HelpScreen
import com.tcron.feature.terminal.TerminalScreen
import com.tcron.feature.task.CreateTaskScreen

@Composable
fun TCronNavigation(
    navController: NavHostController,
    onOpenDrawer: () -> Unit = {}
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
                },
                onNavigateToScriptPicker = {
                    navController.navigate("script_picker")
                },
                onNavigateToNotifications = {
                    navController.navigate("notifications")
                },
                onNavigateToCreatePythonScript = {
                    navController.navigate("create_python_script")
                },
                onNavigateToCreateShellScript = {
                    navController.navigate("create_shell_script")
                },
                onOpenDrawer = onOpenDrawer
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
            CreateTaskScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTaskCreated = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("task_detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            // TODO: Implement TaskDetailScreen
        }
        
        composable("script_picker") {
            ScriptPickerScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onScriptSelected = { script ->
                    // TODO: Handle script selection
                    navController.popBackStack()
                }
            )
        }
        
        composable("notifications") {
            NotificationsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("logs") {
            LogsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("plugins") {
            PluginsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("help") {
            HelpScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("create_python_script") {
            CreatePythonScriptScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onScriptSaved = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("create_shell_script") {
            CreateShellScriptScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onScriptSaved = {
                    navController.popBackStack()
                }
            )
        }
    }
}