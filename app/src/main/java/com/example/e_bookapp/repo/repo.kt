package com.example.e_bookapp.repo

import android.util.Log
import com.example.e_bookapp.common.BOOK_CATEGORY
import com.example.e_bookapp.common.BOOK_PATH
import com.example.e_bookapp.common.ResultState
import com.example.e_bookapp.data.BookModels
import com.example.e_bookapp.data.model.BookCategoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class repo @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase

) {

    fun getAllBooks()
        : Flow<ResultState<List<BookModels>>> = callbackFlow {

        trySend(ResultState.Loading)


        val postListener= object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                var items:List<BookModels> = emptyList()

                items = snapshot.children.map {value->
                    value.getValue<BookModels>()!!

                }
                Log.d("TAG", "onDataChange: $items")

                trySend(ResultState.Success(items))
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Eroor(error.message))

            }

        }

        firebaseDatabase.reference.child(BOOK_PATH).addValueEventListener(postListener)

        awaitClose{
            close()
        }

    }


    fun getAllCategories() : Flow<ResultState<List<BookCategoryModel>>> = callbackFlow {


        trySend(ResultState.Loading)


        val postListener= object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var items: List<BookCategoryModel> = emptyList()

                items=snapshot.children.map {
                    it.getValue<BookCategoryModel>()!!

                }
                Log.d("TAG", "onDataChange: $items")
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Eroor(error.message))

            }
        }

        firebaseDatabase.reference.child(BOOK_CATEGORY).addValueEventListener(postListener)

        awaitClose{
            close()
        }

    }


    fun getAllBooksByCategory(CategoryName: String) : Flow<ResultState<List<BookModels>>> = callbackFlow {



        trySend(ResultState.Loading)


        val postEventListner = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var items:List<BookModels> = emptyList()

                items = snapshot.children.filter {
                    it.getValue<BookModels>()?.BookCategory == CategoryName
                }.map {
                    it.getValue<BookModels>()!!
                }


                trySend(ResultState.Success(items))


            }
            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Eroor(error.message))
            }
        }


        firebaseDatabase.reference.child(BOOK_PATH).addValueEventListener(postEventListner)


        awaitClose{
            close()
        }
    }




}