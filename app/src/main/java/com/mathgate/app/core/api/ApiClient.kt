package com.mathgate.app.core.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders

object ApiClient {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                }
            )
        }
//        install(HttpRequestRetry) {
//            maxRetries = 3
//            retryOnServerErrors(maxRetries = 3)
//            exponentialDelay()
//        }
//        install(HttpTimeout) {
//            requestTimeoutMillis = 15_000
//            connectTimeoutMillis = 10_000
//            socketTimeoutMillis = 15_000
//        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
            sanitizeHeader { it == HttpHeaders.Authorization }
        }
        expectSuccess = true
    }
}