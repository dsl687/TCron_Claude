package com.tcron.feature.terminal

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcron.core.domain.model.TerminalOutput
import com.tcron.core.domain.model.OutputType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    onNavigateBack: () -> Unit,
    viewModel: TerminalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var commandText by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terminal") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearOutput() }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                    IconButton(onClick = { viewModel.copyOutput() }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                    }
                    IconButton(onClick = { viewModel.saveAsScript() }) {
                        Icon(Icons.Default.Save, contentDescription = "Save as Script")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            // Terminal output
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                reverseLayout = false
            ) {
                items(uiState.output) { output ->
                    TerminalOutputItem(output = output)
                }
            }
            
            // Command input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$ ",
                    color = Color.Green,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
                OutlinedTextField(
                    value = commandText,
                    onValueChange = { commandText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter command...") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (commandText.isNotBlank()) {
                                viewModel.executeCommand(commandText)
                                commandText = ""
                            }
                        }
                    ),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun TerminalOutputItem(output: TerminalOutput) {
    val textColor = when (output.type) {
        OutputType.STDOUT -> Color.White
        OutputType.STDERR -> Color.Red
        OutputType.SYSTEM -> Color.Yellow
        OutputType.COMMAND -> Color.Green
    }
    
    Text(
        text = output.content,
        color = textColor,
        fontFamily = FontFamily.Monospace,
        fontSize = 12.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    )
}