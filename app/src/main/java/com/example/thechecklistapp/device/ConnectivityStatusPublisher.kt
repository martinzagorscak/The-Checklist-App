package com.example.thechecklistapp.device

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

enum class ConnectivityStatus {
    CONNECTED,
    DISCONNECTED,
}

interface ConnectivityStatusPublisher {
    fun status(): Flow<ConnectivityStatus?>

    suspend fun publishStatus(status: ConnectivityStatus)
}

internal class ConnectivityStatusPublisherImpl : ConnectivityStatusPublisher {

    private val statusPublisher = MutableStateFlow<ConnectivityStatus?>(null)

    override fun status(): Flow<ConnectivityStatus?> = statusPublisher

    override suspend fun publishStatus(status: ConnectivityStatus) = statusPublisher.update { status }
}
