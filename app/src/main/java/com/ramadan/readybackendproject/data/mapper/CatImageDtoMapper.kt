package com.ramadan.readybackendproject.data.mapper

import com.ramadan.readybackendproject.data.model.CatImageDto
import com.ramadan.readybackendproject.domain.model.CatImage
import javax.inject.Inject


class CatImageDtoMapper @Inject constructor() {

    private fun mapToDomain(dto: CatImageDto): CatImage {
        return CatImage(
            id = dto.id,
            url = dto.url,
            width = dto.width,
            height = dto.height,
        )
    }


    fun mapToDomainList(dtos: List<CatImageDto>): List<CatImage> {
        return dtos.map { mapToDomain(it) }
    }

}