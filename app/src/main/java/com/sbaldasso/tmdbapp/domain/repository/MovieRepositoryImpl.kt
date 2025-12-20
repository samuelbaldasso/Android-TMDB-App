package com.sbaldasso.tmdbapp.domain.repository

import com.sbaldasso.tmdbapp.data.remote.api.TMDBApiService
import com.sbaldasso.tmdbapp.data.remote.mapper.toDomain
import com.sbaldasso.tmdbapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TMDBApiService
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> {
        return try {
            val response = apiService.getPopularMovies(page)
            val movies = response.results.map { it.toDomain() }
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            val movieDto = apiService.getMovieDetails(movieId)
            Result.success(movieDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Result<List<Movie>> {
        return try {
            val response = apiService.searchMovies(query, page)
            val movies = response.results.map { it.toDomain() }
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getPopularMoviesFlow(): Flow<List<Movie>> = flow {
        val result = getPopularMovies(1)
        result.getOrNull()?.let { emit(it) }
    }
}