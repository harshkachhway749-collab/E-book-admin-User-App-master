package com.example.e_bookapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes{

    @Serializable
    object HomeScreen

    @Serializable
    data class BookByCategory(
        val CategoryName: String
    )

    @Serializable
    object AllBooks

    @Serializable
    object BookDetails

    @Serializable
    data class PdfViewer(
        val pdfUrl: String
    )

    @Serializable
    object Login

    @Serializable
    object SignUp

}