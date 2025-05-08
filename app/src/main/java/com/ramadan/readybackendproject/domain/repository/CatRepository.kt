package com.ramadan.readybackendproject.domain.repository

import com.ramadan.readybackendproject.domain.model.BaseResult
import com.ramadan.readybackendproject.domain.model.CatImage
import kotlinx.coroutines.flow.Flow


interface CatRepository {
    fun getCatImages(limit: Int = 10): Flow<BaseResult<List<CatImage>>>

    // Add this function to the interface
    fun refreshCatImages(limit: Int = 10): Flow<BaseResult<List<CatImage>>>
}