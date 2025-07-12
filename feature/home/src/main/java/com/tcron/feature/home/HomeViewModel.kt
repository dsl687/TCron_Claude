package com.tcron.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcron.core.common.SystemInfoManager
import com.tcron.core.common.SystemMetrics
import com.tcron.core.domain.model.Task
import com.tcron.core.domain.model.AppNotification
import com.tcron.core.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val systemInfoManager: SystemInfoManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadTasks()
        loadSystemMetrics()
        startAutoRefresh()
    }
    
    private fun loadTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks()
                .onStart {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
                .collect { tasks ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tasks = tasks,
                        error = null
                    )
                }
        }
    }
    
    fun refreshTasks() {
        loadTasks()
    }
    
    fun toggleTaskEnabled(taskId: String, enabled: Boolean) {
        viewModelScope.launch {
            taskRepository.toggleTaskEnabled(taskId, enabled)
        }
    }
    
    private fun loadSystemMetrics() {
        viewModelScope.launch {
            try {
                val metrics = systemInfoManager.getCurrentMetrics()
                _uiState.value = _uiState.value.copy(systemMetrics = metrics)
            } catch (e: Exception) {
                // Handle error silently, metrics will show default values
            }
        }
    }
    
    fun refreshSystemMetrics() {
        loadSystemMetrics()
    }
    
    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(60000) // Refresh every minute
                loadSystemMetrics()
            }
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val notifications: List<AppNotification> = emptyList(),
    val systemMetrics: SystemMetrics? = null,
    val error: String? = null
)