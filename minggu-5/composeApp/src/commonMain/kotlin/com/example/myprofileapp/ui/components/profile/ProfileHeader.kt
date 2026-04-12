package com.example.myprofileapp.ui.components.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.ui.colors.Colors
import myprofileapp.composeapp.generated.resources.Res
import myprofileapp.composeapp.generated.resources.nagato_yuki_pfp_noupscale_n3_s1
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileHeader(name: String, bio: String, statusColor: Color, colors: Colors) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape)
                    .background(colors.backgroundTopBar)
                    .border(2.dp, colors.accentPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.nagato_yuki_pfp_noupscale_n3_s1),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(120.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
            Box(
                modifier = Modifier.size(28.dp).clip(CircleShape).background(statusColor)
                    .border(3.dp, colors.backgroundMain, CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary
        )

        Text(text = bio, fontSize = 16.sp, color = colors.textSecondary)
    }
}