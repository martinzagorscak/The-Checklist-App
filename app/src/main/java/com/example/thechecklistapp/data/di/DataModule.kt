package com.example.thechecklistapp.data.di

import android.util.Log
import com.example.thechecklistapp.data.api.ChecklistApi
import com.example.thechecklistapp.data.api.ChecklistApiImpl
import com.example.thechecklistapp.data.client.defaultHttpClient
import com.example.thechecklistapp.data.repository.ChecklistRepository
import com.example.thechecklistapp.data.repository.ChecklistRepositoryImpl
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

const val HTTP_CLIENT = "httpClient"
const val BACKGROUND_SCOPE = "backgroundScope"
const val EXCEPTION_HANDLER = "exceptionHandler"

val dataModule = module {
    single<HttpClient>(named(HTTP_CLIENT)) {
        defaultHttpClient(
            json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            },
        )
    }

    // background concurrency
    single<CoroutineScope>(named(BACKGROUND_SCOPE)) {
        CoroutineScope(Dispatchers.IO + SupervisorJob() + get(named(EXCEPTION_HANDLER)))
    }
    factory<CoroutineContext>(named(EXCEPTION_HANDLER)) {
        object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
            override fun handleException(context: CoroutineContext, exception: Throwable) {
                Log.e("APP_BACKGROUND_SCOPE", "Exception, $exception was thrown inside context: $context")
            }
        }
    }

    single<ChecklistApi> { ChecklistApiImpl(client = get(named(HTTP_CLIENT))) }
    single<ChecklistRepository> {
        ChecklistRepositoryImpl(
            checklistApi = get(),
            scope = get(named(BACKGROUND_SCOPE)),
        )
    }
}
