package com.mathgate.app.core.data.oge

import android.content.Context
import android.util.Log
import com.mathgate.app.core.api.ApiClient
import com.mathgate.app.core.entities.FormulaBlock
import com.mathgate.app.core.entities.ImageBlock
import com.mathgate.app.core.entities.OgeQuestion
import com.mathgate.app.core.entities.TaskBlock
import com.mathgate.app.core.entities.TextBlock
import dagger.hilt.android.qualifiers.ApplicationContext

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.lang.Exception
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class OgeParser @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val client = ApiClient.client
    suspend fun parseOge(): OgeQuestion {
        return try {

            val jsonString = context.assets.open("oge/oge.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val length = jsonArray.length()
            val random = Random.nextInt(0..(length - 1))

            val html = client.get(
                "https://math-oge.sdamgia.ru/problem?id=${jsonArray.getInt(random)}&print=true"
            ).bodyAsText()

            val document = Jsoup.parse(html, "https://math-oge.sdamgia.ru")

            val body = document.selectFirst("div.pbody, div.problem_body")
                ?: return OgeQuestion(emptyList(), "")

            val rawAnswer = document.selectFirst("div.answer span")
                ?.text()
                ?.trim()
                ?: ""

            val answer = rawAnswer
                .replace(Regex("(?i)Ответ:\\s*:?"), "")
                .trim()

            val blocks = parseBlocks(body)

            OgeQuestion(
                blocks = blocks,
                answer = answer
            )

        } catch (e: Exception) {
            Log.e("OGE", "parse error", e)
            OgeQuestion(emptyList(), "")
        }
    }

    private fun parseBlocks(root: Element): List<TaskBlock> {
        val result = mutableListOf<TaskBlock>()

        root.childNodes().forEach { node ->
            parseNode(node, result)
        }

        return result
    }

    private fun parseNode(node: Node, out: MutableList<TaskBlock>) {

        when (node) {

            is TextNode -> {
                val text = cleanText(node.text())
                if (text.isNotBlank()) {
                    out += TextBlock(text)
                }
            }

            is Element -> {

                when (node.tagName()) {

                    "img" -> {
                        val src = node.absUrl("src").ifBlank {
                            node.attr("src")
                        }

                        if (src.isBlank()) return

                        when {
                            node.hasClass("tex") -> {
                                out += FormulaBlock(src)
                            }

                            else -> {
                                out += ImageBlock(src)
                            }
                        }
                    }

                    else -> {
                        node.childNodes().forEach {
                            parseNode(it, out)
                        }
                    }
                }
            }
        }
    }

    private fun cleanText(text: String): String {
        return text
            .replace("\u00AD", "")
            .replace("\u202f", " ")
            .replace("\u00A0", " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}