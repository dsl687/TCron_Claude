package com.tcron.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.tcron.app.navigation.TCronNavigation
import com.tcron.app.ui.theme.TCronTheme
import com.tcron.core.common.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var themeManager: ThemeManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TCronTheme(themeMode = themeManager.currentTheme) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        TCronDrawerContent(
                            onNavigate = { route ->
                                scope.launch { drawerState.close() }
                                navController.navigate(route)
                            },
                            onCloseDrawer = {
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Show debug warning if in debug mode
                        if (BuildConfig.IS_DEBUG_BUILD) {
                            DebugWarningBanner()
                        }
                        
                        TCronNavigation(
                            navController = navController,
                            onOpenDrawer = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DebugWarningBanner() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "DEBUG MODE ACTIVE",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(Color.Red)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TCronDrawerContent(
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        // Drawer header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp)
        ) {
            Text(
                text = "TCron Menu",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Navigation items
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Início") },
            selected = false,
            onClick = { onNavigate("home") },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("Configurações") },
            selected = false,
            onClick = { onNavigate("settings") },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Assessment, contentDescription = null) },
            label = { Text("Logs") },
            selected = false,
            onClick = { onNavigate("logs") },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Extension, contentDescription = null) },
            label = { Text("Plugins / Extensões") },
            selected = false,
            onClick = { onNavigate("plugins") },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Help, contentDescription = null) },
            label = { Text("Ajuda / Tutorial") },
            selected = false,
            onClick = { onNavigate("help") },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}