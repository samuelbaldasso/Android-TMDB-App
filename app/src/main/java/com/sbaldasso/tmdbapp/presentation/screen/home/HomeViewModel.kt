package com.sbaldasso.tmdbapp.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbaldasso.tmdbapp.domain.usecase.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getPopularMoviesUseCase(
                1
            )
                .onSuccess { movies ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            movies = movies,
                            currentPage = 1,
                            canLoadMore = movies.isNotEmpty()
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Erro desconhecido"
                        )
                    }
                }
        }
    }

    fun loadMoreMovies() {
        val currentState = _uiState.value
        if (currentState.isLoadingMore || !currentState.canLoadMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }

            val nextPage = currentState.currentPage + 1
            getPopularMoviesUseCase(nextPage)
                .onSuccess { newMovies ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            movies = it.movies + newMovies,
                            currentPage = nextPage,
                            canLoadMore = newMovies.isNotEmpty()
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(isLoadingMore = false)
                    }
                }
        }
    }

    fun retry() {
        loadMovies()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}