package com.example.e_bookapp.data.model

import com.google.firebase.database.PropertyName

data class BookCategoryModel(
    @PropertyName("id")
    var id: String? = "",

    @PropertyName("categoryName")
    val CategoryName: String = "",

    @PropertyName("categoryImageUrl")
    val CategoryImageUrl: String = ""
)
