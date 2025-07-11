package com.tcron.feature.terminal

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

data class TerminalLine(
    val text: String,
    val type: LineType = LineType.OUTPUT,
    val timestamp: Long = System.currentTimeMillis()
)

enum class LineType {
    INPUT, OUTPUT, ERROR, SUCCESS
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun TerminalScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: TerminalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentInput by remember { mutableStateOf("") }
    var terminalLines by remember { 
        mutableStateOf(listOf(
            TerminalLine("Welcome to TCron Terminal v1.0", LineType.SUCCESS),
            TerminalLine("Type 'help' for available commands", LineType.OUTPUT),
            TerminalLine("", LineType.OUTPUT)
        ))
    }
    var showDrawer by remember { mutableStateOf(false) }
    var showFloatingButtons by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()
    
    // Auto scroll to bottom when new lines are added
    LaunchedEffect(terminalLines.size) {
        if (terminalLines.isNotEmpty()) {
            listState.animateScrollToItem(terminalLines.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Terminal",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { showDrawer = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E)
                )
            )
        },
        floatingActionButton = {
            if (showFloatingButtons) {
                TerminalFloatingActions(
                    onClear = {
                        terminalLines = listOf(
                            TerminalLine("Terminal cleared", LineType.SUCCESS),
                            TerminalLine("", LineType.OUTPUT)
                        )
                    },
                    onKeyboardToggle = {
                        keyboardController?.let {
                            if (currentInput.isNotEmpty()) {
                                it.hide()
                            } else {
                                it.show()
                            }
                        }
                    }
                )
            }
        },
        containerColor = Color(0xFF1E1E1E)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Terminal output
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E))
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(terminalLines) { line ->
                        TerminalLineItem(line)
                    }
                }
                
                // Input area
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2D2D2D)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$ ",
                            color = Color(0xFF4CAF50),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        
                        OutlinedTextField(
                            value = currentInput,
                            onValueChange = { currentInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { 
                                Text(
                                    "Digite um comando...",
                                    color = Color(0xFF666666),
                                    fontFamily = FontFamily.Monospace
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Send
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF4CAF50),
                                unfocusedBorderColor = Color(0xFF666666),
                                cursorColor = Color(0xFF4CAF50)
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp
                            )
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        IconButton(
                            onClick = {
                                if (currentInput.isNotEmpty()) {
                                    executeCommand(currentInput, terminalLines) { newLines ->
                                        terminalLines = newLines
                                    }
                                    currentInput = ""
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Executar",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }
            
            // Terminal Drawer
            if (showDrawer) {
                TerminalDrawer(
                    onDismiss = { showDrawer = false },
                    onClearHistory = {
                        terminalLines = listOf(
                            TerminalLine("History cleared", LineType.SUCCESS),
                            TerminalLine("", LineType.OUTPUT)
                        )
                        showDrawer = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TerminalLineItem(line: TerminalLine) {
    val color = when (line.type) {
        LineType.INPUT -> Color(0xFF4CAF50)
        LineType.OUTPUT -> Color.White
        LineType.ERROR -> Color(0xFFE57373)
        LineType.SUCCESS -> Color(0xFF81C784)
    }
    
    val prefix = when (line.type) {
        LineType.INPUT -> "$ "
        LineType.ERROR -> "ERROR: "
        LineType.SUCCESS -> "✓ "
        LineType.OUTPUT -> ""
    }
    
    Text(
        text = "$prefix${line.text}",
        color = color,
        fontFamily = FontFamily.Monospace,
        fontSize = 13.sp,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
private fun TerminalFloatingActions(
    onClear: () -> Unit,
    onKeyboardToggle: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Navigation keys
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TerminalKey("↑") { /* TODO: Arrow up */ }
            TerminalKey("↓") { /* TODO: Arrow down */ }
            TerminalKey("←") { /* TODO: Arrow left */ }
            TerminalKey("→") { /* TODO: Arrow right */ }
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TerminalKey("HOME") { /* TODO: Home key */ }
            TerminalKey("END") { /* TODO: End key */ }
            TerminalKey("TAB") { /* TODO: Tab key */ }
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TerminalKey("CTRL") { /* TODO: Ctrl key */ }
            TerminalKey("ALT") { /* TODO: Alt key */ }
            TerminalKey("PgUp") { /* TODO: Page up */ }
            TerminalKey("PgDn") { /* TODO: Page down */ }
        }
        
        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FloatingActionButton(
                onClick = onClear,
                modifier = Modifier.size(48.dp),
                containerColor = Color(0xFFE57373)
            ) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "Limpar terminal",
                    tint = Color.White
                )
            }
            
            FloatingActionButton(
                onClick = onKeyboardToggle,
                modifier = Modifier.size(48.dp),
                containerColor = Color(0xFF4CAF50)
            ) {
                Icon(
                    Icons.Default.Keyboard,
                    contentDescription = "Teclado",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun TerminalKey(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(width = 50.dp, height = 36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3D3D3D)
        ),
        contentPadding = PaddingValues(4.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.White
        )
    }
}

@Composable
private fun TerminalDrawer(
    onDismiss: () -> Unit,
    onClearHistory: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .width(280.dp)
                .align(Alignment.CenterEnd),
            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2D2D2D)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Terminal Settings",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                TerminalMenuItem(
                    icon = Icons.Default.History,
                    title = "Histórico",
                    subtitle = "Ver comandos anteriores"
                ) { /* TODO: Show history */ }
                
                TerminalMenuItem(
                    icon = Icons.Default.Palette,
                    title = "Temas",
                    subtitle = "Alterar cores do terminal"
                ) { /* TODO: Theme selector */ }
                
                TerminalMenuItem(
                    icon = Icons.Default.Clear,
                    title = "Limpar histórico",
                    subtitle = "Apagar todos os comandos"
                ) { onClearHistory() }
                
                TerminalMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Configurações",
                    subtitle = "Opções do terminal"
                ) { /* TODO: Terminal settings */ }
                
                TerminalMenuItem(
                    icon = Icons.Default.Help,
                    title = "Ajuda",
                    subtitle = "Comandos disponíveis"
                ) { /* TODO: Show help */ }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TerminalMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3D3D3D)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    title,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    subtitle,
                    color = Color(0xFF999999),
                    fontSize = 12.sp
                )
            }
        }
    }
}

