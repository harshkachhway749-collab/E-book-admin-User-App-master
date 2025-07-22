package com.example.e_bookapp.presentation.screens

import android.app.ActionBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.e_bookapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@Composable
fun TabScreenUI(viewModel: MainViewModel=hiltViewModel(),navController: NavController) {

    val tabs= listOf<TabItem>(

        TabItem(
            title = "Categories",
            Icon = Icons.Default.Category,
            fillesIcon = Icons.Filled.Category
        ) ,

        TabItem(
            title = "Books",
            Icon = Icons.Default.Book,
            fillesIcon = Icons.Filled.Book
        )
    )
    
    
    val pagestate=rememberPagerState(

        pageCount = {tabs.size}
    )
    val scope= rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 50.dp)
    ) {

        TabRow(
            selectedTabIndex = pagestate.currentPage,

            modifier = Modifier.fillMaxWidth()
        ) {

            tabs.forEachIndexed {index,Tabitem ->
                Tab(
                    modifier= Modifier.fillMaxWidth(),
                    selected = pagestate.currentPage==index,
                    onClick = {
                        scope.launch {
                            pagestate.animateScrollToPage(index)
//                            pagestate.scrollToPage(index)

                        }



                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (pagestate.currentPage==index){
                                    Tabitem.fillesIcon
                                }else{
                                    Tabitem.Icon
                                },
                                 contentDescription = Tabitem.title,
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(18.dp))

                            Text("${Tabitem.title}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    },
                    selectedContentColor = Color.Red,
                    unselectedContentColor = Color.White
                )
            }

        }


        HorizontalPager(
            state=pagestate

        ) {
            when(it) {
                0-> CategoryScreen(navController=navController)
                1-> AllBooksScreenUI(viewModel,navController)
            }

        }
    }

}

data class TabItem(
    val title:String,
    val Icon: ImageVector,
    val fillesIcon: ImageVector
)