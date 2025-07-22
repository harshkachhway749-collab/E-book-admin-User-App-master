package com.example.e_bookapp.presentation.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.e_bookapp.viewmodel.MainViewModel
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.core.content.ContextCompat.startActivity
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreenUI(
    viewModel: MainViewModel = hiltViewModel(),
    pdfUrl: String,
    navController: NavController
) {
    val pdfState = rememberVerticalPdfReaderState(
        resource = ResourceType.Remote(pdfUrl),
        isZoomEnable = true
    )
    val context = LocalContext.current

    // Log state changes
    LaunchedEffect(pdfState.isLoaded) {
        Log.d("TAG1", "pdfState.isLoaded: ${pdfState.isLoaded}")
    }
    LaunchedEffect(pdfState.error) {
        if (pdfState.error != null) {
            Log.e("TAG1", "pdfState.error: ${pdfState.error?.message}", pdfState.error)
        }
    }
    Log.d("TAG1", "PDF URL: $pdfUrl")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6D4627))
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    windowInsets = WindowInsets(top = 30.dp, left = 10.dp, right = 40.dp),
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PDF Viewer",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 30.sp,
                                color = Color.White
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0F4559),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            floatingActionButton = {
                pdfState.file?.let {
                    FloatingActionButton(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_STREAM, it)
                                type = "application/pdf"
                            }
                            startActivity(context, Intent.createChooser(shareIntent, "Share PDF"), null)
                        },
                        containerColor = Color(0xFF4AA4EA),
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share PDF"
                        )
                    }
                }
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFF6D4627))
            ) {
                if (pdfState.error != null) {
                    Text(
                        text = "Error: ${pdfState.error?.message ?: "Failed to load PDF"}",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                } else {
                    when {
                        pdfState.isLoaded == false -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center).size(200.dp),
                                    color = Color(0xFF4AA4EA),
                                    strokeWidth = 4.dp
                                )
                            }
                        }

                    }
                }
            }


            Log.d("TAG1,", "PdfViewerScreenUI: ${pdfState.isLoaded}")


            VerticalPDFReader(
                state = pdfState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}