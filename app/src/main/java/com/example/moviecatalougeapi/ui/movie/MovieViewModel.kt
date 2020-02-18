package com.example.moviecatalougeapi.ui.movie

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviecatalougeapi.data.api.ApiClient
import com.example.moviecatalougeapi.data.model.movie.MovieList
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import com.example.moviecatalougeapi.data.repository.MovieDataSource
import com.example.moviecatalougeapi.data.repository.MovieDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MovieViewModel : ViewModel() {

    private val _listMovie = MutableLiveData<MovieList>()
    private val _movieItem = MutableLiveData<ArrayList<ResultMovie>>()
    val movieItem: LiveData<ArrayList<ResultMovie>> get() = _movieItem

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private var job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    fun getMovie(language: String) {
        val item = ArrayList<ResultMovie>()
        _response.value = null

        scope.launch {
            try {
                val result: MovieList = ApiClient.getClient().getMovie(language)

                if (result.resultMovies.isNotEmpty()) {
                    _listMovie.value = result

                    for (i in _listMovie.value!!.resultMovies.indices) {
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
