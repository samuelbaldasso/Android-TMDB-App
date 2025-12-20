package com.sbaldasso.tmdbapp.data.remote.mapper

import com.sbaldasso.tmdbapp.data.remote.dto.MovieDto
import com.sbaldasso.tmdbapp.domain.model.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
        popularity = popularity
    )
}