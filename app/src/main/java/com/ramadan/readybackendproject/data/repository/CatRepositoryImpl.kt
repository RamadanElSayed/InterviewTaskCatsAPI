package com.ramadan.readybackendproject.data.repository

import com.ramadan.readybackendproject.data.api.CatApiService
import com.ramadan.readybackendproject.data.mapper.CatImageDtoMapper
import com.ramadan.readybackendproject.domain.model.ApiError
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
                    emit(BaseResult.Error(ApiError.NotFound("No cat images found")))
                } else {
                    val domainModels = mapper.mapToDomainList(catImagesDto)
                    emit(BaseResult.Success(domainModels))
                }
            } else {
                emit(BaseResult.Error(
                    ApiError.ServerError(
                        message = response.message() ?: "API error occurred",
                        code = response.code()
                    )
                ))
            }
        } catch (e: HttpException) {
            emit(BaseResult.Error(
                ApiError.ServerError(
                    message = e.message(),
                    code = e.code()
                )
            ))
        } catch (e: IOException) {
            emit(BaseResult.Error(ApiError.Network(e.message ?: "Network error occurred")))
        } catch (e: Exception) {
            emit(BaseResult.Error(ApiError.Unknown(e.message ?: "Unknown error occurred")))
        }
    }

    override fun refreshCatImages(limit: Int): Flow<BaseResult<List<CatImage>>> = flow {
        emit(BaseResult.Loading)

        try {
            // Add timestamp to force fresh data
            val timestamp = System.currentTimeMillis()
            val response = apiService.getCatImages(limit=limit, timestamp=timestamp)

            if (response.isSuccessful) {
                val catImagesDto = response.body()

                if (catImagesDto.isNullOrEmpty()) {
                    emit(BaseResult.Error(ApiError.NotFound("No cat images found")))
                } else {
                    val domainModels = mapper.mapToDomainList(catImagesDto)
                    emit(BaseResult.Success(domainModels))
                }
            } else {
                emit(BaseResult.Error(
                    ApiError.ServerError(
                        message = response.message() ?: "API error occurred",
                        code = response.code()
                    )
                ))
            }
        } catch (e: HttpException) {
            emit(BaseResult.Error(
                ApiError.ServerError(
                    message = e.message(),
                    code = e.code()
                )
            ))
        } catch (e: IOException) {
            emit(BaseResult.Error(ApiError.Network(e.message ?: "Network error occurred")))
        } catch (e: Exception) {
            emit(BaseResult.Error(ApiError.Unknown(e.message ?: "Unknown error occurred")))
        }
    }
}