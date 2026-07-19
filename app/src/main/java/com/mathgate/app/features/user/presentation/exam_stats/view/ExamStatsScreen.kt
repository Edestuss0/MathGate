package com.mathgate.app.features.user.presentation.exam_stats.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mathgate.app.features.user.presentation.exam_stats.viewmodel.ExamStatsViewModel
import com.mathgate.app.shared.exam.entity.ExamTypes
import com.mathgate.app.shared.user.domain.entity.ExamStatsItem
import com.mathgate.app.ui.theme.AppBadge
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.EmptyState
import com.mathgate.app.ui.theme.MathGateTheme
import com.mathgate.app.ui.preview.PreviewData

@Composable
fun ExamStatsScreen(
    viewModel: ExamStatsViewModel = hiltViewModel(),
    onBackNavigate: () -> Unit
) {
    val stats by viewModel.stats.collectAsState()

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        hasBackButton = true,
        onBackClick = { onBackNavigate() }
    ) { paddingValues ->
        when {
            stats == null -> {
                EmptyState("Не удалось получить информацию о статистике")
            }
            (stats ?: emptyList()).isEmpty() -> {
                EmptyState("У вас пока нет статистических данных")
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(items = stats!!) {
                        ExamStatsCard(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExamStatsCard(
    item: ExamStatsItem,
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.correct) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${item.type.label} | ${item.themeLabel}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Правильный ответ: ${item.answer}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                AppBadge(
                    text = item.date,
                )
                Spacer(Modifier.width(8.dp))
                AppBadge(
                    text = "Задание №${item.themeNumber}"
                )
            }
        }
    }
}

@Preview(name = "Статистика · Экзамены (список)", showBackground = true, showSystemUi = true)
@Composable
private fun ExamStatsListPreview() {
    MathGateTheme {
        AppScaffold(
            modifier = Modifier.fillMaxSize(),
            hasBackButton = true,
            topBarText = "Статистика экзаменов"
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(items = PreviewData.examStats) { ExamStatsCard(it) }
            }
        }
    }
}

@Preview(
    name = "Статистика · Экзамены (тёмная)",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ExamStatsListDarkPreview() {
    MathGateTheme(darkTheme = true) {
        AppScaffold(
            modifier = Modifier.fillMaxSize(),
            hasBackButton = true,
            topBarText = "Статистика экзаменов"
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(items = PreviewData.examStats) { ExamStatsCard(it) }
            }
        }
    }
}