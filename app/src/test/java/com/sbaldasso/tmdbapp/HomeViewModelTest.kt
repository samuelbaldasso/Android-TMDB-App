package com.sbaldasso.tmdbapp

import app.cash.turbine.test
import com.sbaldasso.tmdbapp.domain.model.Movie
import com.sbaldasso.tmdbapp.domain.usecase.GetPopularMoviesUseCase
import com.sbaldasso.tmdbapp.presentation.screen.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPopularMoviesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMovies should update state with movies on success`() = runTest {
        // Given
        val movies = listOf(
            Movie(
                id = 1,
                title = "Test Movie",
                overview = "Test overview",
                posterPath = "/test.jpg",
                backdropPath = "/backdrop.jpg",
                voteAverage = 8.5,
                releaseDate = "2024-01-01",
                popularity = 100.0
            )
        )
        coEvery { getPopularMoviesUseCase(1) } returns Result.success(movies)

        // When
        viewModel = HomeViewModel(getPopularMoviesUseCase)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(movies, state.movies)
            assertNull(state.error)
        }
    }

    @Test
    fun `loadMovies should update state with error on failure`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { getPopularMoviesUseCase(1) } returns Result.failure(Exception(errorMessage))

        // When
        viewModel = HomeViewModel(getPopularMoviesUseCase)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.movies.isEmpty())
            assertEquals(errorMessage, state.error)
        }
    }
}