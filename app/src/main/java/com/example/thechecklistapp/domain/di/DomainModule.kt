package com.example.thechecklistapp.domain.di

import com.example.thechecklistapp.domain.usecase.GetChecklistUseCase
import com.example.thechecklistapp.domain.usecase.GetChecklistUseCaseImpl
import com.example.thechecklistapp.domain.usecase.RefetchChecklistUseCase
import com.example.thechecklistapp.domain.usecase.RefetchChecklistUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    single<GetChecklistUseCase> { GetChecklistUseCaseImpl(checklistRepository = get()) }
    single<RefetchChecklistUseCase> { RefetchChecklistUseCaseImpl(checklistRepository = get()) }
}
