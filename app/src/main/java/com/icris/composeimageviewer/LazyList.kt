package com.icris.composeimageviewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
fun LazyListDemo() {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val imageViewerState = rememberImageViewerState(
        model = { images[it] },
        count = { images.size },
        scrollToItem = { lazyListState.scrollToItem(it) }
    )
    Box {
        LazyColumn(state = lazyListState) {
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