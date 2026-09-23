package com.example.thechecklistapp.domain.usecase

import com.example.thechecklistapp.data.repository.ChecklistRepository
import com.example.thechecklistapp.domain.model.ChecklistItem
import kotlinx.coroutines.flow.Flow

interface GetChecklistUseCase {
    operator fun invoke(): Flow<List<ChecklistItem>?>
}

internal class GetChecklistUseCaseImpl(
    private val checklistRepository: ChecklistRepository,
) : GetChecklistUseCase {

    override fun invoke(): Flow<List<ChecklistItem>?> = checklistRepository.getChecklist()
}
