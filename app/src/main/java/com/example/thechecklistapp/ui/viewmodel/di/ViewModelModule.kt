package com.example.thechecklistapp.ui.viewmodel.di

import com.example.thechecklistapp.ui.viewmodel.ChecklistViewModel
import com.example.thechecklistapp.ui.viewmodel.ChecklistViewModelImpl
import com.example.thechecklistapp.ui.viewmodel.DetailedImageViewModel
import com.example.thechecklistapp.ui.viewmodel.DetailedImageViewModelImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<ChecklistViewModel> {
        ChecklistViewModelImpl(
            getChecklistUseCase = get(),
            refetchChecklistUseCase = get(),
            connectivityStatusPublisher = get()
        )
    }
    viewModel<DetailedImageViewModel> { (imageSectionId: Int) ->
        DetailedImageViewModelImpl(
            imageSectionId = imageSectionId,
            getChecklistUseCase = get(),
        )
    }
}
