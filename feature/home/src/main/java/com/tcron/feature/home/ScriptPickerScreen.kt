package com.tcron.feature.home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class ScriptFile(
    val name: String,
    val path: String,
    val type: ScriptType,
    val size: String,
    val lastModified: String
)

enum class ScriptType {
    SHELL, PYTHON, JAVASCRIPT, OTHER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptPickerScreen(
    onNavigateBack: () -> Unit = {},
    onScriptSelected: (ScriptFile) -> Unit = {}
) {
    val sampleScripts = remember {
        emptyList<ScriptFile>() // No sample data - will be replaced with real data
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selecionar Script") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Open file browser */ }) {
                        Icon(Icons.Default.FolderOpen, contentDescription = "Navegar arquivos")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Open file browser */ }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar script")
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
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Selecione um script para carregar ou use o botão + para navegar pelos arquivos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            if (sampleScripts.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Code,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Nenhum script encontrado",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Use o botão + para navegar pelos arquivos ou criar novos scripts",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                items(sampleScripts) { script ->
                    ScriptItem(
                        script = script,
                        onScriptClick = { onScriptSelected(script) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScriptItem(
    script: ScriptFile,
    onScriptClick: () -> Unit
) {
    val scriptIcon = when (script.type) {
        ScriptType.SHELL -> Icons.Default.Terminal
        ScriptType.PYTHON -> Icons.Default.Code
        ScriptType.JAVASCRIPT -> Icons.Default.Javascript
        ScriptType.OTHER -> Icons.Default.InsertDriveFile
    }
    
    val scriptColor = when (script.type) {
        ScriptType.SHELL -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
        ScriptType.PYTHON -> androidx.compose.ui.graphics.Color(0xFF3776AB)
        ScriptType.JAVASCRIPT -> androidx.compose.ui.graphics.Color(0xFFF7DF1E)
        ScriptType.OTHER -> androidx.compose.ui.graphics.Color(0xFF9E9E9E)
    }
    
    Card(
        onClick = onScriptClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                scriptIcon,
                contentDescription = null,
                tint = scriptColor,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = script.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = script.path,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = script.size,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = script.lastModified,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScriptPickerScreenPreview() {
    MaterialTheme {
        ScriptPickerScreen()
    }
}