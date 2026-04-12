package com.example.myprofileapp.ui.components.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.myprofileapp.ui.colors.Colors


@Composable
fun EditProfileCard(
    draftName: String,
    draftBio: String,
    draftStudentId: String,
    draftEmail: String,
    draftPhone: String,
    draftWebsite: String,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onStudentIdChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onWebsiteChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    colors: Colors,
    modifier: Modifier = Modifier
) {
    ProfileCard(colors = colors) {
        Text("Edit Profile", color = colors.textPrimary, fontWeight = FontWeight.Bold)

        LabeledTextField(
            label = "Name",
            value = draftName,
            onValueChange = onNameChange,
            colors = colors
        )
        LabeledTextField(
            label = "Bio",
            value = draftBio,
            onValueChange = onBioChange,
            colors = colors
        )
        LabeledTextField(
            label = "Student ID",
            value = draftStudentId,
            onValueChange = onStudentIdChange,
            colors = colors
        )
        LabeledTextField(
            label = "Email",
            value = draftEmail,
            onValueChange = onEmailChange,
            colors = colors
        )
        LabeledTextField(
            label = "Phone",
            value = draftPhone,
            onValueChange = onPhoneChange,
            colors = colors
        )
        LabeledTextField(
            label = "Website",
            value = draftWebsite,
            onValueChange = onWebsiteChange,
            colors = colors
        )

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.success,
                contentColor = colors.backgroundMain
            )
        ) {
            Text("Save Changes")
        }
    }
}