package com.example.e_bookapp.viewmodel

import android.util.Log
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_bookapp.common.ResultState
import com.example.e_bookapp.data.BookModels
import com.example.e_bookapp.data.model.BookCategoryModel
import com.example.e_bookapp.repo.repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.core.utilities.ImmutableTree
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(val repo: repo) : ViewModel() {

    //auth states

    private val auth: FirebaseAuth= FirebaseAuth.getInstance()

    private val _authstate= MutableLiveData<AuthState>()
    val authstate: LiveData<AuthState> = _authstate

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if(auth.currentUser!=null){
            _authstate.value= AuthState.authenticated
        }else{
            _authstate.value= AuthState.unauthenticated
        }
    }

    fun login(email: String, password: String) {


        if (email.isEmpty() || password.isEmpty()) {
            _authstate.value =AuthState.Error("Email and password cannot be empty")
            return
        }
        _authstate.value =AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authstate.value = AuthState.authenticated
                } else {
                    _authstate.value =AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }

        _authstate.value= AuthState.authenticated
    }

    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authstate.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authstate.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authstate.value = AuthState.authenticated
                } else {
                    _authstate.value =AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }

        _authstate.value=AuthState.authenticated

    }

    fun signOut(){
        auth.signOut()
        _authstate.value= AuthState.unauthenticated
    }

    val currentUser: FirebaseUser?
        get() = auth.currentUser



    // all books states and values

    private val _getallbooksstate = MutableStateFlow<GetAllBooksState>(GetAllBooksState())
    val getallbooksstate = _getallbooksstate.asStateFlow()

    //books categories state

    private val _getallcategorystate = MutableStateFlow<GetAllCategory>(GetAllCategory())
    val getallcategorystate = _getallcategorystate.asStateFlow()

    //books by category state
    private val _getallbooksbycategorystate =
        MutableStateFlow<GetAllBooksByCategory>(GetAllBooksByCategory())
    val getallbooksbycategorystate = _getallbooksbycategorystate.asStateFlow()


    fun getAllBooks() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getAllBooks().collect {
                when (it) {

                    is ResultState.Success -> {
                        Log.d("TAG", "getAllBooks: ${it.data}")
                        _getallbooksstate.value =
                            GetAllBooksState(data = it.data, isLoading = false)
                    }

                    is ResultState.Eroor -> {
                        _getallbooksstate.value =
                            GetAllBooksState(error = it.exception, isLoading = false)

                    }

                    is ResultState.Loading -> {
                        _getallbooksstate.value = GetAllBooksState(isLoading = true)

                    }
                }

            }
        }
    }

    fun getAllCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllCategories().collect {
                when (it) {

                    is ResultState.Success -> {
                        Log.d("TAG", "getAllCategory: ${it.data}")

                        _getallcategorystate.value =
                            GetAllCategory(data = it.data, isLoading = false)


                    }

                    is ResultState.Eroor -> {

                        _getallcategorystate.value =
                            GetAllCategory(error = it.exception, isLoading = false)


                    }

                    is ResultState.Loading -> {
                        _getallcategorystate.value = GetAllCategory(isLoading = true)
                    }


                }

            }
        }
    }


    fun getAllBooksByCategory(CategoryName: String) {

        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllBooksByCategory(CategoryName).collect {

                when (it) {

                    is ResultState.Success -> {
                        Log.d("TAG", "getAllBooksByCategory: ${CategoryName}")

                        Log.d("TAG", "getAllBooksByCategory: ${it.data}")
                        _getallbooksbycategorystate.value =
                            GetAllBooksByCategory(data = it.data, isLoading = false)
                    }

                    is ResultState.Eroor -> {
                        _getallbooksbycategorystate.value =
                            GetAllBooksByCategory(error = it.exception, isLoading = false)
                    }

                    is ResultState.Loading -> {
                        _getallbooksbycategorystate.value = GetAllBooksByCategory(isLoading = true)

                    }


                }


            }


        }


    }


}


data class GetAllBooksState(
    val isLoading: Boolean = false,
    val data: List<BookModels> = emptyList(),
    val error: String = ""

)

data class GetAllCategory(
    val isLoading: Boolean = false,
    val data: List<BookCategoryModel> = emptyList(),
    val error: String = ""
)

data class GetAllBooksByCategory(
    val isLoading: Boolean = false,
    val data: List<BookModels> = emptyList(),
    val error: String = ""
)

sealed class AuthState{
    object Loading: AuthState()
    object authenticated: AuthState()
    object unauthenticated: AuthState()
    data class Error(val message: String): AuthState()
}