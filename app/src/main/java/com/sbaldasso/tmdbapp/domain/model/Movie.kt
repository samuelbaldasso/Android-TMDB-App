package com.sbaldasso.tmdbapp.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val releaseDate: String,
    val popularity: Double
) {
    fun getPosterUrl(): String {
        return "https://image.tmdb.org/t/p/w500$posterPath"
    }

    fun getBackdropUrl(): String {
        return "https://image.tmdb.org/t/p/w780$backdropPath"
    }
}