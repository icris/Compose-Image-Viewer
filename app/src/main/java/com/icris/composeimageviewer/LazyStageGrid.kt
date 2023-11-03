package com.icris.composeimageviewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.icris.imageviewer.ImageViewer
import com.icris.imageviewer.positionTracing
import com.icris.imageviewer.rememberImageViewerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyStaggeredGridDemo() {
    val scope = rememberCoroutineScope()
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val imageViewerState = rememberImageViewerState(
        model = { images[it] },
        count = { images.size },
        scrollToItem = { lazyStaggeredGridState.scrollToItem(it) }
    )
    Box {
        LazyVerticalStaggeredGrid(StaggeredGridCells.Fixed(3), state = lazyStaggeredGridState) {
            items(images.size) {
                AsyncImage(
                    model = images[it],
                    contentDescription = null,
                    Modifier
                        .clickable {
                            scope.launch { imageViewerState.onClick(it) }
                        }
                        .fillMaxWidth()
                        .positionTracing(it, imageViewerState),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        ImageViewer(imageViewerState)
    }
}