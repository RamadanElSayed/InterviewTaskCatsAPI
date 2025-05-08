package com.ramadan.readybackendproject.presentation.mapper

import com.ramadan.readybackendproject.domain.model.CatImage
import com.ramadan.readybackendproject.presentation.model.CatImageUI
import javax.inject.Inject

class CatImageDomainMapper @Inject constructor() {

    private fun mapToPresentation(domainModel: CatImage): CatImageUI {
        return CatImageUI(
            id = domainModel.id,
            imageUrl = domainModel.url,
        )
    }

    fun mapToPresentationList(domainModels: List<CatImage>): List<CatImageUI> {
        return domainModels.map { mapToPresentation(it) }
    }
}