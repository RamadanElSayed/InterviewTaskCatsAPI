package com.ramadan.readybackendproject.data.repository

import com.ramadan.readybackendproject.data.api.CatApiService
import com.ramadan.readybackendproject.data.mapper.CatImageDtoMapper
import com.ramadan.readybackendproject.domain.model.BaseResult
import com.ramadan.readybackendproject.domain.model.CatImage
import com.ramadan.readybackendproject.domain.repository.CatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CatRepositoryImpl @Inject constructor(
    private val apiService: CatApiService,
    private val mapper: CatImageDtoMapper,
) : CatRepository {

    override fun getCatImages(limit: Int): Flow<BaseResult<List<CatImage>>> = flow {
        emit(BaseResult.Loading)

        try {
            val response = apiService.getCatImages(limit)

            if (response.isSuccessful) {
                val catImagesDto = response.body()

                if (catImagesDto.isNullOrEmpty()) {
                    emit(BaseResult.Error(message = "No cat images found", code = 203))
                } else {
                    val domainModels = mapper.mapToDomainList(catImagesDto)
                    emit(BaseResult.Success(domainModels))
                }
            } else {
                emit(
                    BaseResult.Error(
                        message = response.message() ?: "API error occurred",
                        code = response.code()
                    )
                )
            }
        } catch (e: HttpException) {
            emit(
                BaseResult.Error(
                    message = e.message(),
                    code = e.code()

                )
            )
        } catch (e: IOException) {
            emit(BaseResult.Error(message = e.message ?: "Network error occurred", code = 203))

        } catch (e: Exception) {
            emit(BaseResult.Error(message = e.message ?: "Unknown error occurred", code = 203))
        }
    }

    override fun refreshCatImages(limit: Int): Flow<BaseResult<List<CatImage>>> = flow {
        emit(BaseResult.Loading)

        try {
            val response = apiService.getCatImages(limit = limit)

            if (response.isSuccessful) {
                val catImagesDto = response.body()

                if (catImagesDto.isNullOrEmpty()) {
                    emit(BaseResult.Error(message = "No cat images found", code = 203))

                } else {
                    val domainModels = mapper.mapToDomainList(catImagesDto)
                    emit(BaseResult.Success(domainModels))
                }
            } else {
                emit(
                    BaseResult.Error(message = "API error occurred", code = 203)
                )
            }
        } catch (e: HttpException) {
            emit(
                BaseResult.Error(
                    message = e.message(),
                    code = e.code()
                )
            )

        } catch (e: IOException) {
            emit(
                BaseResult.Error(message = "Network error occurred", code = 203)
            )
        } catch (e: Exception) {
            emit(
                BaseResult.Error(message = "Unknown error occurred", code = 203)
            )
        }
    }
}