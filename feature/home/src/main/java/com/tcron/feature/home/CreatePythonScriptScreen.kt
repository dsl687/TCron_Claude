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
# TCron Python Script - Teste Funcional
# Este é um script simples e funcional para demonstrar o TCron

import os
import sys
import datetime
import json

def main():
    print("=== TCron Python Test ===")
    print(f"Data/Hora: {datetime.datetime.now()}")
    print(f"Python: {sys.version}")
    print(f"Diretório: {os.getcwd()}")
    print(f"Usuário: {os.getenv('USER', 'desconhecido')}")
    
    # Criar arquivo de teste
    test_data = {
        "script": "Python TCron Test",
        "timestamp": str(datetime.datetime.now()),
        "status": "executado com sucesso",
        "python_version": sys.version.split()[0],
        "platform": sys.platform
    }
    
    # Tentar criar arquivo em locais seguros
    test_paths = [
        "/data/local/tmp/tcron_python_test.json",
        "/tmp/tcron_python_test.json",
        "./tcron_python_test.json"
    ]
    
    for test_file in test_paths:
        try:
            # Criar diretório se necessário
            directory = os.path.dirname(test_file)
            if directory and not os.path.exists(directory):
                os.makedirs(directory, exist_ok=True)
            
            # Escrever arquivo
            with open(test_file, 'w') as f:
                json.dump(test_data, f, indent=2)
            
            print(f"✅ Arquivo criado: {test_file}")
            
            # Verificar se pode ler
            with open(test_file, 'r') as f:
                data_read = json.load(f)
            
            print(f"✅ Arquivo lido: {data_read['status']}")
            break
            
        except Exception as e:
            print(f"❌ Falha em {test_file}: {e}")
            continue
    else:
        print("❌ Não foi possível criar arquivo em nenhum local")
    
    print("✅ Script Python executado com sucesso!")
    return 0

if __name__ == "__main__":
    exit_code = main()
    print(f"Código de saída: {exit_code}")
    sys.exit(exit_code)
"""
}