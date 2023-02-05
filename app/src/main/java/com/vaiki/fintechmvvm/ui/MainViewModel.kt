package com.vaiki.fintechmvvm.ui

import androidx.lifecycle.*
import com.vaiki.fintechmvvm.model.films.Result
import com.vaiki.fintechmvvm.data.MovieRepository
import com.vaiki.fintechmvvm.model.films.Films
import com.vaiki.fintechmvvm.model.movie.MovieDescription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {


    private var _id = MutableLiveData<Int>()
    private val _movieList: MutableLiveData<Result<Films>?> = MutableLiveData<Result<Films>?>()
    val movieList = _movieList
    private val _movie: LiveData<Result<MovieDescription>> = _id.distinctUntilChanged().switchMap {
        liveData {
            movieRepository.fetchMovie(it).onStart {
                emit(Result.onLoading())
            }.collect { emit(it) }
        }
    }
    val movie = _movie

    init {
        fetchMovies()
    }

    fun getMovieDetail(id: Int) {
        _id.value = id
    }

    fun fetchMovies() {
        viewModelScope.launch {
            movieRepository.fetchTopMovies().collect() {
                _movieList.value = it
            }
        }
    }
    }