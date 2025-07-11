package com.tcron.feature.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcron.core.common.SystemMetrics
import com.tcron.core.domain.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTerminal: () -> Unit = {},
    onNavigateToCreateTask: () -> Unit = {},
    onNavigateToTaskDetail: (String) -> Unit = {},
    onOpenDrawer: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFabMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TCronTopBar(
                onOpenDrawer = onOpenDrawer
            )
        },
        floatingActionButton = {
            TCronFAB(
                expanded = showFabMenu,
                onToggle = { showFabMenu = !showFabMenu },
                onCreateTask = {
                    showFabMenu = false
                    onNavigateToCreateTask()
                },
                onCreateScript = {
                    showFabMenu = false
                    // Navigate to create script
                },
                onOpenTerminal = {
                    showFabMenu = false
                    onNavigateToTerminal()
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                SystemSummaryCard(
                    systemMetrics = uiState.systemMetrics,
                    onRefresh = { viewModel.refreshSystemMetrics() }
                )
            }
            
            item {
                ScheduledTasksCard(
                    tasks = uiState.tasks,
                    onTaskClick = onNavigateToTaskDetail
                )
            }
            
            item {
                ScriptsCard()
            }
            
            item {
                ExecutionHistoryCard()
            }
            
            item {
                WelcomeMessage()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TCronTopBar(
    onOpenDrawer: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "TCron",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            var showMenu by remember { mutableStateOf(false) }
            
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Atualizar lista") },
                        onClick = { 
                            showMenu = false
                            // TODO: Refresh data
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Exportar agendamentos") },
                        onClick = { 
                            showMenu = false
                            // TODO: Export functionality
                        },
                        leadingIcon = {
                            Icon(Icons.Default.GetApp, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Limpar histórico") },
                        onClick = { 
                            showMenu = false
                            // TODO: Clear history
                        },
                        leadingIcon = {
                            Icon(Icons.Default.CleaningServices, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Filtrar por tipo") },
                        onClick = { 
                            showMenu = false
                            // TODO: Filter functionality
                        },
                        leadingIcon = {
                            Icon(Icons.Default.FilterList, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Ordenar por...") },
                        onClick = { 
                            showMenu = false
                            // TODO: Sort functionality
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Sort, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Modo de teste") },
                        onClick = { 
                            showMenu = false
                            // TODO: Test mode
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Science, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Ver termos de uso") },
                        onClick = { 
                            showMenu = false
                            // TODO: Terms of use
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Description, contentDescription = null)
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun SystemSummaryCard(
    systemMetrics: SystemMetrics? = null,
    onRefresh: () -> Unit = {}
) {
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Assessment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Resumo do Sistema",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                IconButton(onClick = onRefresh) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Atualizar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            SystemMetricRow(
                icon = Icons.Default.CheckCircle,
                label = "Tarefas executadas",
                value = systemMetrics?.tasksExecuted?.toString() ?: "0",
                iconColor = Color(0xFF4CAF50)
            )
            
            SystemMetricRow(
                icon = Icons.Default.Warning,
                label = "Falhas",
                value = systemMetrics?.tasksFailed?.toString() ?: "0",
                iconColor = Color(0xFFE57373)
            )
            
            SystemMetricRow(
                icon = Icons.Default.Timer,
                label = "Tempo médio",
                value = systemMetrics?.averageExecutionTime ?: "0s",
                iconColor = Color(0xFF64B5F6)
            )
            
            SystemMetricRow(
                icon = Icons.Default.Memory,
                label = "Uso de CPU",
                value = systemMetrics?.let { "${it.cpuUsage.toInt()}%" } ?: "0%",
                iconColor = Color(0xFFFFB74D)
            )
            
            SystemMetricRow(
                icon = Icons.Default.Storage,
                label = "Uso de RAM",
                value = systemMetrics?.let { "${it.ramUsage}MB" } ?: "0MB",
                iconColor = Color(0xFF81C784)
            )
            
            SystemMetricRow(
                icon = Icons.Default.BatteryFull,
                label = "Bateria",
                value = systemMetrics?.let { "${it.batteryLevel}%" } ?: "0%",
                iconColor = Color(0xFF4DD0E1),
                isLast = true
            )
        }
    }
}

@Composable
private fun SystemMetricRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ScheduledTasksCard(
    tasks: List<Task>,
    onTaskClick: (String) -> Unit
) {
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tarefas Agendadas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            TaskItem(
                name = "Backup automático",
                lastRun = "Última: 08/07/2025",
                status = "Ativo",
                statusColor = Color(0xFF4CAF50),
                timeLeft = "53:20",
                onClick = { /* Navigate to task detail */ }
            )
            
            TaskItem(
                name = "Limpeza Temp",
                lastRun = "Última: 09/07/2025",
                status = "Agendado",
                statusColor = Color(0xFFFFB74D),
                timeLeft = "02:05",
                onClick = { /* Navigate to task detail */ },
                isLast = true
            )
        }
    }
}

@Composable
private fun TaskItem(
    name: String,
    lastRun: String,
    status: String,
    statusColor: Color,
    timeLeft: String,
    onClick: () -> Unit,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(statusColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = lastRun,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = timeLeft,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ScriptsCard() {
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Code,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Scripts",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            ScriptItem(
                name = "Script Python - Monitor",
                lastRun = "Última: 09/07/2025",
                status = "Ativo",
                statusColor = Color(0xFF4CAF50),
                runtime = "08:00",
                icon = Icons.Default.Code
            )
            
            ScriptItem(
                name = "Shell - Reset de Cache",
                lastRun = "Última: 09/07/2025",
                status = "Inativo",
                statusColor = Color(0xFF9E9E9E),
                runtime = "0s",
                icon = Icons.Default.Terminal,
                isLast = true
            )
        }
    }
}

@Composable
private fun ScriptItem(
    name: String,
    lastRun: String,
    status: String,
    statusColor: Color,
    runtime: String,
    icon: ImageVector,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to script detail */ }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = lastRun,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = runtime,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ExecutionHistoryCard() {
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Histórico de Execuções",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            HistoryItem(
                name = "Backup diário",
                executedAt = "Executado em 09/07/2025",
                status = "Sucesso",
                statusColor = Color(0xFF4CAF50),
                duration = "43s",
                icon = Icons.Default.CheckCircle
            )
            
            HistoryItem(
                name = "Limpeza Temp",
                executedAt = "Executado em 09/07/2025",
                status = "Falha",
                statusColor = Color(0xFFE57373),
                duration = "0s",
                icon = Icons.Default.Error,
                isLast = true
            )
        }
    }
}

@Composable
private fun HistoryItem(
    name: String,
    executedAt: String,
    status: String,
    statusColor: Color,
    duration: String,
    icon: ImageVector,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to execution detail */ }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = executedAt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = duration,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun WelcomeMessage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "🤖 Bem-vindo ao TCron!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TCronFAB(
    expanded: Boolean,
    onToggle: () -> Unit,
    onCreateTask: () -> Unit,
    onCreateScript: () -> Unit,
    onOpenTerminal: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        animationSpec = tween(300),
        label = "rotation"
    )
    
    Column(
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300)
            ) + fadeIn(tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300)
            ) + fadeOut(tween(300))
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                FABAction(
                    icon = Icons.Default.Terminal,
                    label = "Abrir terminal",
                    onClick = onOpenTerminal,
                    delay = 0
                )
                
                FABAction(
                    icon = Icons.Default.FileUpload,
                    label = "Carregar script",
                    onClick = { /* TODO: File picker */ },
                    delay = 50
                )
                
                FABAction(
                    icon = Icons.Default.Schedule,
                    label = "Criar agendamento",
                    onClick = onCreateTask,
                    delay = 100
                )
                
                FABAction(
                    icon = Icons.Default.IntegrationInstructions,
                    label = "Criar script Python",
                    onClick = { /* TODO: Python script creator */ },
                    delay = 150
                )
                
                FABAction(
                    icon = Icons.Default.Code,
                    label = "Criar script Shell",
                    onClick = { /* TODO: Shell script creator */ },
                    delay = 200
                )
            }
        }
        
        FloatingActionButton(
            onClick = onToggle,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = if (expanded) "Fechar menu" else "Abrir menu",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
private fun FABAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    delay: Int = 0
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        visible = true
    }
    
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.scale(scale)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 12.dp
            )
        ) {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}