package com.example.e_bookapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.example.e_bookapp.presentation.navigation.Routes
import com.example.e_bookapp.viewmodel.MainViewModel
import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.getallcategorystate.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getAllCategory()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x908C7E73)) // Custom background color for the entire screen
    ) {
        // Spacer to create space at the top
        Spacer(modifier = Modifier.height(16.dp))
        var searchQuery by remember { mutableStateOf("") }


        Spacer(modifier = Modifier.height(8.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(),
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                state.data != null -> {

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search Categories...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        singleLine = true,
                        shape = CircleShape

                    )
                    val filteredCategories = state.data.filter {
                        it.CategoryName.contains(searchQuery, ignoreCase = true)
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredCategories) { category ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val scale = if (isPressed) 0.95f else 1f

                            Card(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .scale(scale)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        Log.d("TAG", "CategoryScreen: ${category.CategoryName}")
                                        navController.navigate(
                                            Routes.BookByCategory(
                                                CategoryName = category.CategoryName
                                            )
                                        )
                                    }
                                    .shadow(12.dp, RoundedCornerShape(36.dp)),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(1000000000.dp) ,
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF1A2A44) // Darker card background
                                )
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    SubcomposeAsyncImage(
                                        model = category.CategoryImageUrl,
                                        contentDescription = category.CategoryName,
                                        loading = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    color = Color(0xFF4AA4EA),
                                                    strokeWidth = 4.dp
                                                )
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    // Semi-transparent overlay for text readability
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                brush = Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color(0xAA000000)
                                                    )
                                                )
                                            )
                                    )

                                    Text(
                                        text = category.CategoryName,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 24.sp
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(12.dp)
                                    )
                                }
                            }
                        }
                    }

            }
        }
    }
}