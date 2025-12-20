package com.sbaldasso.tmdbapp.domain.repository

import com.sbaldasso.tmdbapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<List<Movie>>
    suspend fun getMovieDetails(movieId: Int): Result<Movie>
    suspend fun searchMovies(query: String, page: Int): Result<List<Movie>>
    fun getPopularMoviesFlow(): Flow<List<Movie>>
}