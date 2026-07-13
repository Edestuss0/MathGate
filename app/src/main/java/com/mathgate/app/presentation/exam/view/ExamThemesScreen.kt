package com.mathgate.app.presentation.exam.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mathgate.app.features.exam.domain.entity.ExamTheme
import com.mathgate.app.ui.theme.AppBadge
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.EmptyState
import com.mathgate.app.ui.theme.PrimaryButton

@Composable
fun ExamThemesScreen(
    themes: List<ExamTheme>,
    onBackClick: () -> Unit,
    onStartClick: (number: Int) -> Unit
) {
    AppScaffold(
        hasBackButton = true,
        onBackClick = onBackClick
    ) { paddingValues ->
        if (themes.isEmpty()) {
            EmptyState("Темы не найдены")
            return@AppScaffold
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = themes) { theme ->
                ThemeBlock(
                    theme = theme,
                    onStartClick = onStartClick
                )
            }
        }
    }
}

@Composable
private fun ThemeBlock(
    theme: ExamTheme,
    onStartClick: (number: Int) -> Unit
) {
    AppCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = "№${theme.number}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = theme.label,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))
            AppBadge(
                text = "Заданий доступно: ${theme.tasks}",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "К практике",
                onClick = {onStartClick(theme.number)}
            )
        }
    }
}

@Composable @Preview(showBackground = true)
private fun ThemeBlockPreview() {
    ThemeBlock(
        theme = ExamTheme(
            tasks = 61,
            number = 1,
            label = "Планиметрия"
        ),
        onStartClick = {}
    )
}