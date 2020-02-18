package com.example.moviecatalougeapi.ui.favorite.tvshowfavorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.adapter.TvAdapter
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import com.example.moviecatalougeapi.ui.detail.DetailActivity
import com.example.moviecatalougeapi.ui.search.SearchActivity
import com.example.moviecatalougeapi.util.widget.FavoriteWidget
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

        viewModel = ViewModelProvider(this).get(TvShowFavoriteViewModel::class.java)
        tvAdapter = TvAdapter()
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

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteFavorite(tvAdapter.getData(viewHolder.adapterPosition))
                sendUpdateFavoriteList(requireContext())
            }

        }).attachToRecyclerView(recycler_tv_favorite)

        tvAdapter.setOnItemClickCallback(object : TvAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultTv) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra(DetailActivity.EXTRA_TV, data)
                startActivity(intent)
            }

        })
    }

    private fun sendUpdateFavoriteList(context: Context) {
        val intent = Intent(context, FavoriteWidget::class.java)
        intent.action = FavoriteWidget.UPDATE_WIDGET
        context.sendBroadcast(intent)
    }
}
