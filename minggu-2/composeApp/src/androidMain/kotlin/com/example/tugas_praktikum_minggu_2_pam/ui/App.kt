package com.example.tugas_praktikum_minggu_2_pam.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugas_praktikum_minggu_2_pam.ui.components.NewsCard
import com.example.tugas_praktikum_minggu_2_pam.ui.style.colorscheme.CatpuccinMocha
import com.example.tugas_praktikum_minggu_2_pam.viewmodel.NewsFeedViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = remember { NewsFeedViewModel() }

        LaunchedEffect(Unit) {
            viewModel.startProcessing()
        }

        val count by viewModel.readCount.collectAsState()
        val feedList by viewModel.newsFeed.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CatpuccinMocha.base)
                .safeContentPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "News Feed - 123140107",
                color = CatpuccinMocha.mauve,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "total berita dibaca : $count",
                color = CatpuccinMocha.green,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "hint: click berita untuk membaca",
                color = CatpuccinMocha.yellow,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 16.dp, top = 2.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(feedList) { content ->
                    NewsCard(
                        content = content,
                        onRead = { viewModel.markNewsAsRead() })
                }
            }
        }
    }
}