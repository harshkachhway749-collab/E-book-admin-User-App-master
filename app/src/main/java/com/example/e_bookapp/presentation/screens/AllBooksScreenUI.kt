package com.example.e_bookapp.presentation.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.Uri
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.e_bookapp.data.BookModels
import com.example.e_bookapp.presentation.navigation.Routes
import com.example.e_bookapp.viewmodel.MainViewModel

@Composable
fun AllBooksScreenUI(viewModel: MainViewModel = hiltViewModel(), navController: NavController) {
    val state = viewModel.getallbooksstate.collectAsState().value
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getAllBooks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6D4627))
            .padding(16.dp)
    ) {
        // 🔍 Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search books...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = CircleShape
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF4AA4EA),
                        strokeWidth = 7.dp,
                        trackColor = Color(0xFF071927),
                        modifier = Modifier.size(150.dp)
                    )
                }
            }

            state.error.isNotEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.error)
                    Log.d("TAG", "AllBooksScreenUI: ${state.error}")
                }
            }

            state.data != null -> {
                val filteredBooks = state.data.filter {
                    it.BookName.startsWith(query, ignoreCase = true)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredBooks) { book ->
                        BookCard(book = book, navController)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

//@Composable
//fun BookCard(book: BookModels,navController: NavController) {
//    val context=LocalContext.current
//    val url=book.BookUrl
//    val intent= Intent(Intent.ACTION_VIEW,android.net.Uri.parse(url))
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(200.dp).clickable{
//                context.startActivity(intent)
//            }, // Fixed height
//        elevation = CardDefaults.cardElevation(8.dp),
//        shape = RoundedCornerShape(12.dp),
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            SubcomposeAsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(book.BookImageUrl)
//                    .crossfade(true)
//                    .build(),
//                contentDescription = book.BookName,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize(),
//                loading = {
//                    CircularProgressIndicator()
//                }
//            )
//
//            // Optional: semi-transparent dark gradient overlay for better text visibility
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(
//                                Color.Transparent,
//                                Color(0x80000000)
//                            )
//                        )
//                    )
//            )
//
//            // Details overlay
//            Column(
//                modifier = Modifier
//                    .align(Alignment.BottomStart)
//                    .padding(12.dp)
//            ) {
//                Text(
//                    text = book.BookName,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//                Text(
//                    text = "Author: ${book.BookAuthor}",
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color.LightGray
//                )
//                Text(
//                    text = "Category: ${book.BookCategory}",
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color.LightGray
//                )
//            }
//        }
//    }
//}

@Composable
fun BookCard(book: BookModels, navController: NavController) {
    val context = LocalContext.current
    val url = book.BookUrl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable {

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(android.net.Uri.parse(url), "application/pdf")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                try {

                    context.startActivity(Intent.createChooser(intent, "Open PDF with"))
                } catch (e: Exception) {

                    Log.e("TAG", "Error opening PDF: ${e.message}")

                }
            },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.BookImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = book.BookName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    CircularProgressIndicator()
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x80000000)
                            )
                        )
                    )
            )

            // Details overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = book.BookName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Author: ${book.BookAuthor}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray
                )
                Text(
                    text = "Category: ${book.BookCategory}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray
                )
            }
        }
    }
}