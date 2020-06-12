package com.asiauniv.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {
    @GET("new_book")
    fun getNewBooks(): Call<ArrayList<Book>>

    @GET("theme_book")
    fun getThemeBooks(): Call<ArrayList<Book>>

    @GET("search/{book_name}")
    fun searchBooks(@Path("book_name") bookName: String): Call<ArrayList<Book>>

    @GET("books/{book_id}")
    fun getBookDetail(@Path("book_id") bookId: Int): Call<BookDetail>
}