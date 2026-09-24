package com.example.thechecklistapp.data.di

import android.util.Log
import androidx.room.Room
import com.example.thechecklistapp.data.api.ChecklistApi
import com.example.thechecklistapp.data.api.ChecklistApiImpl
import com.example.thechecklistapp.data.client.defaultHttpClient
import com.example.thechecklistapp.data.persistance.ChecklistDatabase
import com.example.thechecklistapp.data.persistance.ChecklistLocalDataSource
import com.example.thechecklistapp.data.persistance.ChecklistLocalDataSourceImpl
import com.example.thechecklistapp.data.repository.ChecklistRepository
import com.example.thechecklistapp.data.repository.ChecklistRepositoryImpl
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

const val HTTP_CLIENT = "httpClient"
const val BACKGROUND_SCOPE = "backgroundScope"
const val EXCEPTION_HANDLER = "exceptionHandler"
const val APP_JSON = "appJson"

val dataModule = module {
    single<Json>(named(APP_JSON)) {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single<HttpClient>(named(HTTP_CLIENT)) {
        defaultHttpClient(json = get(named(APP_JSON)))
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
    single {
        Room.databaseBuilder(
            androidContext(),
            ChecklistDatabase::class.java,
            "checklist.db",
        ).build()
    }
    single { get<ChecklistDatabase>().checklistCacheDao() }
    single<ChecklistLocalDataSource> {
        ChecklistLocalDataSourceImpl(
            checklistCacheDao = get(),
            json = get(named(APP_JSON)),
        )
    }
    single<ChecklistRepository> {
        ChecklistRepositoryImpl(
            checklistApi = get(),
            checklistLocalDataSource = get(),
            scope = get(named(BACKGROUND_SCOPE)),
        )
    }
}
