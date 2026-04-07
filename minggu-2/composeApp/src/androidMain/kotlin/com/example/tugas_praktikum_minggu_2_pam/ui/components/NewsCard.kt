package com.example.tugas_praktikum_minggu_2_pam.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugas_praktikum_minggu_2_pam.ui.style.colorscheme.CatpuccinMocha

@Composable
fun NewsCard(content: String, onRead: () -> Unit) {
    val parts = content.split("\n")
    val title = parts.getOrNull(0) ?: ""
    val detail = parts.getOrNull(1) ?: ""

    var isRead by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRead) CatpuccinMocha.surface1 else CatpuccinMocha.surface0
        ),
        modifier = Modifier
            .padding(16.dp)
            .clickable(enabled = !isRead) {
                isRead = true
                onRead()
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                color = if (isRead) CatpuccinMocha.subtext0 else CatpuccinMocha.text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (detail.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = detail,
                    color = CatpuccinMocha.subtext0,
                    fontSize = 14.sp
                )
            }
        }
    }
}