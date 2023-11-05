package com.icris.composeimageviewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.icris.imageviewer.ImageViewer
import com.icris.imageviewer.positionTracing
import com.icris.imageviewer.rememberImageViewerState
import kotlinx.coroutines.launch


@Composable
fun AllDemo() {
    val scope = rememberCoroutineScope()
    val allLazyListState = rememberLazyListState()
    val lazyListState = rememberLazyListState()
    val lazyGridState = rememberLazyGridState()
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val imageViewerState = rememberImageViewerState(
        model = { images[it % images.size] },
        count = { images.size * 3 },
        scrollToItem = {
            val index = it / images.size
            val i = it % images.size
            allLazyListState.scrollToItem(index)
            when (index) {
                0 -> lazyListState.scrollToItem(i)
                1 -> lazyGridState.scrollToItem(i)
                2 -> lazyStaggeredGridState.scrollToItem(i)
                else -> {}
            }
        }
    )
    Box {
        LazyColumn(Modifier.fillMaxSize(), allLazyListState) {
            item {
                LazyColumn(Modifier.fillParentMaxSize(), state = lazyListState) {
                    items(images.size) {
                        AsyncImage(
                            model = images[it],
                            contentDescription = null,
                            Modifier
                                .clickable { scope.launch { imageViewerState.onClick(it) } }
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .positionTracing(it, imageViewerState),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }

            }
            item {
                LazyVerticalGrid(
                    GridCells.Fixed(3),
                    Modifier.fillParentMaxSize(),
                    state = lazyGridState
                ) {
                    items(images.size) {
                        val i = it + images.size
                        AsyncImage(
                            model = images[it],
                            contentDescription = null,
                            Modifier
                                .clickable { scope.launch { imageViewerState.onClick(i) } }
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .positionTracing(i, imageViewerState),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
            item {
                LazyVerticalStaggeredGrid(
                    StaggeredGridCells.Fixed(3),
                    Modifier.fillParentMaxSize(),
                    state = lazyStaggeredGridState
                ) {
                    items(images.size) {
                        val i = it + images.size * 2
                        AsyncImage(
                            model = images[it],
                            contentDescription = null,
                            Modifier
                                .clickable { scope.launch { imageViewerState.onClick(i) } }
                                .fillMaxWidth()
                                .padding(4.dp)
                                .positionTracing(i, imageViewerState),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
        }

        ImageViewer(imageViewerState)
    }
}