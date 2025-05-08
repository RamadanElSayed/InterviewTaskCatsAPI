package com.ramadan.readybackendproject.domain.usecase

import com.ramadan.readybackendproject.domain.model.BaseResult
import com.ramadan.readybackendproject.domain.model.CatImage
import com.ramadan.readybackendproject.domain.repository.CatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCatImagesUseCase @Inject constructor(
    private val repository: CatRepository
) {
    companion object {
        const val DEFAULT_LIMIT = 10 // Expose constant for testing
    }
    operator fun invoke(limit: Int = 10): Flow<BaseResult<List<CatImage>>> {
        return repository.getCatImages(limit)
    }
}