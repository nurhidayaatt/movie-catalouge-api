package com.example.moviecatalougeapi.ui.favorite.tvshowfavorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDao
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDatabase
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteRepository
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TvShowFavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val tvFavoriteDao: TvFavoriteDao = TvFavoriteDatabase.getInstance(application).tvFavoriteDao()
    private val repository: TvFavoriteRepository =
        TvFavoriteRepository(
            tvFavoriteDao
        )

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private var _tvs : LiveData<List<ResultTv>> = repository.allTvFavorites
    val tvs : LiveData<List<ResultTv>> get() = _tvs

    fun deleteFavorite(data: ResultTv) {
        scope.launch {
            repository.delete(data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
