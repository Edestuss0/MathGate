package com.mathgate.app.features.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun CampaignHomeScreen(
    viewModel: CampaignViewModel = hiltViewModel(),
    onStartButtonClick: () -> Unit
) {

    LaunchedEffect(Unit) {
     viewModel.initializeData()
    }

    val isLast by viewModel.isLastCampaign.collectAsState()
    val currentCampaign by viewModel.currentCampaign.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!isLast) {
                        Text("Кампания", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Text("Уровень ${currentCampaign?.id ?: 1}", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 8.dp))
                        Text("Пройди все испытания!", fontSize = 16.sp)
                    } else {
                        Text("Поздравляем!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Вы прошли всю кампанию", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            if (!isLast) {
                Button(
                    onClick = onStartButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text(text = "Начать", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}