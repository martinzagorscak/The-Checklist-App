package com.example.thechecklistapp.device

import org.koin.dsl.module

val deviceModule = module {
    single<ConnectivityStatusPublisher> { ConnectivityStatusPublisherImpl() }
}
