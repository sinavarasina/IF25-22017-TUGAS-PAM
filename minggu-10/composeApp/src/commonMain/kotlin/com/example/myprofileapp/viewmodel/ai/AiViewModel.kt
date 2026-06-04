package com.example.myprofileapp.viewmodel.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofileapp.data.ai.AiAction
import com.example.myprofileapp.data.ai.AiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AiUiState(
    val selectedAction: AiAction = AiAction.SUMMARIZE,
    val inputText: String = "",
    val resultText: String = "",
    val promptPreview: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class AiViewModel(
    private val repository: AiRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AiUiState())
    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    fun setAction(action: AiAction) {
        _uiState.update {
            it.copy(
                selectedAction = action,
                promptPreview = repository.getPromptPreview(action, it.inputText),
                errorMessage = null,
            )
        }
    }

    fun setInputText(text: String) {
        _uiState.update {
            it.copy(
                inputText = text,
                promptPreview = repository.getPromptPreview(it.selectedAction, text),
                errorMessage = null,
            )
        }
    }

    fun execute() {
        val state = _uiState.value
        if (state.inputText.isBlank() || state.isLoading) return

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                resultText = "",
                promptPreview = repository.getPromptPreview(it.selectedAction, it.inputText),
            )
        }

        viewModelScope.launch {
            repository
                .execute(
                    action = state.selectedAction,
                    input = state.inputText,
                ).onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            resultText = result,
                            isLoading = false,
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            errorMessage = error.message ?: "Gagal mendapatkan respons AI.",
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun clearResult() {
        _uiState.update {
            it.copy(
                resultText = "",
                errorMessage = null,
            )
        }
    }

    fun clearAll() {
        _uiState.value = AiUiState()
    }
}
