package com.sbaldasso.tmdbapp

import com.sbaldasso.tmdbapp.domain.model.Movie
import com.sbaldasso.tmdbapp.domain.repository.MovieRepository
import com.sbaldasso.tmdbapp.domain.usecase.GetPopularMoviesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPopularMoviesUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetPopularMoviesUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        movieRepository = mockk()
        useCase = GetPopularMoviesUseCase(movieRepository, testDispatcher)
    }

    @Test
    fun `invoke should return success when repository returns movies`() = runTest {
        // Given
        val movies = listOf(
            Movie(1, "Movie 1", "Overview", null, null, 8.0, "2024-01-01", 100.0)
        )
        coEvery { movieRepository.getPopularMovies(1) } returns Result.success(movies)

        // When
        val result = useCase(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(movies, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { movieRepository.getPopularMovies(1) } returns Result.failure(exception)

        // When
        val result = useCase(1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}