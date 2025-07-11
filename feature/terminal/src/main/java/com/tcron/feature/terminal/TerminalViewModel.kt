package com.tcron.feature.terminal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcron.core.common.Result
import com.tcron.core.domain.model.OutputType
import com.tcron.core.domain.model.TerminalOutput
import com.tcron.core.domain.repository.TerminalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TerminalViewModel @Inject constructor(
    private val terminalRepository: TerminalRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TerminalUiState())
    val uiState: StateFlow<TerminalUiState> = _uiState.asStateFlow()
    
    private var currentSessionId: String? = null
    
    init {
        initializeSession()
    }
    
    private fun initializeSession() {
        viewModelScope.launch {
            val result = terminalRepository.createSession("default", "/")
            when (result) {
                is Result.Success -> {
                    currentSessionId = result.data.id
                    addOutput("Terminal session started", OutputType.SYSTEM)
                }
                is Result.Error -> {
                    addOutput("Failed to start terminal: ${result.exception.message}", OutputType.STDERR)
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
    
    fun executeCommand(command: String) {
        if (command.isBlank()) return
        
        val sessionId = currentSessionId ?: return
        
        // Add command to output
        addOutput("$ $command", OutputType.COMMAND)
        
        viewModelScope.launch {
            val result = terminalRepository.executeCommand(sessionId, command)
            when (result) {
                is Result.Success -> {
                    val terminalCommand = result.data
                    if (terminalCommand.output.isNotEmpty()) {
                        addOutput(terminalCommand.output, OutputType.STDOUT)
                    }
                    if (terminalCommand.errorOutput.isNotEmpty()) {
                        addOutput(terminalCommand.errorOutput, OutputType.STDERR)
                    }
                }
                is Result.Error -> {
                    addOutput("Error: ${result.exception.message}", OutputType.STDERR)
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
    
    fun clearOutput() {
        _uiState.value = _uiState.value.copy(output = emptyList())
        addOutput("Terminal cleared", OutputType.SYSTEM)
    }
    
    fun copyOutput() {
        val outputText = _uiState.value.output.joinToString("\n") { it.content }
        // TODO: Implement clipboard copy
        addOutput("Output copied to clipboard", OutputType.SYSTEM)
    }
    
    fun saveAsScript() {
        // TODO: Implement save as script functionality
        addOutput("Save as script functionality not yet implemented", OutputType.SYSTEM)
    }
    
    private fun addOutput(content: String, type: OutputType) {
        val output = TerminalOutput(
            content = content,
            type = type,
            timestamp = Date(),
            isAnsiColored = false
        )
        
        _uiState.value = _uiState.value.copy(
            output = _uiState.value.output + output
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        currentSessionId?.let { sessionId ->
            viewModelScope.launch {
                terminalRepository.closeSession(sessionId)
            }
        }
    }
}

data class TerminalUiState(
    val output: List<TerminalOutput> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)