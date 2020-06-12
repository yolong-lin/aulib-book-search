package com.asiauniv.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThemeBookFragment : Fragment() {

    private val BASE_URL = "http://aulib.asia.edu.tw/"

    lateinit var recView_newBook: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recycler_book, container, false)

        // BookAdapter
        val adapter = BookAdapter(inflater.context)
        // Books Array
        var books: ArrayList<Book>
        // Recycler View
        recView_newBook = view.findViewById(R.id.recView_newBook)
        recView_newBook.layoutManager = LinearLayoutManager(inflater.context)
        recView_newBook.adapter = adapter



        // Networking: Start
        val retrofit = MyRetrofit.instant

        val service = retrofit.create(Service::class.java)
        val call = service.getThemeBooks()

        call.enqueue(object : Callback<ArrayList<Book>> {
            override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {
                Log.d("MainActivity", "Fail~~")
                Log.d("MainActivity", t.message!!)
            }

            override fun onResponse(
                call: Call<ArrayList<Book>>,
                response: Response<ArrayList<Book>>
            ) {
                val bookBase = response.body()
                Log.d("MainActivity", bookBase.toString() )
//                books = ArrayList(bookBase?.book!!)
                adapter.setBooks(bookBase!!)
            }

        })
        // Networking: End
        return view
    }
}