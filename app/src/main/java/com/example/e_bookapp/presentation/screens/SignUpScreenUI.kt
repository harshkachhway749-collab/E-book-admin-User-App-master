package com.example.e_bookapp.presentation.screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.e_bookapp.presentation.navigation.Routes
import com.example.e_bookapp.viewmodel.AuthState
import com.example.e_bookapp.viewmodel.MainViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isSigningUp = remember { mutableStateOf(false) }


    val authState= viewModel.authstate
    val context= LocalContext.current

    LaunchedEffect(authState.value){
        when(authState.value){
            is AuthState.authenticated -> {
                navController.navigate(Routes.HomeScreen) {
                    popUpTo(Routes.SignUp) { inclusive = true }
                }
            }
            is AuthState.Error ->
                Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()

            else -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
//
//            OutlinedTextField(
//                value = name.value,
//                onValueChange = { name.value = it },
//                label = { Text("Full Name") },
//                singleLine = true,
//                shape = RoundedCornerShape(12.dp),
//                modifier = Modifier.fillMaxWidth()
//            )

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    if (!email.value.isEmpty() || !password.value.isEmpty()) {
                        viewModel.signUp(email.value, password.value)
                        isSigningUp.value = true

                        if(authState.value==AuthState.authenticated){
                            navController.navigate(Routes.HomeScreen)
                        }

                    }

//                    navController.navigate("login") {
//                        popUpTo("signup") { inclusive = true }
//                    }
                },
                shape = RoundedCornerShape(12.dp),
                enabled = !isSigningUp.value,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (isSigningUp.value) "Signing up..." else "Sign Up")
            }

            // 🔽 Navigate back to Login
            Button(
                onClick = {
                    navController.navigate(Routes.Login) {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Already have an account? Login")
            }
        }
    }
}