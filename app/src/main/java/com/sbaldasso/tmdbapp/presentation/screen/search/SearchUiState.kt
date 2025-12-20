package com.sbaldasso.tmdbapp.presentation.screen.search

import com.sbaldasso.tmdbapp.domain.model.Movie

data class SearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
    val hasSearched: Boolean = false
)