package com.example.myprofileapp.ui.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.data.ai.AiAction
import com.example.myprofileapp.ui.theme.Colors
import com.example.myprofileapp.viewmodel.ai.AiUiState
import com.example.myprofileapp.viewmodel.ai.AiViewModel

@Composable
fun AIAssistantScreen(
    viewModel: AiViewModel,
    colors: Colors,
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .background(colors.backgroundMain),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Gemini AI Assistant",
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Gunakan AI untuk meringkas catatan, mencari ide, memperbaiki tulisan, membuat judul, menerjemahkan, atau bertanya bebas.",
                color = colors.textSecondary,
                fontSize = 14.sp,
            )
        }

        item {
            ActionSelector(
                selectedAction = uiState.selectedAction,
                colors = colors,
                enabled = !uiState.isLoading,
                onActionSelected = viewModel::setAction,
            )
        }

        item {
            AiInputCard(
                uiState = uiState,
                colors = colors,
                onInputChange = viewModel::setInputText,
                onExecute = viewModel::execute,
                onClear = viewModel::clearAll,
            )
        }

        if (uiState.promptPreview.isNotBlank()) {
            item {
                PromptPreviewCard(
                    prompt = uiState.promptPreview,
                    colors = colors,
                )
            }
        }

        uiState.errorMessage?.let { message ->
            item {
                ErrorCard(
                    message = message,
                    colors = colors,
                )
            }
        }

        if (uiState.resultText.isNotBlank()) {
            item {
                ResultCard(
                    result = uiState.resultText,
                    colors = colors,
                    onClear = viewModel::clearResult,
                )
            }
        }
    }
}

@Composable
private fun ActionSelector(
    selectedAction: AiAction,
    colors: Colors,
    enabled: Boolean,
    onActionSelected: (AiAction) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = colors.backgroundCard),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "AI Action",
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    enabled = enabled,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = colors.accentPrimary,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedAction.title,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = colors.textSecondary,
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(colors.backgroundCard),
                ) {
                    AiAction.entries.forEach { action ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = action.title,
                                        color = colors.textPrimary,
                                        fontWeight = if (action == selectedAction) FontWeight.Bold else FontWeight.Normal,
                                    )
                                    Text(
                                        text = action.description,
                                        color = colors.textSecondary,
                                        fontSize = 12.sp,
                                    )
                                }
                            },
                            onClick = {
                                onActionSelected(action)
                                expanded = false
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = selectedAction.description,
                color = colors.textSecondary,
                fontSize = 13.sp,
            )
        }
    }
}

@Composable
private fun AiInputCard(
    uiState: AiUiState,
    colors: Colors,
    onInputChange: (String) -> Unit,
    onExecute: () -> Unit,
    onClear: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colors.backgroundCard),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Input",
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.inputText,
                onValueChange = onInputChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                minLines = 5,
                maxLines = 10,
                placeholder = {
                    Text(
                        text = "Masukkan catatan, topik, atau pertanyaan...",
                        color = colors.textSecondary,
                    )
                },
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary,
                        focusedBorderColor = colors.accentPrimary,
                        unfocusedBorderColor = colors.borderUnfocused,
                        focusedContainerColor = colors.backgroundCard,
                        unfocusedContainerColor = colors.backgroundCard,
                        cursorColor = colors.accentPrimary,
                    ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onExecute,
                    enabled = uiState.inputText.isNotBlank() && !uiState.isLoading,
                    modifier = Modifier.weight(1f),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = colors.accentPrimary,
                            contentColor = colors.backgroundMain,
                        ),
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 8.dp),
                            strokeWidth = 2.dp,
                            color = colors.backgroundMain,
                        )
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (uiState.isLoading) "Processing..." else "Run AI")
                }

                OutlinedButton(
                    onClick = onClear,
                    enabled = !uiState.isLoading,
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = colors.error,
                    )
                }
            }
        }
    }
}

@Composable
private fun PromptPreviewCard(
    prompt: String,
    colors: Colors,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colors.backgroundCard),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Prompt Preview",
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = prompt,
                color = colors.textSecondary,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}

@Composable
private fun ResultCard(
    result: String,
    colors: Colors,
    onClear: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colors.backgroundCard),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "AI Result",
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = onClear) {
                    Text("Clear", color = colors.error)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = result,
                color = colors.textPrimary,
                fontSize = 15.sp,
                lineHeight = 22.sp,
            )
        }
    }
}

@Composable
private fun ErrorCard(
    message: String,
    colors: Colors,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colors.error.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = message,
            color = colors.error,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold,
        )
    }
}
