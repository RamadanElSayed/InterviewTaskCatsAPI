package com.ramadan.readybackendproject

import com.ramadan.readybackendproject.domain.model.ApiError
import com.ramadan.readybackendproject.domain.model.BaseResult
import com.ramadan.readybackendproject.domain.model.CatImage
import com.ramadan.readybackendproject.domain.repository.CatRepository
import com.ramadan.readybackendproject.domain.usecase.GetCatImagesUseCase.Companion.DEFAULT_LIMIT
import com.ramadan.readybackendproject.domain.usecase.RefreshCatImagesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RefreshCatImagesUseCaseTest {

    private lateinit var useCase: RefreshCatImagesUseCase
    private val repository: CatRepository = mockk()

    @Before
    fun setup() {
        useCase = RefreshCatImagesUseCase(repository)
    }

    @Test
    fun `invoke returns success result from repository`() = runTest {
        // Arrange
        val catImages = listOf(
            CatImage("id3", "url3", 300, 300),
            CatImage("id4", "url4", 400, 400)
        )
        val successFlow = flowOf(BaseResult.Success(catImages))
        every { repository.refreshCatImages(10) } returns successFlow

        // Act
        val result = useCase(10).toList()

        // Assert
        assertEquals(1, result.size)
        assertTrue(result[0] is BaseResult.Success)
        assertEquals(catImages, (result[0] as BaseResult.Success).data)

        // Verify repository method was called with correct parameter
        verify { repository.refreshCatImages(10) }
    }

    @Test
    fun `invoke returns loading state from repository`() = runTest {
        // Arrange
        val loadingFlow = flowOf(BaseResult.Loading)
        every { repository.refreshCatImages(10) } returns loadingFlow

        // Act
        val result = useCase(10).toList()

        // Assert
        assertEquals(1, result.size)
        assertTrue(result[0] is BaseResult.Loading)

        // Verify repository was called
        verify { repository.refreshCatImages(10) }
    }

    @Test
    fun `invoke returns error state from repository`() = runTest {
        // Arrange
        val error = ApiError.ServerError("Server error", 500)
        val errorFlow = flowOf(BaseResult.Error(error))
        every { repository.refreshCatImages(10) } returns errorFlow

        // Act
        val result = useCase(10).toList()

        // Assert
        assertEquals(1, result.size)
        assertTrue(result[0] is BaseResult.Error)
        assertEquals(error, (result[0] as BaseResult.Error).error)

        // Verify repository was called
        verify { repository.refreshCatImages(10) }
    }

    @Test
    fun `invoke uses default parameter when not provided`() = runTest {
        // Arrange
        val catImages = listOf(CatImage("id3", "url3", 300, 300))
        val successFlow = flowOf(BaseResult.Success(catImages))
        every { repository.refreshCatImages(any()) } returns successFlow

        // Act
        val result = useCase().toList() // No parameter provided

        // Assert
        assertEquals(1, result.size)
        assertTrue(result[0] is BaseResult.Success)

        // Verify repository was called with default limit value
        verify { repository.refreshCatImages(DEFAULT_LIMIT) } // Assuming DEFAULT_LIMIT is the constant in your use case
    }

    @Test
    fun `invoke handles complex flow with multiple emissions`() = runTest {
        // Arrange
        val catImages = listOf(CatImage("id3", "url3", 300, 300))
        val complexFlow = flowOf(
            BaseResult.Loading,
            BaseResult.Success(catImages)
        )
        every { repository.refreshCatImages(10) } returns complexFlow

        // Act
        val results = useCase(10).toList()

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is BaseResult.Loading)
        assertTrue(results[1] is BaseResult.Success)
        assertEquals(catImages, (results[1] as BaseResult.Success).data)
    }

}