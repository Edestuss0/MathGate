package com.mathgate.app.shared.reference.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.PrimaryButton

@Composable
fun ReferenceScreen(
    onTrigonometryNavigate: () -> Unit
) {
    AppScaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                AppCard(modifier = Modifier.weight(1f)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = "Справочник формул",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        PrimaryButton(
                            text = "Скоро",
                            onClick = {},
                            enabled = false
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                AppCard(modifier = Modifier.weight(1f)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = "Геометрический справочник",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        PrimaryButton(
                            text = "Скоро",
                            onClick = {},
                            enabled = false
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = "Тригонометрический круг",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Перейти",
                        onClick = {onTrigonometryNavigate()},
                    )
                }
            }
        }
    }
}