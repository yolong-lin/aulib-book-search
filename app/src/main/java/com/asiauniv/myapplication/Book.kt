package com.asiauniv.myapplication

import com.google.gson.annotations.SerializedName

data class Book (

    @SerializedName("name")
    val name : String,
    @SerializedName("author")
    val author : String,
    @SerializedName("book_id")
    val book_id : Int,
    @SerializedName("photo_url")
    val photo_url : String

)


data class BookDetail (

    @SerializedName("title") val title : String,
    @SerializedName("author") val author : String,
    @SerializedName("publishers") val publishers : String,
    @SerializedName("publication_year") val publication_year : String,
    @SerializedName("isbn") val isbn : String,
    @SerializedName("photo") val photo : String,
    @SerializedName("collections") val collections : List<Collections>
)
data class Collections (

    @SerializedName("locate") val locate : String,
    @SerializedName("call_number") val call_number : String,
    @SerializedName("status") val status : String,
    @SerializedName("item_class") val item_class : String
)

