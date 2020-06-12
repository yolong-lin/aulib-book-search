package com.asiauniv.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookAdapter: RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private val imageBaseUrl = "http://aulib.asia.edu.tw/webpac/"

    private var books = ArrayList<Book>()
    private var context: Context

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            book_name.text = books[position].name
            book_author.text = books[position].author

            var imageUrl = books[position].photo_url
            if (!imageUrl.contains("http://")) {
                imageUrl = "${imageBaseUrl}${books[position].photo_url}"
            }

            Glide.with(context)
                .load(imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(book_image)

            book_card.setOnClickListener {
//                Toast.makeText(context, books[position].author, Toast.LENGTH_SHORT).show()

                val retrofit = MyRetrofit.instant
                val service = retrofit.create(Service::class.java)

                service.getBookDetail(books[position].book_id)
                    .enqueue(object: Callback<BookDetail> {
                        override fun onFailure(call: Call<BookDetail>, t: Throwable) {
                            Log.d("BookAdapter", "get book detail fail")
                            Log.d("BookAdapter", t.message.toString())
                        }

                        override fun onResponse(
                            call: Call<BookDetail>,
                            response: Response<BookDetail>
                        ) {
                            Log.d("BookAdapter", response.body().toString())
                            response.body()?.apply {

                                val v = LayoutInflater.from(context).inflate(R.layout.book_detail, null)

                                Glide.with(context)
                                    .load(photo)
                                    .error(R.mipmap.ic_launcher)
                                    .into(v.findViewById(R.id.book_detail_image))

                                v.findViewById<TextView>(R.id.book_detail_name).text = title
                                v.findViewById<TextView>(R.id.book_detail_author).text = author
                                v.findViewById<TextView>(R.id.book_detail_isbn).text = "ISBN: $isbn"
                                v.findViewById<TextView>(R.id.book_detail_publisher).text = "出版社: $publishers"



                                if (collections.size == 0) {
                                    v.findViewById<TextView>(R.id.book_detail_collections_text).apply {
                                        text = "無館藏"
                                    }

                                } else {
                                    var message = ""
                                    for (collection in collections) {
                                        message += "館藏地: ${collection.locate}\n"
                                        message += "索書號: ${collection.call_number}\n"
                                        message += "Status: ${collection.status}\n"
                                        message += "流通類別: ${collection.item_class}\n\n"
                                    }

                                    v.findViewById<TextView>(R.id.book_detail_collections).text = message
                                }




                                AlertDialog.Builder(context)
                                    .setView(v)
                                    .setNegativeButton("Cancel") { dialog, which ->
                                        Log.d("BookAdapter", "")
                                    }
                                    .create()
                                    .show()

                            }
                        }
                    })
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var book_card: RelativeLayout = itemView.findViewById(R.id.book_card)
        var book_name: TextView = itemView.findViewById(R.id.book_name)
        var book_image: ImageView = itemView.findViewById(R.id.book_image)
        var book_author: TextView = itemView.findViewById(R.id.book_author)

    }

    fun setBooks(books: ArrayList<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

}