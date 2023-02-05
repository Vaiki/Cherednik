package com.vaiki.fintechmvvm.network

import com.vaiki.fintechmvvm.model.films.Films
import com.vaiki.fintechmvvm.model.movie.MovieDescription
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    suspend fun getTopFilms(
        @Query("page") page: Int,
        @Header("x-api-key") apiKey: String = "bf2053b2-4e82-480b-8dbd-521bcdc34e72"
    ): Response<Films>

    @GET("/api/v2.2/films/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") id: Int,
        @Header("x-api-key") apiKey: String = "bf2053b2-4e82-480b-8dbd-521bcdc34e72"
    ): Response<MovieDescription>
}