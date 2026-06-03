package com.example.myprofileapp.ui.screens.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.data.common.UiState
import com.example.myprofileapp.ui.theme.Colors
import com.example.myprofileapp.viewmodel.news.NewsViewModel

@Composable
fun NewsDetailScreen(
    articleId: Int,
    viewModel: NewsViewModel,
    colors: Colors,
) {
    LaunchedEffect(articleId) {
        viewModel.loadArticleDetail(articleId)
    }

    val detailState by viewModel.detailState.collectAsState()

    when (val state = detailState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colors.accentPrimary)
            }
        }

        is UiState.Success -> {
            val article = state.data
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = "Article #${article.id}",
                    fontSize = 14.sp,
                    color = colors.accentPrimary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.title.replaceFirstChar { it.uppercase() },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = article.body,
                    fontSize = 16.sp,
                    color = colors.textSecondary,
                    lineHeight = 24.sp,
                )
            }
        }

        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Error: ${state.message}",
                    color = colors.error,
                )
            }
        }
    }
}
