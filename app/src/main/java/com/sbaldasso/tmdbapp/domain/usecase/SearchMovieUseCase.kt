package com.sbaldasso.tmdbapp.domain.usecase

import com.sbaldasso.tmdbapp.domain.model.Movie
import com.sbaldasso.tmdbapp.domain.repository.MovieRepository
import com.sbaldasso.tmdbapp.di.IoDispatcher
import com.sbaldasso.tmdbapp.domain.usecase.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

data class SearchMoviesParams(
    val query: String,
    val page: Int = 1
)

class SearchMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<SearchMoviesParams, List<Movie>>(dispatcher) {

    override suspend fun execute(parameters: SearchMoviesParams): Result<List<Movie>> {
        return movieRepository.searchMovies(
            query = parameters.query,
            page = parameters.page
        )
    }
}