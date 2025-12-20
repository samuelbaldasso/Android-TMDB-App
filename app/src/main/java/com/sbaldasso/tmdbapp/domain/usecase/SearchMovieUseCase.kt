package com.sbaldasso.tmdbapp.domain.usecase

import com.sbaldasso.tmdbapp.domain.model.Movie
import com.sbaldasso.tmdbapp.domain.repository.MovieRepository
import com.sbaldasso.tmdbapp.domain.usecase.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

data class SearchMoviesParams(
    val query: String,
    val page: Int = 1
)

class SearchMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<SearchMoviesParams, List<Movie>>(dispatcher) {

    override suspend fun execute(parameters: SearchMoviesParams): Result<List<Movie>> {
        return movieRepository.searchMovies(
            query = parameters.query,
            page = parameters.page
        )
    }
}