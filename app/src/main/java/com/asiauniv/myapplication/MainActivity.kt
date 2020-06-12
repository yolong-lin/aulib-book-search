package com.asiauniv.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var tabView: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var toolbar: Toolbar
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabView = findViewById(R.id.tabView)
        viewPager = findViewById(R.id.viewPager)
        toolbar = findViewById(R.id.toolbar)

        toolbarSetting()

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment("New Book", NewBookFragment())
        viewPagerAdapter.addFragment("Theme Book", ThemeBookFragment())
        viewPager.adapter = viewPagerAdapter

        tabView.setupWithViewPager(viewPager)

    }

    fun toolbarSetting() {
        toolbar.inflateMenu(R.menu.menu)
        searchView = toolbar.findViewById(R.id.toolbar_search)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()

                val intent = Intent(this@MainActivity, SearchedBookActivity::class.java)
                intent.putExtra("query", query)
                startActivity(intent)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }
}