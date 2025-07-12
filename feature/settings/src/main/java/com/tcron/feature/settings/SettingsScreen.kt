package com.tcron.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcron.core.common.ThemeManager
import com.tcron.core.common.ThemeMode
import com.tcron.core.common.NotificationHelper
import com.tcron.core.common.BiometricHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val themeManager: ThemeManager = hiltViewModel()
    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }
    val biometricHelper = remember { BiometricHelper() }
    val currentTheme by themeManager.currentTheme.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    
    var showLicensesDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showRestartDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ThemeSettingsCard(
                    themeManager = themeManager, 
                    currentTheme = currentTheme,
                    onThemeChanged = { showRestartDialog = true }
                )
            }
            
            item {
                AppSettingsCard(uiState, viewModel, notificationHelper)
            }
            
            item {
                SystemSettingsCard(uiState, viewModel)
            }
            
            item {
                SecuritySettingsCard(
                    uiState = uiState, 
                    viewModel = viewModel, 
                    biometricHelper = biometricHelper, 
                    context = context
                )
            }
            
            item {
                AboutCard(
                    showLicensesDialog = showLicensesDialog,
                    onShowLicensesDialog = { showLicensesDialog = it },
                    showPrivacyDialog = showPrivacyDialog,
                    onShowPrivacyDialog = { showPrivacyDialog = it }
                )
            }
        }
    }
    
    // Diálogo de reinicialização após mudança de tema
    if (showRestartDialog) {
        AlertDialog(
            onDismissRequest = { showRestartDialog = false },
            title = { Text("Reiniciar Aplicativo") },
            text = { 
                Text("Para aplicar o novo tema completamente, é recomendado reiniciar o aplicativo. Deseja reiniciar agora?") 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRestartDialog = false
                        // Reiniciar o aplicativo
                        restartApp(context)
                    }
                ) {
                    Text("Reiniciar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRestartDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSettingsCard(
    themeManager: ThemeManager, 
    currentTheme: ThemeMode,
    onThemeChanged: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Palette,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Aparência",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            var expanded by remember { mutableStateOf(false) }
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = when (currentTheme) {
                        ThemeMode.LIGHT -> "Claro"
                        ThemeMode.DARK -> "Escuro"
                        ThemeMode.SYSTEM -> "Padrão do sistema"
                    },
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Tema") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Claro") },
                        onClick = {
                            if (currentTheme != ThemeMode.LIGHT) {
                                themeManager.setThemeMode(ThemeMode.LIGHT)
                                onThemeChanged()
                            }
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Default.LightMode, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Escuro") },
                        onClick = {
                            if (currentTheme != ThemeMode.DARK) {
                                themeManager.setThemeMode(ThemeMode.DARK)
                                onThemeChanged()
                            }
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Default.DarkMode, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Padrão do sistema") },
                        onClick = {
                            if (currentTheme != ThemeMode.SYSTEM) {
                                themeManager.setThemeMode(ThemeMode.SYSTEM)
                                onThemeChanged()
                            }
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Default.SettingsBrightness, contentDescription = null)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppSettingsCard(
    uiState: SettingsUiState,
    viewModel: SettingsViewModel,
    notificationHelper: NotificationHelper
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Aplicativo",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            SettingItem(
                title = "Iniciar no boot",
                subtitle = "Iniciar o aplicativo automaticamente após o boot do sistema",
                icon = Icons.Default.PlayArrow,
                switch = {
                    Switch(
                        checked = uiState.startOnBoot,
                        onCheckedChange = { viewModel.setStartOnBoot(it) }
                    )
                }
            )
            
            SettingItem(
                title = "Auto-executar tarefas",
                subtitle = "Executar tarefas programadas automaticamente",
                icon = Icons.Default.Schedule,
                switch = {
                    Switch(
                        checked = uiState.autoStartTasks,
                        onCheckedChange = { viewModel.setAutoStartTasks(it) }
                    )
                }
            )
            
            SettingItem(
                title = "Notificações",
                subtitle = "Receber notificações sobre execução de tarefas",
                icon = Icons.Default.Notifications,
                switch = {
                    Switch(
                        checked = uiState.enableNotifications,
                        onCheckedChange = { viewModel.setEnableNotifications(it) }
                    )
                }
            )
            
            // Expanded notification settings when notifications are enabled
            if (uiState.enableNotifications) {
                Spacer(modifier = Modifier.height(8.dp))
                NotificationSettingsCard(uiState, viewModel, notificationHelper)
            }
        }
    }
}

