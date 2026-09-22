package com.example.thechecklistapp.data.di

import com.example.thechecklistapp.data.api.ChecklistApi
import com.example.thechecklistapp.data.api.ChecklistApiImpl
import com.example.thechecklistapp.data.api.client.defaultHttpClient
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val HTTP_CLIENT = "httpClient"

val dataModule = module {
    single<HttpClient>(named(HTTP_CLIENT)) {
        defaultHttpClient(
            json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            },
        )
    }
    single<ChecklistApi> { ChecklistApiImpl(client = get(named(HTTP_CLIENT))) }
}
