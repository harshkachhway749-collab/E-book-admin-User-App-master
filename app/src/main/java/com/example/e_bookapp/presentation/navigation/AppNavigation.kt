package com.example.e_bookapp.presentation.navigation

import android.net.wifi.hotspot2.pps.HomeSp
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.e_bookapp.presentation.screens.AllBooksScreenUI
import com.example.e_bookapp.presentation.screens.BookByCategoryScreenUI
import com.example.e_bookapp.presentation.screens.BookDetailsScreenUI
import com.example.e_bookapp.presentation.screens.HomeScreenUI
import com.example.e_bookapp.presentation.screens.LoginScreen
import com.example.e_bookapp.presentation.screens.PdfViewerScreenUI
import com.example.e_bookapp.presentation.screens.SignUpScreen
import com.example.e_bookapp.presentation.screens.TabScreenUI
import com.example.e_bookapp.viewmodel.AuthState
import com.example.e_bookapp.viewmodel.MainViewModel

@Composable
fun AppNavigation(viewModel: MainViewModel = hiltViewModel()) {

    val authState= viewModel.authstate
    val context= LocalContext.current
    val navController= rememberNavController()
    LaunchedEffect(authState.value){
        when(authState.value){
            is AuthState.authenticated -> {
                navController.navigate(Routes.HomeScreen) {
                    popUpTo(Routes.SignUp) { inclusive = true }
                }
            }
            is AuthState.Error ->
                Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()

            else -> navController.navigate(Routes.Login)
        }
        viewModel.getAllBooks()
        viewModel.getAllCategory()
    }


    NavHost(
        navController = navController,
        startDestination = Routes.Login,

    ){

        composable<Routes.HomeScreen>{
          TabScreenUI(viewModel,navController)
        }
        composable<Routes.AllBooks> {
            AllBooksScreenUI(viewModel,navController )
        }

        composable<Routes.BookByCategory> {
            val data = it.toRoute<Routes.BookByCategory>()
            BookByCategoryScreenUI(
                CategoryName = data.CategoryName.toString(),navController = navController,

            )
        }

        composable<Routes.BookDetails> {
            BookDetailsScreenUI()

        }
        composable<Routes.PdfViewer> {
            val data=it.toRoute<Routes.PdfViewer>()

            PdfViewerScreenUI(pdfUrl = data.pdfUrl.toString(), navController = navController)
        }

        composable<Routes.Login> {
            LoginScreen(navController = navController)
        }

        composable<Routes.SignUp> {
            SignUpScreen(navController = navController)
        }
    }



}