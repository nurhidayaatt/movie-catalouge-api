package com.example.moviecatalougeapi.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDao
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDatabase
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteRepository
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDao
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDatabase
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteRepository
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val movieFavoriteDao: MovieFavoriteDao = MovieFavoriteDatabase.getInstance(application).movieFavoriteDao()
    private val movieRepository: MovieFavoriteRepository =
        MovieFavoriteRepository(
            movieFavoriteDao
        )
    private val tvFavoriteDao: TvFavoriteDao = TvFavoriteDatabase.getInstance(application).tvFavoriteDao()
    private val tvRepository: TvFavoriteRepository =
        TvFavoriteRepository(
            tvFavoriteDao
        )

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _movieId = MutableLiveData<Int>()
    val movieId : LiveData<Int> get() = _movieId
    private val _tvId = MutableLiveData<Int>()
    val tvId : LiveData<Int> get() = _tvId

    fun getFavoriteMovieId(id: Int) {
        scope.launch {
            _movieId.postValue(movieRepository.movieFaviriteId(id))
        }
    }

    fun getFavoriteTvId(id: Int) {
        scope.launch {
            _tvId.postValue(tvRepository.tvFavoriteId(id))
        }
    }

    fun addFavorite(data: ResultMovie) {
        scope.launch {
            movieRepository.insert(data)
        }
    }

    fun addFavorite(data: ResultTv) {
        scope.launch {
            tvRepository.insert(data)
        }
    }

    fun deleteFavorite(data: ResultMovie) {
        scope.launch {
            movieRepository.delete(data)
        }
    }

    fun deleteFavorite(data: ResultTv) {
        scope.launch {
            tvRepository.delete(data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}