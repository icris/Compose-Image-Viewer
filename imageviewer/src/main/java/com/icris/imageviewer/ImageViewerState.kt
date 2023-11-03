package com.icris.imageviewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.roundToIntRect

@OptIn(ExperimentalFoundationApi::class)
@Stable
class ImageViewerState(
    internal val model: State<(Int) -> Any?>,
    internal val pagerState: PagerState,
    internal val positionTracingState: PositionTracingState = PositionTracingState(),
    internal val bigPictureVisible: MutableState<Boolean> = mutableStateOf(false)
) {
    suspend fun onClick(i: Int) {
        bigPictureVisible.value = true
        pagerState.scrollToPage(i)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberImageViewerState(
    model: (Int) -> Any?,
    count: () -> Int,
    scrollToItem: suspend (index: Int) -> Unit
): ImageViewerState {
    val modelState = rememberUpdatedState(newValue = model)
    val pagerState = rememberPagerState(pageCount = count)
    val state = remember { ImageViewerState(modelState, pagerState) }
    val newScrollToItem by rememberUpdatedState(newValue = scrollToItem)
    LaunchedEffect(pagerState.currentPage) {
        if (!state.positionTracingState.isVisible(pagerState.currentPage)) newScrollToItem(pagerState.currentPage)
    }
    return state
}

@Stable
class PositionTracingState {
    internal val visibleItems = mutableStateMapOf<Int, Rect>()

    internal fun isVisible(i: Int) = visibleItems[i]?.isEmpty == false
    internal fun sizeFor(i: Int) = visibleItems[i]?.roundToIntRect()?.size ?: IntSize.Zero
    internal fun centerFor(i: Int): IntOffset = visibleItems[i]?.center?.round() ?: IntOffset.Zero
    internal fun calcScale(i: Int, fullWidth: Int): Float = visibleItems[i]?.let { it.width / fullWidth } ?: .5f
}


fun Modifier.positionTracing(i: Int, state: ImageViewerState): Modifier = composed {
    DisposableEffect(state, i) {
        onDispose {
            state.positionTracingState.visibleItems.remove(i)
        }
    }
    onGloballyPositioned {
        state.positionTracingState.visibleItems[i] = it.boundsInRoot()
    }
}
