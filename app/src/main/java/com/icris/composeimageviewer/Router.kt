package com.icris.composeimageviewer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Router() {
    var selected by remember {
        mutableIntStateOf(-1)
    }
    BackHandler(selected >= 0) {
        selected = -1
    }

    when (selected) {
        0 -> LazyListDemo()
        1 -> LazyGridDemo()
        2 -> LazyStaggeredGridDemo()
        3 -> AllDemo()
        else -> {
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                Button(onClick = { selected=0 }) {
                    Text(text = "LazyList")
                    LazyColumn(state = rememberLazyListState(), content = {})
                    LazyVerticalGrid(columns = GridCells.Fixed(3), state = rememberLazyGridState(), content = {})
                }
                Button(onClick = { selected=1 }) {
                    Text(text = "LazyGrid")
                    LazyColumn(state = rememberLazyListState(), content = {})
                    LazyVerticalGrid(columns = GridCells.Fixed(3), state = rememberLazyGridState(), content = {})
                }
                Button(onClick = { selected=2 }) {
                    Text(text = "LazyStaggeredGrid")
                    LazyColumn(state = rememberLazyListState(), content = {})
                    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3), state = rememberLazyStaggeredGridState(), content = {})
                }
                Button(onClick = { selected=3 }) {
                    Text(text = "All")
                    LazyColumn(state = rememberLazyListState(), content = {})
                    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3), state = rememberLazyStaggeredGridState(), content = {})
                }
            }
        }
    }


}