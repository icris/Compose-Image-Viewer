package com.icris.composeimageviewer

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.icris.imageviewer.ImageViewer
import com.icris.imageviewer.positionTracing
import com.icris.imageviewer.rememberPositionTracingState
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllDemo() {
    val scope = rememberCoroutineScope()
    var showImageViewer by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { images.size*3 }
    val allLazyListState = rememberLazyListState()
    val lazyListState = rememberLazyListState()
    val lazyGridState = rememberLazyGridState()
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val positionTracingState = rememberPositionTracingState()
    positionTracingState.Sync(pagerState) {
        val index = it / images.size
        val i = it % images.size
        allLazyListState.scrollToItem(index)
        when (index) {
            0 -> { lazyListState.scrollToItem(i) }
            1 -> { lazyGridState.scrollToItem(i) }
            2 -> { lazyStaggeredGridState.scrollToItem(i) }
            else -> {}
        }
    }

    Box {
        LazyColumn(Modifier.fillMaxSize(), allLazyListState) {
            item {
                LazyColumn(Modifier.fillParentMaxSize(), state = lazyListState) {
                    items(images.size) {
                        AsyncImage(
                            model = images[it],
                            contentDescription = null,
                            Modifier
                                .clickable {
                                    scope.launch { pagerState.scrollToPage(it) }
                                    showImageViewer = true
                                }
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .positionTracing(it, positionTracingState),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }

            }
            item {
                LazyVerticalGrid(GridCells.Fixed(3), Modifier.fillParentMaxSize(), state = lazyGridState) {
                    items(images.size) {
                        val i = it+ images.size
                        AsyncImage(
                            model = images[it],
                            contentDescription = null,
                            Modifier
                                .clickable {
                                    scope.launch { pagerState.scrollToPage(i) }
                                    showImageViewer = true
                                }
                                .fillMaxWidth()
                                .aspectRatio(16f/9f)
                                .positionTracing(i, positionTracingState),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
            item {
                LazyVerticalStaggeredGrid(StaggeredGridCells.Fixed(3), Modifier.fillParentMaxSize(), state = lazyStaggeredGridState) {
                    items(images.size) {
                        val i = it+ images.size*2
                        AsyncImage(
                            model = images[it],
                            contentDescription = null,
                            Modifier
                                .clickable {
                                    scope.launch { pagerState.scrollToPage(i) }
                                    showImageViewer = true
                                }
                                .fillMaxWidth()
                                .padding(4.dp)
                                .positionTracing(i, positionTracingState),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
        }

        ImageViewer(
            visible = showImageViewer,
            pagerState ,
            positionTracingState ,
            model = { images[it% images.size]},
            onBack = { showImageViewer=false },
            animDuration = 3000
        )
    }
}