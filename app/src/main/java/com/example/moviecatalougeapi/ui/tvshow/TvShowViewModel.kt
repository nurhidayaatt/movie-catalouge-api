package com.example.moviecatalougeapi.ui.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalougeapi.data.api.ApiClient
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import com.example.moviecatalougeapi.data.model.tv.TvList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TvShowViewModel : ViewModel() {

    private val _listTv = MutableLiveData<TvList>()
    private val _tvItem = MutableLiveData<ArrayList<ResultTv>>()
    val tvItem: LiveData<ArrayList<ResultTv>> get() = _tvItem

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private var job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    fun getTv(language: String) {
        val item = ArrayList<ResultTv>()
        _response.value = null

        scope.launch {
            try {
                val result: TvList = ApiClient.getClient().getTV(language)

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