@Composable
private fun SystemSettingsCard(
    uiState: SettingsUiState,
    viewModel: SettingsViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Computer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Sistema",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            SettingItem(
                title = "Status Root",
                subtitle = uiState.rootStatus,
                icon = Icons.Default.Security,
                action = {
                    OutlinedButton(
                        onClick = { viewModel.checkRootAccess() },
                        enabled = !uiState.isCheckingRoot
                    ) {
                        if (uiState.isCheckingRoot) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Verificar")
                        }
                    }
                }
            )
            
            SettingItem(
                title = "Usar permissões elevadas",
                subtitle = "Executar comandos com privilégios root quando disponível",
                icon = Icons.Default.AdminPanelSettings,
                switch = {
                    Switch(
                        checked = uiState.useRootPermissions,
                        onCheckedChange = { viewModel.setUseRootPermissions(it) }
                    )
                }
            )
        }
    }
}

@Composable
private fun SecuritySettingsCard(
    uiState: SettingsUiState,
    viewModel: SettingsViewModel,
    biometricHelper: BiometricHelper,
    context: android.content.Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Segurança",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            SettingItem(
                title = "Autenticação biométrica",
                subtitle = if (biometricHelper.isBiometricAvailable(context)) {
                    "Exigir autenticação para executar scripts sensíveis - ${biometricHelper.getBiometricStatus(context)}"
                } else {
                    "Biometria não disponível - ${biometricHelper.getBiometricStatus(context)}"
                },
                icon = Icons.Default.Fingerprint,
                switch = {
                    Switch(
                        checked = uiState.requireBiometricAuth,
                        enabled = biometricHelper.isBiometricAvailable(context),
                        onCheckedChange = { enabled ->
                            if (enabled && context is FragmentActivity) {
                                biometricHelper.authenticate(
                                    activity = context,
                                    title = "Ativar Autenticação Biométrica",
                                    subtitle = "Confirme sua identidade para ativar a autenticação biométrica",
                                    onSuccess = {
                                        viewModel.setRequireBiometricAuth(true)
                                    },
                                    onError = { error ->
                                        // Error handled, keep current state
                                    },
                                    onFailed = {
                                        // Authentication failed, keep current state
                                    }
                                )
                            } else {
                                viewModel.setRequireBiometricAuth(enabled)
                            }
                        }
                    )
                }
            )
            
            SettingItem(
                title = "Criptografar scripts",
                subtitle = "Armazenar scripts de forma criptografada",
                icon = Icons.Default.Lock,
                switch = {
                    Switch(
                        checked = uiState.encryptScripts,
                        onCheckedChange = { viewModel.setEncryptScripts(it) }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationSettingsCard(
    uiState: SettingsUiState,
    viewModel: SettingsViewModel,
    notificationHelper: NotificationHelper
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Configurações Avançadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            var showChannelDropdown by remember { mutableStateOf(false) }
            
            // Notification Channels Section
            Text(
                text = "Canal de Notificação",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            ExposedDropdownMenuBox(
                expanded = showChannelDropdown,
                onExpandedChange = { showChannelDropdown = !showChannelDropdown }
            ) {
                OutlinedTextField(
                    value = uiState.selectedNotificationChannel,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Tipo de Canal") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showChannelDropdown)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                
                ExposedDropdownMenu(
                    expanded = showChannelDropdown,
                    onDismissRequest = { showChannelDropdown = false }
                ) {
                    listOf(
                        "Execução de Tarefas",
                        "Alertas do Sistema", 
                        "Atualizações",
                        "Depuração"
                    ).forEach { channel ->
                        DropdownMenuItem(
                            text = { Text(channel) },
                            onClick = {
                                viewModel.setSelectedNotificationChannel(channel)
                                showChannelDropdown = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Notification Types Section
            Text(
                text = "Tipos de Notificação",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            SettingItem(
                title = "Tarefa concluída",
                subtitle = "Notificar quando uma tarefa for executada com sucesso",
                icon = Icons.Default.CheckCircle,
                switch = {
                    Switch(
                        checked = uiState.taskCompletionNotifs,
                        onCheckedChange = { viewModel.setTaskCompletionNotifs(it) }
                    )
                }
            )
            
            SettingItem(
                title = "Falha na tarefa",
                subtitle = "Notificar quando uma tarefa falhar na execução",
                icon = Icons.Default.Error,
                switch = {
                    Switch(
                        checked = uiState.taskFailureNotifs,
                        onCheckedChange = { viewModel.setTaskFailureNotifs(it) }
                    )
                }
            )
            
            SettingItem(
                title = "Alertas do sistema",
                subtitle = "Notificar sobre problemas de sistema e recursos",
                icon = Icons.Default.Warning,
                switch = {
                    Switch(
                        checked = uiState.systemAlertsNotifs,
                        onCheckedChange = { viewModel.setSystemAlertsNotifs(it) }
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Notification Preferences Section
            Text(
                text = "Preferências",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            SettingItem(
                title = "Som",
                subtitle = "Reproduzir som ao receber notificações",
                icon = Icons.Default.VolumeUp,
                switch = {
                    Switch(
                        checked = uiState.notificationSound,
                        onCheckedChange = { viewModel.setNotificationSound(it) }
                    )
                }
            )
            
            SettingItem(
                title = "Vibração",
                subtitle = "Vibrar dispositivo ao receber notificações",
                icon = Icons.Default.Vibration,
                switch = {
                    Switch(
                        checked = uiState.notificationVibration,
                        onCheckedChange = { viewModel.setNotificationVibration(it) }
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Test Notification Button
            OutlinedButton(
                onClick = { 
                    notificationHelper.sendTestNotification()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Testar Notificação")
            }
        }
    }
}

@Composable
private fun AboutCard(
    showLicensesDialog: Boolean,
    onShowLicensesDialog: (Boolean) -> Unit,
    showPrivacyDialog: Boolean,
    onShowPrivacyDialog: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Sobre",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            SettingItem(
                title = "Versão",
                subtitle = "TCron v1.0.0",
                icon = Icons.Default.AppShortcut
            )
            
            SettingItem(
                title = "Licenças",
                subtitle = "Ver licenças de código aberto",
                icon = Icons.Default.Article,
                onClick = { 
                    onShowLicensesDialog(true)
                }
            )
            
            SettingItem(
                title = "Política de privacidade",
                subtitle = "Como protegemos seus dados",
                icon = Icons.Default.PrivacyTip,
                onClick = { 
                    onShowPrivacyDialog(true)
                }
            )
        }
    }
    
    // Licenses Dialog
    if (showLicensesDialog) {
        AlertDialog(
            onDismissRequest = { onShowLicensesDialog(false) },
            title = { Text("Licenças de Código Aberto") },
            text = {
                Text(
                    text = """
                        TCron utiliza as seguintes bibliotecas de código aberto:
                        
                        • Jetpack Compose - Apache License 2.0
                        • Kotlin - Apache License 2.0
                        • Hilt - Apache License 2.0
                        • Material Design Components - Apache License 2.0
                        • AndroidX Libraries - Apache License 2.0
                        • Gson - Apache License 2.0
                        
                        Agradecemos a todos os desenvolvedores que contribuem para o ecossistema de código aberto.
                    """.trimIndent()
                )
            },
            confirmButton = {
                TextButton(onClick = { onShowLicensesDialog(false) }) {
                    Text("Fechar")
                }
            }
        )
    }
    
    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { onShowPrivacyDialog(false) },
            title = { Text("Política de Privacidade") },
            text = {
                Text(
                    text = """
                        Política de Privacidade do TCron
                        
                        O TCron respeita sua privacidade e se compromete a proteger seus dados pessoais.
                        
                        Coleta de Dados:
                        • O aplicativo armazena localmente os scripts e agendamentos que você criar
                        • Configurações do aplicativo são salvas no dispositivo
                        • Nenhum dado é enviado para servidores externos
                        
                        Uso dos Dados:
                        • Todos os dados permanecem exclusivamente no seu dispositivo
                        • Scripts e configurações são usados apenas para funcionalidade do app
                        
                        Segurança:
                        • Dados podem ser criptografados localmente se habilitado
                        • Autenticação biométrica opcional para maior segurança
                        
                        O TCron é um aplicativo offline que não coleta nem compartilha dados pessoais.
                    """.trimIndent()
                )
            },
            confirmButton = {
                TextButton(onClick = { onShowPrivacyDialog(false) }) {
                    Text("Fechar")
                }
            }
        )
    }
}

@Composable
private fun SettingItem(
    title: String,
    subtitle: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: (() -> Unit)? = null,
    switch: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else Modifier
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        when {
            switch != null -> switch()
            action != null -> action()
            onClick != null -> {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Função para reiniciar o app
private fun restartApp(context: android.content.Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.let {
        it.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP)
        it.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(it)
        if (context is android.app.Activity) {
            context.finish()
        }
        kotlin.system.exitProcess(0)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}