package com.tcron.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tcron.app.navigation.TCronNavigation
import com.tcron.app.ui.theme.TCronTheme
import com.tcron.core.common.ThemeManager
import com.tcron.core.common.BiometricHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    
    @Inject
    lateinit var biometricHelper: BiometricHelper
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeManager: ThemeManager = hiltViewModel()
            val currentTheme by themeManager.currentTheme.collectAsState()
            
            TCronTheme(themeMode = currentTheme) {
                MainScreen()
            }
        }
    }
    
    @Composable
    private fun MainScreen() {
        var isAuthenticated by remember { mutableStateOf(false) }
        var biometricRequired by remember { mutableStateOf(true) }
        var showBiometricError by remember { mutableStateOf(false) }
        var biometricErrorMessage by remember { mutableStateOf("") }
        
        LaunchedEffect(Unit) {
            // Check if biometric is available and required
            biometricRequired = biometricHelper.isBiometricAvailable(this@MainActivity)
            if (!biometricRequired) {
                isAuthenticated = true
            }
        }
        
        when {
            !biometricRequired || isAuthenticated -> {
                // Show main app content
                AppWithNavigation()
            }
            else -> {
                // Show biometric authentication screen
                BiometricAuthScreen(
                    onAuthenticate = {
                        biometricHelper.authenticate(
                            activity = this@MainActivity,
                            title = "Acesso ao TCron",
                            subtitle = "Use sua biometria para acessar o aplicativo",
                            negativeButtonText = "Cancelar",
                            onSuccess = {
                                isAuthenticated = true
                                showBiometricError = false
                            },
                            onError = { message ->
                                biometricErrorMessage = message
                                showBiometricError = true
                            },
                            onFailed = {
                                biometricErrorMessage = "Biometria não reconhecida. Tente novamente."
                                showBiometricError = true
                            }
                        )
                    },
                    onSkip = {
                        isAuthenticated = true
                    },
                    showError = showBiometricError,
                    errorMessage = biometricErrorMessage,
                    onDismissError = {
                        showBiometricError = false
                    }
                )
            }
        }
    }
    
    @Composable
    private fun AppWithNavigation() {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        // Only show drawer on home screen
        val showDrawer = currentRoute == "home"
        
        if (showDrawer) {
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
                AppContent(navController, drawerState, scope)
            }
        } else {
            AppContent(navController, drawerState, scope)
        }
    }
}

@Composable
private fun AppContent(
    navController: androidx.navigation.NavHostController,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope
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

@Composable
private fun BiometricAuthScreen(
    onAuthenticate: () -> Unit,
    onSkip: () -> Unit,
    showError: Boolean,
    errorMessage: String,
    onDismissError: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Icon(
                    Icons.Default.Fingerprint,
                    contentDescription = "Autenticação Biométrica",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "TCron",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Acesso Seguro",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "Use sua biometria para acessar o aplicativo de forma segura",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Button(
                    onClick = onAuthenticate,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Fingerprint,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Autenticar")
                }
                
                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pular Autenticação")
                }
            }
        }
        
        if (showError) {
            AlertDialog(
                onDismissRequest = onDismissError,
                title = {
                    Text("Erro de Autenticação")
                },
                text = {
                    Text(errorMessage)
                },
                confirmButton = {
                    TextButton(onClick = onDismissError) {
                        Text("OK")
                    }
                }
            )
        }
    }
}