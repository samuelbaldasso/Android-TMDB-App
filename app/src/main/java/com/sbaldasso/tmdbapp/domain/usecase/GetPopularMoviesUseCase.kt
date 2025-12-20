package com.sbaldasso.tmdbapp.domain.usecase

import com.sbaldasso.tmdbapp.domain.model.Movie
import com.sbaldasso.tmdbapp.domain.repository.MovieRepository
import com.sbaldasso.tmdbapp.domain.usecase.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<Int, List<Movie>>(dispatcher) {

    override suspend fun execute(parameters: Int): Result<List<Movie>> {
        return movieRepository.getPopularMovies(page = parameters)
    }
}