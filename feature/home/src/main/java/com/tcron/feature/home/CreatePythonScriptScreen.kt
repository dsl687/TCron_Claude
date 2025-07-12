package com.tcron.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcron.core.data.repository.Script
import com.tcron.core.data.repository.ScriptRepository
import com.tcron.core.data.repository.ScriptType
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePythonScriptScreen(
    onNavigateBack: () -> Unit = {},
    onScriptSaved: () -> Unit = {}
) {
    val context = LocalContext.current
    val scriptRepository = remember { ScriptRepository(context) }
    var scriptName by remember { mutableStateOf("") }
    var scriptDescription by remember { mutableStateOf("") }
    var scriptContent by remember { mutableStateOf(getPythonTemplate()) }
    var isExecuting by remember { mutableStateOf(false) }
    var executionOutput by remember { mutableStateOf("") }
    var showOutput by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Script Python") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            // Save the script
                            val script = Script(
                                id = UUID.randomUUID().toString(),
                                name = scriptName,
                                description = scriptDescription,
                                content = scriptContent,
                                type = ScriptType.PYTHON,
                                requiresRoot = false
                            )
                            scriptRepository.saveScript(script)
                            onScriptSaved()
                        },
                        enabled = scriptName.isNotBlank() && scriptContent.isNotBlank()
                    ) {
                        Text("Salvar")
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
            // Script Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Informações do Script",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    OutlinedTextField(
                        value = scriptName,
                        onValueChange = { scriptName = it },
                        label = { Text("Nome do Script") },
                        leadingIcon = { Icon(Icons.Default.Code, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = scriptDescription,
                        onValueChange = { scriptDescription = it },
                        label = { Text("Descrição (Opcional)") },
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 3
                    )
                }
            }
            
            // Script Editor Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Editor de Código",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { scriptContent = getPythonTemplate() }
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Resetar template",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            IconButton(
                                onClick = {
                                    isExecuting = true
                                    // TODO: Implement script testing
                                    showOutput = true
                                    executionOutput = "Teste do script em desenvolvimento..."
                                    isExecuting = false
                                }
                            ) {
                                if (isExecuting) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.PlayArrow,
                                        contentDescription = "Testar script",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                    
                    OutlinedTextField(
                        value = scriptContent,
                        onValueChange = { scriptContent = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace
                        ),
                        placeholder = { Text("Digite seu código Python aqui...") }
                    )
                }
            }
            
            // Python Examples Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "💡 Exemplos de Uso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    Text(
                        text = """
                        • Monitoramento de sistema
                        • Backup de arquivos
                        • Análise de logs
                        • Verificação de saúde de serviços
                        • Automação de tarefas administrativas
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Output Card - only shown when there's output
            if (showOutput) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Saída de Execução",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            IconButton(onClick = { showOutput = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Fechar")
                            }
                        }
                        
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Black,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = executionOutput,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Green
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getPythonTemplate(): String {
    return """#!/system/bin/env python3
# -*- coding: utf-8 -*-
\"\"\"
TCron Python Script Template
Descrição: Script de exemplo para automação de tarefas

Este template está otimizado para Android e inclui verificações
de compatibilidade para execução em ambiente móvel.
\"\"\"

import os
import sys
import datetime
import subprocess
import platform

def log_message(message):
    \"\"\"Log com timestamp\"\"\"
    timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print(f"[{timestamp}] {message}")

def check_android_environment():
    \"\"\"Verifica se está executando no Android\"\"\"
    try:
        return 'android' in platform.platform().lower() or os.path.exists('/system/build.prop')
    except:
        return False

def main():
    \"\"\"
    Função principal do script
    \"\"\"
    log_message("=== TCron Python Script ===")
    log_message("Iniciando execução...")
    
    # Verificar ambiente Android
    is_android = check_android_environment()
    log_message(f"Ambiente Android: {'Sim' if is_android else 'Não'}")
    
    # Seu código personalizado aqui
    try:
        # Exemplo: informações básicas do sistema
        log_message(f"Sistema: {platform.system()}")
        log_message(f"Arquitetura: {platform.machine()}")
        log_message(f"Usuário: {os.getenv('USER', os.getenv('USERNAME', 'unknown'))}")
        log_message(f"Diretório: {os.getcwd()}")
        
        # Exemplo específico para Android
        if is_android:
            log_message("Executando comandos Android...")
            # Comandos seguros para Android
            if os.path.exists('/proc/version'):
                with open('/proc/version', 'r') as f:
                    kernel_info = f.read().strip()
                    log_message(f"Kernel: {kernel_info[:50]}...")
        else:
            log_message("Executando comandos padrão...")
            # Comandos para sistemas Unix/Linux padrão
            try:
                result = subprocess.run(['whoami'], capture_output=True, text=True, timeout=5)
                if result.returncode == 0:
                    log_message(f"Usuário verificado: {result.stdout.strip()}")
            except:
                log_message("Comando whoami não disponível")
        
        log_message("Script executado com sucesso!")
        return 0
        
    except Exception as e:
        log_message(f"ERRO: {e}")
        return 1
    finally:
        log_message("=== Fim da execução ===")

if __name__ == "__main__":
    exit_code = main()
    sys.exit(exit_code)
"""
}