private fun executeCommand(
    command: String,
    currentLines: List<TerminalLine>,
    onResult: (List<TerminalLine>) -> Unit
) {
    val inputLine = TerminalLine(command, LineType.INPUT)
    val newLines = currentLines + inputLine
    
    when (command.trim().lowercase()) {
        "help" -> {
            val helpText = listOf(
                "Comandos disponíveis:",
                "  help          - Mostrar esta ajuda",
                "  clear         - Limpar terminal",
                "  ls            - Listar arquivos",
                "  pwd           - Mostrar diretório atual",
                "  whoami        - Mostrar usuário atual",
                "  uname         - Informações do sistema",
                "  date          - Data e hora atual",
                "  echo [text]   - Imprimir texto",
                "  ps            - Processos em execução",
                "  df            - Espaço em disco",
                "  free          - Memória disponível",
                ""
            )
            onResult(newLines + helpText.map { TerminalLine(it, LineType.OUTPUT) })
        }
        "clear" -> {
            onResult(listOf(TerminalLine("", LineType.OUTPUT)))
        }
        "ls" -> {
            val files = listOf(
                "drwxr-xr-x  2 root root 4096 Jul 11 14:30 bin",
                "drwxr-xr-x  3 root root 4096 Jul 11 14:30 etc",
                "drwxr-xr-x  2 root root 4096 Jul 11 14:30 home",
                "drwxr-xr-x  2 root root 4096 Jul 11 14:30 tmp",
                "drwxr-xr-x  2 root root 4096 Jul 11 14:30 var",
                ""
            )
            onResult(newLines + files.map { TerminalLine(it, LineType.OUTPUT) })
        }
        "pwd" -> {
            onResult(newLines + listOf(TerminalLine("/data/data/com.tcron.app", LineType.OUTPUT), TerminalLine("", LineType.OUTPUT)))
        }
        "whoami" -> {
            onResult(newLines + listOf(TerminalLine("u0_a775", LineType.OUTPUT), TerminalLine("", LineType.OUTPUT)))
        }
        "uname" -> {
            onResult(newLines + listOf(TerminalLine("Linux localhost 6.8.0-58-generic #35~22.04.1-Ubuntu aarch64", LineType.OUTPUT), TerminalLine("", LineType.OUTPUT)))
        }
        "date" -> {
            val currentDate = java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", java.util.Locale.getDefault()).format(java.util.Date())
            onResult(newLines + listOf(TerminalLine(currentDate, LineType.OUTPUT), TerminalLine("", LineType.OUTPUT)))
        }
        "ps" -> {
            val processes = listOf(
                "PID   PPID  NAME",
                "1     0     init",
                "123   1     system_server",
                "456   1     zygote",
                "789   456   com.tcron.app",
                ""
            )
            onResult(newLines + processes.map { TerminalLine(it, LineType.OUTPUT) })
        }
        "df" -> {
            val diskInfo = listOf(
                "Filesystem     1K-blocks    Used Available Use% Mounted on",
                "/dev/root       15728640 8123456   7605184  52% /",
                "/data          51200000 20480000  30720000  40% /data",
                ""
            )
            onResult(newLines + diskInfo.map { TerminalLine(it, LineType.OUTPUT) })
        }
        "free" -> {
            val memInfo = listOf(
                "               total        used        free      shared  buff/cache   available",
                "Mem:         8388608     4194304     2097152      524288     2097152     3670016",
                ""
            )
            onResult(newLines + memInfo.map { TerminalLine(it, LineType.OUTPUT) })
        }
        else -> {
            if (command.trim().startsWith("echo ")) {
                val text = command.trim().substring(5)
                onResult(newLines + listOf(TerminalLine(text, LineType.OUTPUT), TerminalLine("", LineType.OUTPUT)))
            } else {
                onResult(newLines + listOf(TerminalLine("$command: command not found", LineType.ERROR), TerminalLine("", LineType.OUTPUT)))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TerminalScreenPreview() {
    MaterialTheme {
        TerminalScreen()
    }
}