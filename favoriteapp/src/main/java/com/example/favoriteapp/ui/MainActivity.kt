package com.example.favoriteapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favoriteapp.R
import com.example.favoriteapp.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.elevation = 0f

        val sectionsPagerAdapter : SectionsPagerAdapter? = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }
}
