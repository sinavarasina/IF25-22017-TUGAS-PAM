package com.example.myprofileapp.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.example.myprofileapp.ui.components.profile.ProfileCard
import com.example.myprofileapp.ui.theme.Colors
import com.example.myprofileapp.viewmodel.notes.NotesViewModel

@Composable
fun SettingsScreen(
    settingsManager: SettingsManager,
    notesViewModel: NotesViewModel,
    colors: Colors,
    onThemeTypeChange: (ThemeType) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProfileCard(colors = colors) {
            Text("Theme", fontWeight = FontWeight.Bold, color = colors.textPrimary, fontSize = 16.sp)

            ThemeType.entries.forEach { type ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = settingsManager.themeType == type,
                        onClick = {
                            settingsManager.themeType = type
                            onThemeTypeChange(type)
                        },
                    )
                    Text(type.name, color = colors.textPrimary)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Dark Mode", color = colors.textPrimary)
                Switch(
                    checked = settingsManager.themeMode == ThemeMode.DARK,
                    onCheckedChange = { checked ->
                        val mode = if (checked) ThemeMode.DARK else ThemeMode.LIGHT
                        settingsManager.themeMode = mode
                        onThemeModeChange(mode)
                    },
                    colors =
                        SwitchDefaults.colors(
                            checkedTrackColor = colors.accentPrimary,
                            checkedThumbColor = colors.backgroundMain,
                        ),
                )
            }
        }
    }
}
