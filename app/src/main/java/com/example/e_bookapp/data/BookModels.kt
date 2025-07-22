package com.example.e_bookapp.data

import com.google.firebase.database.PropertyName

data class BookModels(
    @PropertyName("id")
    val id: String = "",
    @PropertyName("bookCategory")
    val BookCategory: String="",
    @PropertyName("bookName")
    val BookName:String="",
    @PropertyName("bookImageUrl")
    val BookImageUrl: String="",
    @PropertyName("bookUrl")
    val BookUrl: String="",
    @PropertyName("bookAuthor")
    val BookAuthor: String=""
)
