package com.tcron.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Plugin(
    val id: String,
    val name: String,
    val description: String,
    val version: String,
    val author: String,
    val isEnabled: Boolean,
    val isInstalled: Boolean,
    val category: PluginCategory
)

enum class PluginCategory {
    AUTOMATION, MONITORING, SECURITY, SYSTEM, UTILITIES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PluginsScreen(
    onNavigateBack: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf<PluginCategory?>(null) }
    
    val samplePlugins = remember {
        listOf(
            Plugin(
                "backup-manager", "Backup Manager", 
                "Plugin para gerenciamento automático de backups", 
                "1.2.3", "TCron Team", true, true, PluginCategory.UTILITIES
            ),
            Plugin(
                "system-monitor", "System Monitor", 
                "Monitoramento avançado de recursos do sistema", 
                "2.0.1", "DevTeam", true, true, PluginCategory.MONITORING
            ),
            Plugin(
                "security-scanner", "Security Scanner", 
                "Verificação de segurança e vulnerabilidades", 
                "1.5.0", "SecTeam", false, true, PluginCategory.SECURITY
            ),
            Plugin(
                "task-scheduler", "Advanced Scheduler", 
                "Agendador avançado com múltiplas opções", 
                "3.1.2", "AutoTeam", true, false, PluginCategory.AUTOMATION
            ),
            Plugin(
                "file-cleaner", "File Cleaner", 
                "Limpeza automática de arquivos temporários", 
                "1.0.5", "Utils Inc", false, false, PluginCategory.SYSTEM
            )
        )
    }
    
    val filteredPlugins = if (selectedCategory != null) {
        samplePlugins.filter { it.category == selectedCategory }
    } else {
        samplePlugins
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plugins / Extensões") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    var showMenu by remember { mutableStateOf(false) }
                    
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas categorias") },
                            onClick = { 
                                selectedCategory = null
                                showMenu = false 
                            }
                        )
                        PluginCategory.values().forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                onClick = { 
                                    selectedCategory = category
                                    showMenu = false 
                                }
                            )
                        }
                    }
                    
                    IconButton(onClick = { /* TODO: Browse plugin store */ }) {
                        Icon(Icons.Default.Store, contentDescription = "Loja")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Install plugin from file */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Instalar plugin")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredPlugins) { plugin ->
                PluginCard(plugin)
            }
        }
    }
}

@Composable
private fun PluginCard(plugin: Plugin) {
    val statusColor = when {
        !plugin.isInstalled -> Color(0xFF9E9E9E)
        plugin.isEnabled -> Color(0xFF4CAF50)
        else -> Color(0xFFFF9800)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plugin.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = "v${plugin.version} • ${plugin.author}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = plugin.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = statusColor.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = when {
                            !plugin.isInstalled -> "Não instalado"
                            plugin.isEnabled -> "Ativo"
                            else -> "Inativo"
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!plugin.isInstalled) {
                    Button(
                        onClick = { /* TODO: Install plugin */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.GetApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Instalar")
                    }
                } else {
                    if (plugin.isEnabled) {
                        OutlinedButton(
                            onClick = { /* TODO: Disable plugin */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Desativar")
                        }
                    } else {
                        Button(
                            onClick = { /* TODO: Enable plugin */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ativar")
                        }
                    }
                    
                    OutlinedButton(
                        onClick = { /* TODO: Configure plugin */ }
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Configurar")
                    }
                    
                    OutlinedButton(
                        onClick = { /* TODO: Uninstall plugin */ }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Desinstalar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PluginsScreenPreview() {
    MaterialTheme {
        PluginsScreen()
    }
}