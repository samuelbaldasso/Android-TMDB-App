package com.sbaldasso.tmdbapp.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbaldasso.tmdbapp.domain.usecase.SearchMoviesParams
import com.sbaldasso.tmdbapp.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500) // Aguarda 500ms após o usuário parar de digitar
                .filter { it.length >= 3 } // Busca apenas se tiver 3+ caracteres
                .collect { query ->
                    searchMovies(query)
                }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        _searchQuery.value = query

        if (query.isEmpty()) {
            _uiState.update {
                it.copy(
                    movies = emptyList(),
                    error = null,
                    hasSearched = false
                )
            }
        }
    }

    private fun searchMovies(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, error = null) }

            searchMoviesUseCase(SearchMoviesParams(query = query, page = 1))
                .onSuccess { movies ->
                    _uiState.update {
                        it.copy(
                            isSearching = false,
                            movies = movies,
                            hasSearched = true
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isSearching = false,
                            error = exception.message ?: "Erro na busca",
                            hasSearched = true
                        )
                    }
                }
        }
    }

    fun retry() {
        if (_uiState.value.query.isNotEmpty()) {
            searchMovies(_uiState.value.query)
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _uiState.update {
            SearchUiState()
        }
    }
}