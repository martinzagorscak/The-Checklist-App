package com.example.thechecklistapp.data.api

import android.util.Log
import com.example.thechecklistapp.data.model.ApiChecklistItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val CHECKLIST_ENDPOINT_URL =
    "https://gist.githubusercontent.com/aruana-lumiform/383e1291df3e0cc1d49aeb14d45f5b94/raw/lumiform-android-test.json"

interface ChecklistApi {
    suspend fun getChecklistItems(): Result<List<ApiChecklistItem>>
}

internal class ChecklistApiImpl(
    private val client: HttpClient,
) : ChecklistApi {
    override suspend fun getChecklistItems(): Result<List<ApiChecklistItem>> {
        val response = try {
            client.get(urlString = CHECKLIST_ENDPOINT_URL)
        } catch (e: Exception) {
            Log.e("ChecklistApiImpl", "Exception : ${e.message}")
            return Result.failure(e)
        }

        return when (response.status.value) {
            in 200..299 -> {
                Result.success(response.body())
            }

            else -> {
                Result.failure(exception = Exception("Api call not successful"))
            }
        }
    }
}
