package com.example.moviecatalougeapi.ui.favorite.tvshowfavorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.adapter.TvAdapter
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import com.example.moviecatalougeapi.ui.detail.DetailActivity
import com.example.moviecatalougeapi.ui.search.SearchActivity
import kotlinx.android.synthetic.main.tv_show_favorite_fragment.*

class TvShowFavoriteFragment : Fragment() {

    private lateinit var viewModel: TvShowFavoriteViewModel
    private lateinit var tvAdapter: TvAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tv_show_favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab_search.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra(SearchActivity.EXTRA, SearchActivity.TV_FAVORITE)
            startActivity(intent)
        }

        viewModel = ViewModelProviders.of(this).get(TvShowFavoriteViewModel::class.java)

        tvAdapter = TvAdapter()
        tvAdapter.notifyDataSetChanged()

        recycler_tv_favorite.layoutManager = LinearLayoutManager(activity)
        recycler_tv_favorite.adapter = tvAdapter

        viewModel.tvs.observe(this, Observer { data ->
            if (data.isNotEmpty()) {
                fab_search.show()
                error.text = null
                tvAdapter.setData(data as ArrayList<ResultTv>)
            }else {
                fab_search.hide()
                error.text = getString(R.string.data_null)
                tvAdapter.setData()
            }
        })

        tvAdapter.setOnItemClickCallback(object : TvAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultTv) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_TV, data)
                startActivity(intent)
            }

        })
    }

}
