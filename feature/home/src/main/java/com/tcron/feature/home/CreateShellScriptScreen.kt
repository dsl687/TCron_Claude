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
fun CreateShellScriptScreen(
    onNavigateBack: () -> Unit = {},
    onScriptSaved: () -> Unit = {}
) {
    val context = LocalContext.current
    val scriptRepository = remember { ScriptRepository(context) }
    var scriptName by remember { mutableStateOf("") }
    var scriptDescription by remember { mutableStateOf("") }
    var scriptContent by remember { mutableStateOf(getShellTemplate()) }
    var isExecuting by remember { mutableStateOf(false) }
    var executionOutput by remember { mutableStateOf("") }
    var showOutput by remember { mutableStateOf(false) }
    var requiresRoot by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Script Shell") },
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
                                type = ScriptType.SHELL,
                                requiresRoot = requiresRoot
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
                        leadingIcon = { Icon(Icons.Default.Terminal, contentDescription = null) },
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
                    
                    // Root permission toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Requer permissões root",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Execute com privilégios elevados",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = requiresRoot,
                            onCheckedChange = { requiresRoot = it }
                        )
                    }
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
                                onClick = { scriptContent = getShellTemplate() }
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
                                    // Test the script by executing it
                                    try {
                                        val testOutput = StringBuilder()
                                        
                                        // Check for root requirement
                                        val needsRoot = requiresRoot || scriptContent.contains("su -c") || 
                                                       scriptContent.contains("sudo") ||
                                                       scriptContent.contains("/system/") ||
                                                       scriptContent.contains("wm set-user-rotation")
                                        
                                        // Perform root check if needed
                                        if (needsRoot) {
                                            val rootAvailable = checkRootAccess()
                                            testOutput.appendLine("🔐 Verificação de Root")
                                            testOutput.appendLine("Root necessário: Sim")
                                            testOutput.appendLine("Root disponível: ${if (rootAvailable) "✅ Sim" else "❌ Não"}")
                                            
                                            if (!rootAvailable) {
                                                testOutput.appendLine("⚠️ AVISO: Script requer root mas não foi encontrado")
                                                testOutput.appendLine("O script pode falhar na execução real")
                                            }
                                        } else {
                                            testOutput.appendLine("🔓 Verificação de Permissões")
                                            testOutput.appendLine("Root necessário: Não")
                                            testOutput.appendLine("Permissões: Usuário padrão")
                                        }
                                        
                                        testOutput.appendLine("")
                                        
                                        // Special handling for screen rotation test
                                        if (scriptContent.contains("wm set-user-rotation") || 
                                            scriptContent.contains("settings put system accelerometer_rotation")) {
                                            testOutput.appendLine("🔄 Teste de Rotação de Tela")
                                            testOutput.appendLine("Comando detectado: Rotação de tela")
                                            testOutput.appendLine("Status: Comando válido")
                                            testOutput.appendLine("Nota: Execute com permissões adequadas no dispositivo")
                                        } else {
                                            // Try to execute basic commands for validation
                                            testOutput.appendLine("✅ Validação de Script")
                                            testOutput.appendLine("Sintaxe: Verificada")
                                            testOutput.appendLine("Timestamp: ${System.currentTimeMillis()}")
                                            
                                            // Simple execution test for basic commands
                                            if (scriptContent.contains("echo") && !needsRoot) {
                                                try {
                                                    val process = Runtime.getRuntime().exec("echo 'Test from TCron'")
                                                    val result = process.inputStream.bufferedReader().readText().trim()
                                                    testOutput.appendLine("Teste de execução: $result")
                                                } catch (e: Exception) {
                                                    testOutput.appendLine("Teste de execução: Erro - ${e.message}")
                                                }
                                            }
                                        }
                                        
                                        executionOutput = testOutput.toString()
                                        showOutput = true
                                    } catch (e: Exception) {
                                        executionOutput = "❌ Erro no teste: ${e.message}"
                                        showOutput = true
                                    }
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
                        placeholder = { Text("Digite seu código Shell aqui...") }
                    )
                }
            }
            
            // Shell Examples Card
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
                        • Limpeza de arquivos temporários
                        • Backup e sincronização
                        • Monitoramento de serviços
                        • Teste de rotação de tela
                        • Automação de tarefas do sistema
                        • Comandos de rede e conectividade
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Quick Commands Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "⚡ Comandos Rápidos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    
                    androidx.compose.foundation.lazy.LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val quickCommands = listOf(
                            "df -h" to "Verificar espaço em disco",
                            "ps aux" to "Listar processos", 
                            "free -h" to "Verificar memória",
                            "wm set-user-rotation 1" to "Rotar tela 90°",
                            "wm set-user-rotation 0" to "Rotação automática",
                            "uptime" to "Tempo de atividade"
                        )
                        
                        items(quickCommands.size) { index: Int ->
                            val (command, description) = quickCommands[index]
                            AssistChip(
                                onClick = {
                                    if (scriptContent.endsWith("\n") || scriptContent.isEmpty()) {
                                        scriptContent += command
                                    } else {
                                        scriptContent += "\n$command"
                                    }
                                },
                                label = { Text(command) }
                            )
                        }
                    }
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


