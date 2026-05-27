package com.mathgate.app.features.campaign

import com.mathgate.app.core.data.campaign.CampaignEntity

data class CampaignState(
    val currentCampaign: CampaignEntity = CampaignEntity(0, "", 0, 0, ""),
    val isError: Boolean = false,
    val message: String? = null,
)