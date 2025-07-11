package com.tcron.feature.settings

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

data class LogEntry(
    val timestamp: Long,
    val level: LogLevel,
    val tag: String,
    val message: String
)

enum class LogLevel {
    DEBUG, INFO, WARNING, ERROR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    onNavigateBack: () -> Unit = {}
) {
    var selectedLevel by remember { mutableStateOf<LogLevel?>(null) }
    
    val sampleLogs = remember {
        listOf(
            LogEntry(System.currentTimeMillis() - 60000, LogLevel.INFO, "TCron", "Sistema iniciado com sucesso"),
            LogEntry(System.currentTimeMillis() - 120000, LogLevel.DEBUG, "TaskManager", "Carregando tarefas agendadas"),
            LogEntry(System.currentTimeMillis() - 180000, LogLevel.WARNING, "ScriptEngine", "Script Python demorou mais que o esperado"),
            LogEntry(System.currentTimeMillis() - 240000, LogLevel.ERROR, "NetworkManager", "Falha na conexão com o servidor"),
            LogEntry(System.currentTimeMillis() - 300000, LogLevel.INFO, "BackupTask", "Backup realizado com sucesso"),
            LogEntry(System.currentTimeMillis() - 360000, LogLevel.DEBUG, "SystemMonitor", "CPU: 25%, RAM: 580MB"),
            LogEntry(System.currentTimeMillis() - 420000, LogLevel.WARNING, "PermissionManager", "Permissão de root não concedida"),
            LogEntry(System.currentTimeMillis() - 480000, LogLevel.INFO, "TCron", "Aplicativo em segundo plano"),
        )
    }
    
    val filteredLogs = if (selectedLevel != null) {
        sampleLogs.filter { it.level == selectedLevel }
    } else {
        sampleLogs
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Logs do Sistema") },
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
                            text = { Text("Todos") },
                            onClick = { 
                                selectedLevel = null
                                showMenu = false 
                            }
                        )
                        LogLevel.values().forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level.name) },
                                onClick = { 
                                    selectedLevel = level
                                    showMenu = false 
                                }
                            )
                        }
                    }
                    
                    IconButton(onClick = { /* TODO: Export logs */ }) {
                        Icon(Icons.Default.GetApp, contentDescription = "Exportar")
                    }
                    
                    IconButton(onClick = { /* TODO: Clear logs */ }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpar")
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredLogs) { log ->
                LogEntryCard(log)
            }
        }
    }
}

@Composable
private fun LogEntryCard(log: LogEntry) {
    val levelColor = when (log.level) {
        LogLevel.DEBUG -> Color(0xFF9E9E9E)
        LogLevel.INFO -> Color(0xFF2196F3)
        LogLevel.WARNING -> Color(0xFFFF9800)
        LogLevel.ERROR -> Color(0xFFE57373)
    }
    
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(levelColor, RoundedCornerShape(4.dp))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeFormat.format(Date(log.timestamp)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = FontFamily.Monospace
                    )
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = levelColor.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = log.level.name,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = levelColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = log.tag,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = log.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LogsScreenPreview() {
    MaterialTheme {
        LogsScreen()
    }
}