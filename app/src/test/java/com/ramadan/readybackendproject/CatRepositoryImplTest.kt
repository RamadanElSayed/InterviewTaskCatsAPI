package com.ramadan.readybackendproject

import com.ramadan.readybackendproject.data.api.CatApiService
import com.ramadan.readybackendproject.data.mapper.CatImageDtoMapper
import com.ramadan.readybackendproject.data.model.CatImageDto
import com.ramadan.readybackendproject.data.repository.CatRepositoryImpl
import com.ramadan.readybackendproject.domain.model.BaseResult
import com.ramadan.readybackendproject.domain.model.CatImage
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class CatRepositoryImplTest {

    private lateinit var repository: CatRepositoryImpl
    private val apiService: CatApiService = mockk()
    private val mapper: CatImageDtoMapper = mockk()

    @Before
    fun setup() {
        repository = CatRepositoryImpl(apiService, mapper)
    }

    @Test
    fun `getCatImages returns Success when API call is successful`() = runTest {
        // Arrange
        val catImageDtoList = listOf(
            CatImageDto("id1", "url1", 100, 100),
            CatImageDto("id2", "url2", 200, 200)
        )
        val domainList = listOf(
            CatImage("id1", "url1", 100, 100),
            CatImage("id2", "url2", 200, 200)
        )

        coEvery { apiService.getCatImages(any()) } returns Response.success(catImageDtoList)
        every { mapper.mapToDomainList(catImageDtoList) } returns domainList

        // Act
        val results = repository.getCatImages(10).toList()

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is BaseResult.Loading)
        assertTrue(results[1] is BaseResult.Success)
        assertEquals(domainList, (results[1] as BaseResult.Success).data)
    }


    @Test
    fun `refreshCatImages returns Success when API call is successful`() = runTest {
        // Arrange
        val catImageDtoList = listOf(
            CatImageDto("id3", "url3", 300, 300),
            CatImageDto("id4", "url4", 400, 400)
        )
        val domainList = listOf(
            CatImage("id3", "url3", 300, 300),
            CatImage("id4", "url4", 400, 400)
        )

        coEvery { apiService.getCatImages(any(), any()) } returns Response.success(catImageDtoList)
        every { mapper.mapToDomainList(catImageDtoList) } returns domainList

        // Act
        val results = repository.refreshCatImages(10).toList()

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is BaseResult.Loading)
        assertTrue(results[1] is BaseResult.Success)
        assertEquals(domainList, (results[1] as BaseResult.Success).data)
    }
}