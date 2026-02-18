package com.sbaldasso.tmdbapp.presentation.screen.home

import com.sbaldasso.tmdbapp.domain.model.Movie

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
    val isLoadingMore: Boolean = false,
    val currentPage: Int = 1,
    val loadMoreError: String? = null,
    val canLoadMore: Boolean = true
)