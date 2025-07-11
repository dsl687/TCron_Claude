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
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcron.core.common.ThemeManager
import com.tcron.core.common.ThemeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
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
                ThemeSettingsCard()
            }
            
            item {
                AppSettingsCard()
            }
            
            item {
                SystemSettingsCard()
            }
            
            item {
                SecuritySettingsCard()
            }
            
            item {
                AboutCard()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSettingsCard() {
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
            val currentTheme = ThemeMode.DARK // TODO: Get from actual theme manager
            
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
                            // TODO: Set theme to light
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Default.LightMode, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Escuro") },
                        onClick = {
                            // TODO: Set theme to dark
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Default.DarkMode, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Padrão do sistema") },
                        onClick = {
                            // TODO: Set theme to system
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
private fun AppSettingsCard() {
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
            
            var startOnBoot by remember { mutableStateOf(false) }
            var autoStartTasks by remember { mutableStateOf(true) }
            var enableNotifications by remember { mutableStateOf(true) }
            
            SettingItem(
                title = "Iniciar no boot",
                subtitle = "Iniciar o aplicativo automaticamente após o boot do sistema",
                icon = Icons.Default.PlayArrow,
                switch = {
                    Switch(
                        checked = startOnBoot,
                        onCheckedChange = { startOnBoot = it }
                    )
                }
            )
            
            SettingItem(
                title = "Auto-executar tarefas",
                subtitle = "Executar tarefas programadas automaticamente",
                icon = Icons.Default.Schedule,
                switch = {
                    Switch(
                        checked = autoStartTasks,
                        onCheckedChange = { autoStartTasks = it }
                    )
                }
            )
            
            SettingItem(
                title = "Notificações",
                subtitle = "Receber notificações sobre execução de tarefas",
                icon = Icons.Default.Notifications,
                switch = {
                    Switch(
                        checked = enableNotifications,
                        onCheckedChange = { enableNotifications = it }
                    )
                }
            )
        }
    }
}

@Composable
private fun SystemSettingsCard() {
    var rootStatus by remember { mutableStateOf("Toque em verificar para testar") }
    var isCheckingRoot by remember { mutableStateOf(false) }
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
                subtitle = rootStatus,
                icon = Icons.Default.Security,
                action = {
                    OutlinedButton(
                        onClick = { 
                            isCheckingRoot = true
                            // Simulate root check
                            CoroutineScope(Dispatchers.IO).launch {
                                kotlinx.coroutines.delay(1000)
                                withContext(Dispatchers.Main) {
                                    rootStatus = "Sem acesso root"
                                    isCheckingRoot = false
                                }
                            }
                        },
                        enabled = !isCheckingRoot
                    ) {
                        if (isCheckingRoot) {
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
                    var useRoot by remember { mutableStateOf(false) }
                    Switch(
                        checked = useRoot,
                        onCheckedChange = { useRoot = it }
                    )
                }
            )
        }
    }
}

@Composable
private fun SecuritySettingsCard() {
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
            
            var requireAuth by remember { mutableStateOf(false) }
            var encryptScripts by remember { mutableStateOf(true) }
            
            SettingItem(
                title = "Autenticação biométrica",
                subtitle = "Exigir autenticação para executar scripts sensíveis",
                icon = Icons.Default.Fingerprint,
                switch = {
                    Switch(
                        checked = requireAuth,
                        onCheckedChange = { requireAuth = it }
                    )
                }
            )
            
            SettingItem(
                title = "Criptografar scripts",
                subtitle = "Armazenar scripts de forma criptografada",
                icon = Icons.Default.Lock,
                switch = {
                    Switch(
                        checked = encryptScripts,
                        onCheckedChange = { encryptScripts = it }
                    )
                }
            )
        }
    }
}

@Composable
private fun AboutCard() {
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
                onClick = { /* TODO: Show licenses */ }
            )
            
            SettingItem(
                title = "Política de privacidade",
                subtitle = "Como protegemos seus dados",
                icon = Icons.Default.PrivacyTip,
                onClick = { /* TODO: Show privacy policy */ }
            )
        }
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

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}