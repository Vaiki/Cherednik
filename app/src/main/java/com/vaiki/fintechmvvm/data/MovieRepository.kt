package com.vaiki.fintechmvvm.data


import com.vaiki.fintechmvvm.data.remote.MovieRemoteDataSource
import com.vaiki.fintechmvvm.model.films.Films
import com.vaiki.fintechmvvm.model.films.Result
import com.vaiki.fintechmvvm.model.movie.MovieDescription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
) {
    suspend fun fetchTopMovies(): Flow<Result<Films>?> {
        return flow {
            emit(Result.onLoading())
            val result = movieRemoteDataSource.fetchTopMovies()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchMovie(id: Int): Flow<Result<MovieDescription>> {
        return flow {
            emit(Result.onLoading())
            emit(movieRemoteDataSource.fetchMovie(id))
        }.flowOn(Dispatchers.IO)
    }
}