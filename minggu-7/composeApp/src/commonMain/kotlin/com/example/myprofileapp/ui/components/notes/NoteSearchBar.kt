package com.example.myprofileapp.ui.components.notes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.ui.theme.Colors

@Composable
fun NoteSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    colors: Colors,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Find notes...", fontSize = 14.sp) },
        leadingIcon = {
            IconButton(onClick = {
                onQueryChange("")
                onClose()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Close Search",
                    tint = colors.textSecondary,
                )
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = colors.textSecondary,
                    )
                }
            }
        },
        singleLine = true,
        textStyle = TextStyle(fontSize = 14.sp, color = colors.textPrimary),
        shape = RoundedCornerShape(100),
        modifier =
            modifier
                .fillMaxWidth()
                .height(50.dp),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accentPrimary,
                unfocusedBorderColor = colors.borderUnfocused,
                focusedContainerColor = colors.backgroundCard,
                unfocusedContainerColor = colors.backgroundCard,
                cursorColor = colors.accentPrimary,
            ),
    )
}
