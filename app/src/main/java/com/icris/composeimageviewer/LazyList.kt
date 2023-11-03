package com.icris.composeimageviewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.icris.imageviewer.ImageViewer
import com.icris.imageviewer.positionTracing
import com.icris.imageviewer.rememberPositionTracingState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyListDemo() {
    val scope = rememberCoroutineScope()
    var showImageViewer by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { images.size }
    val lazyListState = rememberLazyListState()
    val positionTracingState = rememberPositionTracingState()
    positionTracingState.Sync(pagerState) { lazyListState.scrollToItem(it) }

    Box {
        LazyColumn(state = lazyListState) {
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
        ImageViewer(
            visible = showImageViewer,
            pagerState ,
            positionTracingState ,
            model = { images[it]},
            onBack = { showImageViewer=false }
        )
    }
}