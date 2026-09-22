package com.example.thechecklistapp.data.api.client

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.scope.Scope
import java.io.IOException

private const val REQUEST_TIMEOUT = 5000L
private const val MAX_NUMBER_OF_CALLS = 2
private const val MAX_CALL_DELAY = 5000L

internal fun Scope.defaultHttpClient(json: Json): HttpClient {

    return HttpClient {

        expectSuccess = true

        installRetryPolicy()

        installContentNegotiation(json)

        installLogging()

        installTimeout()
    }
}

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installRetryPolicy() =
    install(HttpRequestRetry) {
        maxRetries = MAX_NUMBER_OF_CALLS
        retryOnServerErrors() // Retries on 5xx status codes
        retryOnExceptionIf { request, cause ->
            cause is IOException // Retries on network errors
        }
        exponentialDelay(maxDelayMs = MAX_CALL_DELAY)
    }

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installContentNegotiation(json: Json) =
    install(ContentNegotiation) {
        json(json)

        // reading data from Gist as text/plain, but it is actually JSON
        json(json, io.ktor.http.ContentType.Text.Plain)
    }

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installLogging() {
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Log.i("HTTP Client", message)
            }
        }
    }
}

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installTimeout() =
    install(HttpTimeout) {
        requestTimeoutMillis = REQUEST_TIMEOUT
    }
