package com.example.thechecklistapp.ui.viewmodel.di

import com.example.thechecklistapp.ui.viewmodel.ChecklistViewModel
import com.example.thechecklistapp.ui.viewmodel.ChecklistViewModelImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<ChecklistViewModel> {
        ChecklistViewModelImpl(
            getChecklistUseCase = get(),
            refetchChecklistUseCase = get(),
        )
    }
}
