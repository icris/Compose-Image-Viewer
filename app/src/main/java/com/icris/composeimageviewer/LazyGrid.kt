package com.icris.composeimageviewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.icris.imageviewer.ImageViewer
import com.icris.imageviewer.positionTracing
import com.icris.imageviewer.rememberImageViewerState
import kotlinx.coroutines.launch

@Composable
fun LazyGridDemo() {
    val scope = rememberCoroutineScope()
    val lazyGridState = rememberLazyGridState()
    val imageViewerState = rememberImageViewerState(
        model = { images[it] },
        count = { images.size },
        scrollToItem = { lazyGridState.scrollToItem(it) }
    )
    Box {
        LazyVerticalGrid(GridCells.Fixed(3), state = lazyGridState) {
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
        ImageViewer(imageViewerState)
    }
}