package com.example.thechecklistapp.domain.usecase

import com.example.thechecklistapp.data.repository.ChecklistRepository

interface RefetchChecklistUseCase {
    suspend operator fun invoke()
}

internal class RefetchChecklistUseCaseImpl(
    private val checklistRepository: ChecklistRepository,
) : RefetchChecklistUseCase {

    override suspend fun invoke() = checklistRepository.refetchChecklist()
}
