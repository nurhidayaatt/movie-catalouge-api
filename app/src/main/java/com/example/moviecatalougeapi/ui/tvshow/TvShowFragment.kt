package com.example.moviecatalougeapi.ui.tvshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.adapter.TvAdapter
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import com.example.moviecatalougeapi.ui.detail.DetailActivity
import com.example.moviecatalougeapi.ui.search.SearchActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.tv_show_fragment.*
import java.util.*

class TvShowFragment : Fragment() {

    private lateinit var tvAdapter: TvAdapter
    private lateinit var viewModel: TvShowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tv_show_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getTv()

        fab_search.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra(SearchActivity.EXTRA, SearchActivity.TV)
            startActivity(intent)
        }
    }

    private fun getTv() {
        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(TvShowViewModel::class.java)
        if (Locale.getDefault().language.toString() == "in") {
            viewModel.getTv("id")
        } else {
            viewModel.getTv("en-US")
        }
        showTv()
    }

    private fun showTv() {
        tvAdapter = TvAdapter()
        tvAdapter.notifyDataSetChanged()

        recycler_tv.layoutManager = LinearLayoutManager(activity)
        recycler_tv.adapter = tvAdapter

        viewModel.response.observe(this, Observer {response ->
            if (response != null) {
                error.text = response
                Snackbar.make(coordinator, getString(R.string.error), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.try_again)) { getTv() }.show()
                showLoading(false)
            } else {
                error.text = null
                viewModel.tvItem.observe(this, Observer { tvItem ->
                    if (tvItem != null) {
                        tvAdapter.setData(tvItem)
                        showLoading(false)
                    }
                })
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

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}
