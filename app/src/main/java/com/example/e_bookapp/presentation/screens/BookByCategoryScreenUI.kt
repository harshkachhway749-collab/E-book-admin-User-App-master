package com.example.e_bookapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.e_bookapp.data.BookModels
import com.example.e_bookapp.presentation.navigation.Routes
import com.example.e_bookapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookByCategoryScreenUI(
    viewModel: MainViewModel = hiltViewModel(),
    CategoryName: String,
    navController: NavController
) {
    val state = viewModel.getallbooksbycategorystate.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getAllBooksByCategory(CategoryName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6D4627)) // Background color for the screen
    ) {
        // Add Spacer to create space at the top
        Spacer(modifier = Modifier.height(36.dp))

        Scaffold(
            topBar = {
                TopAppBar(
                    windowInsets = WindowInsets(top = 0.dp, left = 30.dp, right = 30.dp), // Reduced top inset
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = CategoryName,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 30.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0F4559),
                        titleContentColor = Color.White
                    ),
                    modifier = Modifier

                        .clip(CircleShape)
                        .padding(start = 2.dp,end=2.dp)

                )
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != "" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                    }
                }
                else -> {
                    BookList(
                        books = state.data,
                        modifier = Modifier
                            .background(Color(0xFF6D4627))
                            .padding(paddingValues)
                            .padding(top = 16.dp) // Additional padding to avoid overlap
                    ,navController
                    )
                }
            }
        }
    }
}

@Composable
fun BookList(
    books: List<BookModels>,
    modifier: Modifier = Modifier,
    navController: NavController

) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(books) { book ->
            BookItem(book = book,navController)
        }
    }
}

@Composable
fun BookItem(book: BookModels, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                navController.navigate(Routes.PdfViewer(pdfUrl = book.BookUrl.toString()))

            }
,
        elevation = CardDefaults.cardElevation(defaultElevation = 200.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book cover image
            Image(
                painter = rememberAsyncImagePainter(book.BookImageUrl),
                contentDescription = "${book.BookName} cover",
                modifier = Modifier
                    .size(160.dp)
                    .padding(end = 16.dp).shadow(40.dp),
                contentScale = ContentScale.FillBounds
            )
            // Book details
            Column {
                Text(
                    text = book.BookName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp
                )
                Text(
                    text = "by ${book.BookAuthor}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 24.sp,
                    color = Color.Gray,


                )
            }
        }
    }
}