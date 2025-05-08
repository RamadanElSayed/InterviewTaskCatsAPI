package com.ramadan.readybackendproject
import com.ramadan.readybackendproject.domain.model.ApiError
import com.ramadan.readybackendproject.domain.model.BaseResult
import com.ramadan.readybackendproject.domain.model.CatImage
import com.ramadan.readybackendproject.domain.repository.CatRepository
import com.ramadan.readybackendproject.domain.usecase.GetCatImagesUseCase
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
class GetCatImagesUseCaseTest {

    private lateinit var useCase: GetCatImagesUseCase
    private val repository: CatRepository = mockk()

    @Before
    fun setup() {
        useCase = GetCatImagesUseCase(repository)
    }

    @Test
    fun `invoke returns success result from repository`() = runTest {
        // Arrange
        val catImages = listOf(
            CatImage("id1", "url1", 100, 100),
            CatImage("id2", "url2", 200, 200)
        )
        val successFlow = flowOf(BaseResult.Success(catImages))
        every { repository.getCatImages(10) } returns successFlow

        // Act
        val result = useCase(10).toList()

        // Assert
        assertEquals(1, result.size)
        assertTrue(result[0] is BaseResult.Success)
        assertEquals(catImages, (result[0] as BaseResult.Success).data)

        // Verify repository was called with the correct parameter
        verify { repository.getCatImages(10) }
    }

    @Test
    fun `invoke returns loading state from repository`() = runTest {
        // Arrange
        val loadingFlow = flowOf(BaseResult.Loading)
        every { repository.getCatImages(10) } returns loadingFlow

        // Act
        val result = useCase(10).toList()

        // Assert
        assertEquals(1, result.size)
        assertTrue(result[0] is BaseResult.Loading)

        // Verify repository was called
        verify { repository.getCatImages(10) }
    }

    @Test
    fun `invoke returns error state from repository`() = runTest {
        // Arrange
        val error = ApiError.Network("Network error")
        val errorFlow = flowOf(BaseResult.Error(error))
        every { repository.getCatImages(10) } returns errorFlow

        // Act
        val result = useCase(10).toList()

        // Assert
        assertEquals(1, result.size)
        assertTrue(result[0] is BaseResult.Error)
        assertEquals(error, (result[0] as BaseResult.Error).error)

        // Verify repository was called
        verify { repository.getCatImages(10) }
    }

    @Test
    fun `invoke uses default parameter when not provided`() = runTest {
        // Arrange
        val catImages = listOf(CatImage("id1", "url1", 100, 100))
        val successFlow = flowOf(BaseResult.Success(catImages))
        every { repository.getCatImages(any()) } returns successFlow

        // Act
        val result = useCase().toList() // No parameter provided

        // Assert
        assertEquals(1, result.size)
        assertTrue(result[0] is BaseResult.Success)

        // Verify repository was called with default limit value
        verify { repository.getCatImages(DEFAULT_LIMIT) } // Assuming DEFAULT_LIMIT is the constant in your use case
    }
}
