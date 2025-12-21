package com.sbaldasso.tmdbapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sbaldasso.tmdbapp.data.local.dao.MovieDao
import com.sbaldasso.tmdbapp.data.local.mapper.toDomain
import com.sbaldasso.tmdbapp.data.local.mapper.toEntity
import com.sbaldasso.tmdbapp.data.paging.MoviePagingSource
import com.sbaldasso.tmdbapp.data.remote.api.TMDBApiService
import com.sbaldasso.tmdbapp.data.remote.mapper.toDomain
import com.sbaldasso.tmdbapp.domain.model.Movie
import com.sbaldasso.tmdbapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TMDBApiService,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> {
        return try {
            val response = apiService.getPopularMovies(page)
            val movies = response.results.map { it.toDomain() }

            val entities = movies.map { it.toEntity(page) }
            movieDao.insertMovies(entities)

            Result.success(movies)
        } catch (e: Exception) {
            try {
                val cachedMovies = movieDao.getMoviesByPage(page).map { it.toDomain() }
                if (cachedMovies.isNotEmpty()) {
                    Result.success(cachedMovies)
                } else {
                    Result.failure(e)
                }
            } catch (cacheException: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            val movieDto = apiService.getMovieDetails(movieId)
            val movie = movieDto.toDomain()

            movieDao.insertMovie(movie.toEntity(page = 0))

            Result.success(movie)
        } catch (e: Exception) {
            try {
                val cachedMovie = movieDao.getMovieById(movieId)
                if (cachedMovie != null) {
                    Result.success(cachedMovie.toDomain())
                } else {
                    Result.failure(e)
                }
            } catch (cacheException: Exception) {
                Result.failure(e)
            }
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

    override fun getPopularMoviesFlow(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPopularMoviesPaging(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { MoviePagingSource(apiService) }
        ).flow
    }
}