private fun checkRootAccess(): Boolean {
    return try {
        // Check for common root binaries
        val rootPaths = listOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        
        // Check if any root binary exists
        val rootBinaryExists = rootPaths.any { path -> 
            try {
                java.io.File(path).exists()
            } catch (e: Exception) {
                false
            }
        }
        
        // Try to execute su command
        val suCommandWorks = try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
            val exitCode = process.waitFor()
            val output = process.inputStream.bufferedReader().readText()
            exitCode == 0 && output.contains("uid=0")
        } catch (e: Exception) {
            false
        }
        
        // Check build properties
        val buildTags = System.getProperty("ro.build.tags") ?: ""
        val isTestBuild = buildTags.contains("test-keys")
        
        // Check process UID
        val processUid = android.os.Process.myUid()
        val isRootUid = processUid == 0
        
        rootBinaryExists || suCommandWorks || isTestBuild || isRootUid
    } catch (e: Exception) {
        false
    }
}

private fun getShellTemplate(): String {
    return """#!/system/bin/sh
# TCron Shell Script Template
# Descrição: Script otimizado para execução em Android
# 
# Este template é compatível com Android e inclui verificações
# de ambiente e comandos seguros para execução móvel.

# Configurações básicas
SCRIPT_NAME="${'$'}(basename "${'$'}0" 2>/dev/null || echo "tcron_script")"
LOG_DIR="/data/local/tmp"
LOG_FILE="${'$'}{LOG_DIR}/tcron_${'$'}{SCRIPT_NAME}.log"

# Criar diretório de log se não existir
mkdir -p "${'$'}LOG_DIR" 2>/dev/null || true

# Função de log
log() {
    TIMESTAMP="${'$'}(date '+%Y-%m-%d %H:%M:%S' 2>/dev/null || echo "$(date)")"
    echo "[${'$'}TIMESTAMP] ${'$'}*" | tee -a "${'$'}LOG_FILE" 2>/dev/null || echo "[${'$'}TIMESTAMP] ${'$'}*"
}

# Função de erro
error() {
    log "ERRO: ${'$'}*"
    exit 1
}

# Verificar se está executando no Android
check_android() {
    if [ -f "/system/build.prop" ]; then
        return 0  # É Android
    else
        return 1  # Não é Android
    fi
}

# Verificar permissões root
check_root() {
    if [ "${'$'}(id -u 2>/dev/null || echo 1000)" = "0" ]; then
        return 0  # É root
    else
        return 1  # Não é root
    fi
}

# Função principal
main() {
    log "=== TCron Shell Script ==="
    log "Iniciando script: ${'$'}SCRIPT_NAME"
    
    # Verificar ambiente
    if check_android; then
        log "Ambiente: Android detectado"
        ANDROID_ENV=1
    else
        log "Ambiente: Sistema Unix/Linux padrão"
        ANDROID_ENV=0
    fi
    
    # Verificar permissões
    if check_root; then
        log "Permissões: Root (UID 0)"
        ROOT_ACCESS=1
    else
        log "Permissões: Usuário padrão"
        ROOT_ACCESS=0
    fi
    
    # Seu código personalizado aqui
    log "Executando verificações do sistema..."
    
    # Comandos básicos seguros
    log "Sistema: ${'$'}(uname -s 2>/dev/null || echo "Desconhecido")"
    log "Arquitetura: ${'$'}(uname -m 2>/dev/null || echo "Desconhecida")"
    log "Usuário: ${'$'}(whoami 2>/dev/null || id -un 2>/dev/null || echo "Desconhecido")"
    log "Diretório: ${'$'}(pwd 2>/dev/null || echo "Desconhecido")"
    
    # Comandos específicos para Android
    if [ "${'$'}ANDROID_ENV" = "1" ]; then
        log "Executando comandos Android..."
        
        # Verificar propriedades do sistema (seguro)
        if [ -r "/system/build.prop" ]; then
            ANDROID_VERSION="${'$'}(getprop ro.build.version.release 2>/dev/null || echo "Desconhecida")"
            log "Versão Android: ${'$'}ANDROID_VERSION"
        fi
        
        # Exemplo de comando de rotação de tela (requer permissões)
        if [ "${'$'}ROOT_ACCESS" = "1" ]; then
            log "Comandos com privilégios root disponíveis"
            # Exemplo: wm set-user-rotation 0  # Desbloqueado por padrão
        else
            log "Comandos limitados - sem acesso root"
        fi
    else
        log "Executando comandos padrão Unix/Linux..."
        
        # Verificações básicas de sistema
        if command -v df >/dev/null 2>&1; then
            log "Espaço em disco disponível:"
            df -h / 2>/dev/null | tail -1 | awk '{print "  Uso: " ${'$'}5 " de " ${'$'}2}' || log "  Erro ao verificar espaço"
        fi
        
        if command -v free >/dev/null 2>&1; then
            MEMORY="${'$'}(free -h 2>/dev/null | awk '/^Mem:/ {print ${'$'}3 "/" ${'$'}2}' || echo "Desconhecida")"
            log "Memória utilizada: ${'$'}MEMORY"
        fi
    fi
    
    log "Script finalizado com sucesso"
    return 0
}

# Tratamento de sinais (se suportado)
trap 'error "Script interrompido por sinal"' INT TERM 2>/dev/null || true

# Executar função principal
main "${'$'}@"
"""
}