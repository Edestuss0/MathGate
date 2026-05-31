package com.mathgate.app.features.campaign

import com.mathgate.app.core.data.campaign.CampaignEntity
import com.mathgate.app.ui.components.AppSnackbarVisuals

data class CampaignState(
    val currentCampaign: CampaignEntity = CampaignEntity(0, "", 0, 0, ""),
    val isError: Boolean = false,
    val snackbarMessage: AppSnackbarVisuals? = null,
)