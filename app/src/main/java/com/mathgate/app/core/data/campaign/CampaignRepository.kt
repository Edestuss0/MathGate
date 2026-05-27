package com.mathgate.app.core.data.campaign

import android.content.Context
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray

class CampaignRepository(private val campaignDao: CampaignDao) {

    val allCampaigns = campaignDao.getAllCampaigns()

    suspend fun getCount(): Int {
        return campaignDao.getCount()
    }

    suspend fun checkAndPreloadDb(context: Context) {
        try {
            val jsonString = context.assets.open("campaign/campaign.json")
                .bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val campaignsToPreload = mutableListOf<CampaignEntity>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                val campaign = CampaignEntity(
                    id = jsonObject.optInt("id", i + 1),
                    question = jsonObject.getString("question"),
                    answer = jsonObject.getInt("answer"),
                    theme = jsonObject.getInt("theme"),
                    solution = jsonObject.getString("solution")
                )
                campaignsToPreload.add(campaign)
            }

            campaignDao.insertCampaigns(campaignsToPreload)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println(campaignDao.getCount())
        println(allCampaigns)
    }

    suspend fun getCampaignById(id: Int): CampaignEntity {
        val campaign = campaignDao.getCampaignById(id) ?: CampaignEntity(0, "", 0, 0, "")
        return campaign
    }
}