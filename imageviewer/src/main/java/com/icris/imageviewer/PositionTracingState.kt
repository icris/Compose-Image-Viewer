package com.icris.imageviewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.roundToIntRect

@Stable
class PositionTracingState {
    internal val visibleItems = mutableStateMapOf<Int, Rect>()

    private fun isVisible(i: Int) = visibleItems[i]?.isEmpty == false
    internal fun sizeFor(i: Int) = visibleItems[i]?.roundToIntRect()?.size ?: IntSize.Zero
    internal fun centerFor(i: Int): IntOffset = visibleItems[i]?.center?.round() ?: IntOffset.Zero
    internal fun calcScale(i: Int, fullWidth: Int): Float = visibleItems[i]?.let { it.width / fullWidth } ?: .5f

    @OptIn(ExperimentalFoundationApi::class)
    @Composable fun Sync(pagerState: PagerState, lazyGridState: LazyGridState) {
        LaunchedEffect(pagerState.currentPage) {
            if (!isVisible(pagerState.currentPage)) lazyGridState.scrollToItem(pagerState.currentPage)
        }
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable fun Sync(pagerState: PagerState, scrollToItem: suspend (index: Int) -> Unit) {
        LaunchedEffect(pagerState.currentPage) {
            if (!isVisible(pagerState.currentPage)) scrollToItem(pagerState.currentPage)
        }
    }
}

@Composable
fun rememberPositionTracingState() = remember { PositionTracingState() }

fun Modifier.positionTracing(i: Int, state: PositionTracingState): Modifier = composed {
    DisposableEffect(state, i) {
        onDispose {
            state.visibleItems.remove(i)
        }
    }
    onGloballyPositioned {
        state.visibleItems[i] = it.boundsInRoot()
    }
}