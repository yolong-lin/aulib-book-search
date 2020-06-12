package com.asiauniv.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchedBookActivity : AppCompatActivity() {

    lateinit var recView_newBook: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recycler_book)

        // BookAdapter
        val adapter = BookAdapter(this)
        // Books Array
        var books = ArrayList<Book>()
        // Recycler View
        recView_newBook = findViewById(R.id.recView_newBook)
        recView_newBook.layoutManager = LinearLayoutManager(this)
        recView_newBook.adapter = adapter

        val retrofit = MyRetrofit.instant
        val service = retrofit.create(Service::class.java)
        service.searchBooks(intent.getStringExtra("query")!!)
            .enqueue(object: Callback<ArrayList<Book>> {
                override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {
                    Log.d("MainActivity", "Fail~~")
                    Log.d("MainActivity", t.toString())
                }

                override fun onResponse(
                    call: Call<ArrayList<Book>>,
                    response: Response<ArrayList<Book>>
                ) {
                    val bookBase = response.body()
                    Log.d("MainActivity", response.body().toString())
                    adapter.setBooks(bookBase!!)
                }

            })

    }
}
