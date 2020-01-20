package com.example.moviecatalougeapi.ui.search

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalougeapi.BuildConfig
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.api.ApiService
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDao
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDatabase
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteRepository
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDao
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDatabase
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteRepository
import com.example.moviecatalougeapi.data.model.movie.MovieList
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import com.example.moviecatalougeapi.data.model.tv.TvList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

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

    private val _listMovie = MutableLiveData<MovieList>()
    private val _movieItem = MutableLiveData<ArrayList<ResultMovie>>()
    val movieItem: LiveData<ArrayList<ResultMovie>> get() = _movieItem

    private val _listTv = MutableLiveData<TvList>()
    private val _tvItem = MutableLiveData<ArrayList<ResultTv>>()
    val tvItem: LiveData<ArrayList<ResultTv>> get() = _tvItem

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private val _favoriteMovie = MutableLiveData<List<ResultMovie>>()
    val favoriteMovie : LiveData<List<ResultMovie>> get() = _favoriteMovie

    private val _favoriteTv = MutableLiveData<List<ResultTv>>()
    val favoriteTv : LiveData<List<ResultTv>> get() = _favoriteTv

    private var job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    fun getMovieFavorite(search: String) {
        scope.launch {
            _favoriteMovie.postValue(movieRepository.searchMovieFavorite(search))
        }
    }

    fun getTvFavorite(search: String) {
        scope.launch {
            _favoriteTv.postValue(tvRepository.searchTvFavorite(search))
        }
    }

    fun getMovie(language: String, search: String) {
        val item = ArrayList<ResultMovie>()
        _response.value = null

        scope.launch {
            try {
                val result: MovieList = ApiService.retrofitService.searchMovie(BuildConfig.TMDB_API_KEY, language, search)

                if (result.resultMovies.isNotEmpty()) {
                    _listMovie.value = result

                    for (i in 0 until _listMovie.value!!.resultMovies.size) {
                        val resultMovie =
                            ResultMovie(
                                _listMovie.value!!.resultMovies[i].id,
                                _listMovie.value!!.resultMovies[i].overview,
                                _listMovie.value!!.resultMovies[i].backdropPath,
                                _listMovie.value!!.resultMovies[i].posterPath,
                                _listMovie.value!!.resultMovies[i].releaseDate,
                                _listMovie.value!!.resultMovies[i].title,
                                _listMovie.value!!.resultMovies[i].voteAverage,
                                _listMovie.value!!.resultMovies[i].voteCount
                            )

                        item.add(resultMovie)
                    }
                    _movieItem.postValue(item)
                }else {
                    _movieItem.postValue(null)
                }
            }catch (t: Throwable) {
                _response.value = t.localizedMessage
            }
        }
    }

    fun getTv(language: String, search: String) {
        val item = ArrayList<ResultTv>()
        _response.value = null

        scope.launch {
            try {
                val result: TvList = ApiService.retrofitService.searchTV(BuildConfig.TMDB_API_KEY, language, search)

                if (result.results.isNotEmpty()) {
                    _listTv.value = result

                    for (i: Int in _listTv.value!!.results.indices) {
                        val resultMovie =
                            ResultTv(
                                _listTv.value!!.results[i].id,
                                _listTv.value!!.results[i].overview,
                                _listTv.value!!.results[i].backdropPath,
                                _listTv.value!!.results[i].posterPath,
                                _listTv.value!!.results[i].firstAirDate,
                                _listTv.value!!.results[i].name,
                                _listTv.value!!.results[i].voteAverage,
                                _listTv.value!!.results[i].voteCount
                            )

                        item.add(resultMovie)
                    }
                    _tvItem.postValue(item)
                }else {
                    _tvItem.postValue(null)
                }
            }catch (t: Throwable) {
                _response.value = t.localizedMessage
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}