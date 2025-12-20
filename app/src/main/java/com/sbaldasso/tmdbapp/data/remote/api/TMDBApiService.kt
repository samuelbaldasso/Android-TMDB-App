package com.sbaldasso.tmdbapp.data.remote.api

import com.sbaldasso.tmdbapp.data.remote.dto.MovieDto
import com.sbaldasso.tmdbapp.data.remote.dto.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "pt-BR"
    ): MoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "pt-BR"
    ): MovieDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "pt-BR"
    ): MoviesResponse
}