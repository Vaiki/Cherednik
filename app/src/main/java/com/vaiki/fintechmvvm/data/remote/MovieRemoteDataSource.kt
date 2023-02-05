package com.vaiki.fintechmvvm.data.remote

import com.vaiki.fintechmvvm.model.films.Films
import com.vaiki.fintechmvvm.model.films.Error
import com.vaiki.fintechmvvm.model.films.Result
import com.vaiki.fintechmvvm.model.movie.MovieDescription
import com.vaiki.fintechmvvm.network.MovieService
import com.vaiki.fintechmvvm.util.ErrorUtils
import retrofit2.Retrofit
import retrofit2.Response
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchTopMovies(): Result<Films> {
        val movieService = retrofit.create(MovieService::class.java);
        return getResponse(
            request = { movieService.getTopFilms(2) },
            defaultErrorMessage = "Error fetching Movie list"
        )
    }

    suspend fun fetchMovie(id: Int): Result<MovieDescription> {
        val movieService = retrofit.create(MovieService::class.java);
        return getResponse(
            request = { movieService.getMovie(id) },
            defaultErrorMessage = "Error fetching Movie Description"
        )
    }

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.onSuccess(result.body())
            } else {
                val errorResponse: Error? = ErrorUtils.parseError(result, retrofit)
                Result.onFailure(
                    errorResponse?.status_message ?: defaultErrorMessage,
                    errorResponse
                )
            }
        } catch (e: Throwable) {
            Result.onFailure("Unknown Error", null)
        }
    }
}