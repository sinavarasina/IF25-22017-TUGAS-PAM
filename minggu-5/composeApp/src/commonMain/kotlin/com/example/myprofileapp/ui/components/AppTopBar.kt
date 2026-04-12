package com.example.myprofileapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.example.myprofileapp.ui.theme.Colors

@Composable
fun AppTopBar(
    title: String,
    colors: Colors,
    activeThemeType: ThemeType,
    themeMode: ThemeMode,
    onThemeTypeChange: (ThemeType) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
    showThemeControls: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.backgroundTopBar)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = colors.textPrimary,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        if (showThemeControls) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }

                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(
                            text = activeThemeType.name,
                            color = colors.accentPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(colors.backgroundCard)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Catppuccin", color = colors.textPrimary) },
                            onClick = {
                                onThemeTypeChange(ThemeType.CATPPUCCIN)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("GruvBox", color = colors.textPrimary) },
                            onClick = {
                                onThemeTypeChange(ThemeType.GRUVBOX)
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (themeMode == ThemeMode.DARK) "Dark" else "Light",
                    color = colors.textSecondary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Switch(
                    checked = themeMode == ThemeMode.DARK,
                    onCheckedChange = { isChecked ->
                        val newMode = if (isChecked) ThemeMode.DARK else ThemeMode.LIGHT
                        onThemeModeChange(newMode)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.backgroundMain,
                        checkedTrackColor = colors.accentPrimary,
                        uncheckedThumbColor = colors.backgroundMain,
                        uncheckedTrackColor = colors.borderUnfocused
                    )
                )
            }
        }
    }
}