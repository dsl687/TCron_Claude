package com.tcron.feature.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcron.core.data.repository.Schedule
import com.tcron.core.data.repository.ScheduleRepository
import java.util.UUID

data class TaskSchedule(
    val type: ScheduleType,
    val minute: String = "*",
    val hour: String = "*",
    val dayOfMonth: String = "*",
    val month: String = "*",
    val dayOfWeek: String = "*"
)

enum class ScheduleType {
    HOURLY, DAILY, WEEKLY, MONTHLY, CUSTOM
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onNavigateBack: () -> Unit = {},
    onTaskCreated: () -> Unit = {}
) {
    val context = LocalContext.current
    val scheduleRepository = remember {
        ScheduleRepository(context)
    }
    var taskName by remember { mutableStateOf("") }
    var taskCommand by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedScheduleType by remember { mutableStateOf(ScheduleType.DAILY) }
    var isEnabled by remember { mutableStateOf(true) }
    var selectedHour by remember { mutableStateOf("00") }
    var selectedMinute by remember { mutableStateOf("00") }
    var customCron by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Agendamento") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            try {
                                // Convert schedule to cron expression
                                val cronExpression = when (selectedScheduleType) {
                                    ScheduleType.HOURLY -> "$selectedMinute * * * *"
                                    ScheduleType.DAILY -> "$selectedMinute $selectedHour * * *"
                                    ScheduleType.WEEKLY -> "$selectedMinute $selectedHour * * 1"
                                    ScheduleType.MONTHLY -> "$selectedMinute $selectedHour 1 * *"
                                    ScheduleType.CUSTOM -> customCron
                                }
                                
                                // Save the schedule with error handling
                                val schedule = Schedule(
                                    id = UUID.randomUUID().toString(),
                                    name = taskName,
                                    description = taskDescription,
                                    command = taskCommand,
                                    cronExpression = cronExpression,
                                    isEnabled = isEnabled
                                )
                                scheduleRepository.saveSchedule(schedule)
                                onTaskCreated()
                            } catch (e: Exception) {
                                android.util.Log.e("CreateTask", "Error saving task: ${e.message}", e)
                                // TODO: Show error dialog or toast to user
                            }
                        },
                        enabled = taskName.isNotEmpty() && taskCommand.isNotEmpty() && 
                                 (selectedScheduleType != ScheduleType.CUSTOM || customCron.isNotEmpty())
                    ) {
                        Text("SALVAR")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Task Basic Info
            TaskInfoCard(
                taskName = taskName,
                onTaskNameChange = { taskName = it },
                taskCommand = taskCommand,
                onTaskCommandChange = { taskCommand = it },
                taskDescription = taskDescription,
                onTaskDescriptionChange = { taskDescription = it },
                isEnabled = isEnabled,
                onEnabledChange = { isEnabled = it }
            )
            
            // Schedule Configuration
            ScheduleCard(
                selectedScheduleType = selectedScheduleType,
                onScheduleTypeChange = { selectedScheduleType = it },
                selectedHour = selectedHour,
                onHourChange = { selectedHour = it },
                selectedMinute = selectedMinute,
                onMinuteChange = { selectedMinute = it },
                customCron = customCron,
                onCustomCronChange = { customCron = it }
            )
            
            // Preview Card
            PreviewCard(
                taskName = taskName,
                selectedScheduleType = selectedScheduleType,
                selectedHour = selectedHour,
                selectedMinute = selectedMinute,
                customCron = customCron
            )
        }
    }
}

@Composable
private fun TaskInfoCard(
    taskName: String,
    onTaskNameChange: (String) -> Unit,
    taskCommand: String,
    onTaskCommandChange: (String) -> Unit,
    taskDescription: String,
    onTaskDescriptionChange: (String) -> Unit,
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Task,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Informações da Tarefa",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            OutlinedTextField(
                value = taskName,
                onValueChange = onTaskNameChange,
                label = { Text("Nome da tarefa") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Label, contentDescription = null)
                }
            )
            
            OutlinedTextField(
                value = taskCommand,
                onValueChange = onTaskCommandChange,
                label = { Text("Comando") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Terminal, contentDescription = null)
                },
                placeholder = { Text("Ex: /system/bin/ls -la") }
            )
            
            OutlinedTextField(
                value = taskDescription,
                onValueChange = onTaskDescriptionChange,
                label = { Text("Descrição (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Description, contentDescription = null)
                },
                maxLines = 3
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tarefa ativa")
                Switch(
                    checked = isEnabled,
                    onCheckedChange = onEnabledChange
                )
            }
        }
    }
}

@Composable
private fun ScheduleCard(
    selectedScheduleType: ScheduleType,
    onScheduleTypeChange: (ScheduleType) -> Unit,
    selectedHour: String,
    onHourChange: (String) -> Unit,
    selectedMinute: String,
    onMinuteChange: (String) -> Unit,
    customCron: String,
    onCustomCronChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Agendamento",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Text(
                text = "Frequência",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            Column {
                ScheduleType.values().forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedScheduleType == type,
                                onClick = { onScheduleTypeChange(type) }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedScheduleType == type,
                            onClick = { onScheduleTypeChange(type) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (type) {
                                ScheduleType.HOURLY -> "A cada hora"
                                ScheduleType.DAILY -> "Diariamente"
                                ScheduleType.WEEKLY -> "Semanalmente"
                                ScheduleType.MONTHLY -> "Mensalmente"
                                ScheduleType.CUSTOM -> "Personalizado (cron)"
                            }
                        )
                    }
                }
            }
            
            if (selectedScheduleType == ScheduleType.DAILY || selectedScheduleType == ScheduleType.WEEKLY) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = selectedHour,
                        onValueChange = onHourChange,
                        label = { Text("Hora") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("00-23") }
                    )
                    
                    OutlinedTextField(
                        value = selectedMinute,
                        onValueChange = onMinuteChange,
                        label = { Text("Minuto") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("00-59") }
                    )
                }
            }
            
            if (selectedScheduleType == ScheduleType.CUSTOM) {
                OutlinedTextField(
                    value = customCron,
                    onValueChange = onCustomCronChange,
                    label = { Text("Expressão Cron") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("* * * * *") },
                    supportingText = { Text("Formato: minuto hora dia mês dia_da_semana") }
                )
            }
        }
    }
}

@Composable
private fun PreviewCard(
    taskName: String,
    selectedScheduleType: ScheduleType,
    selectedHour: String,
    selectedMinute: String,
    customCron: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Preview,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Prévia",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = if (taskName.isNotEmpty()) taskName else "Nome da tarefa",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Text(
                text = buildScheduleDescription(selectedScheduleType, selectedHour, selectedMinute, customCron),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

private fun buildScheduleDescription(
    type: ScheduleType,
    hour: String,
    minute: String,
    customCron: String
): String {
    return when (type) {
        ScheduleType.HOURLY -> "Executar a cada hora"
        ScheduleType.DAILY -> "Executar diariamente às ${hour.padStart(2, '0')}:${minute.padStart(2, '0')}"
        ScheduleType.WEEKLY -> "Executar semanalmente às ${hour.padStart(2, '0')}:${minute.padStart(2, '0')}"
        ScheduleType.MONTHLY -> "Executar mensalmente"
        ScheduleType.CUSTOM -> if (customCron.isNotEmpty()) "Cron: $customCron" else "Configurar expressão cron"
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateTaskScreenPreview() {
    MaterialTheme {
        CreateTaskScreen()
    }
}