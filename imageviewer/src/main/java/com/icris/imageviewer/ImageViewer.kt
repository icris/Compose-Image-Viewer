package com.icris.imageviewer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.center
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun ImageViewer(state: ImageViewerState, animDuration: Int = DefaultDurationMillis) {
    var visible by state.bigPictureVisible
    var fullWidth by remember { mutableIntStateOf(0) }
    BackHandler(visible) { visible = false }

    AnimatedVisibility(
        visible = visible,
        modifier = Modifier.fillMaxSize().onSizeChanged { fullWidth = it.width },
        enter = EnterTransition.None,
        exit = ExitTransition.None,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .animateEnterExit(fadeIn(), fadeOut())
                    .background(Color.Black)
            )
            HorizontalPager(state = state.pagerState) {
                ImageItem(animDuration, state.model.value, it, state.positionTracingState, fullWidth, onBack = { visible = false })
            }

            Text(
                text = "${state.pagerState.currentPage + 1}/${state.pagerState.pageCount}",
                Modifier.align(Alignment.BottomCenter).animateEnterExit(fadeIn(), fadeOut()),
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedVisibilityScope.ImageItem(
    animDuration: Int,
    model: (Int) -> Any?,
    i: Int,
    positionTracingState: PositionTracingState,
    fullWidth: Int,
    onBack: () -> Unit
) {
    AsyncImage(
        model = model(i),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .animateEnterExit(
                enter = slideIn(tween(animDuration)) { positionTracingState.centerFor(i) - it.center }
                        + scaleIn(tween(animDuration), initialScale = positionTracingState.calcScale(i, fullWidth))
                        + expandIn(tween(animDuration), expandFrom = Alignment.Center) { positionTracingState.sizeFor(i) },
                exit = slideOut(tween(animDuration)) { positionTracingState.centerFor(i) - it.center }
                        + scaleOut(tween(animDuration), targetScale = positionTracingState.calcScale(i, fullWidth))
                        + shrinkOut(tween(animDuration), shrinkTowards = Alignment.Center) { positionTracingState.sizeFor(i) }
            )
            .zoom(onBack), contentScale = ContentScale.FillWidth)
}


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.zoom(onTap: () -> Unit) = composed {
    var fullSize by remember { mutableStateOf(Size.Unspecified) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }
    val transformableState = rememberTransformableState { zoomChange, panChange, rotationChange ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        offset = calOffset(fullSize, scale, offset + panChange)
        rotation += rotationChange
    }
    val realScale by animateFloatAsState(targetValue = scale, label = "scale")
    val realRotation by animateFloatAsState(targetValue = rotation, label = "rotation")
    val realOffset by animateOffsetAsState(targetValue = offset, label = "offset")
    transformable(transformableState, { scale > 1f }, true)
        .graphicsLayer {
            fullSize = size
            scaleX = realScale
            scaleY = realScale
            translationX = realOffset.x
            translationY = realOffset.y
            rotationZ = realRotation
        }
        .pointerInput("tapGestures") {
            detectTapGestures(
                onDoubleTap = { point ->
                    val realPoint = offset + fullSize.center - point
                    if (scale <= 1.5f) {
                        scale = 2.6f
                        offset = calOffset(fullSize, scale, realPoint * scale)
                    } else {
                        scale = 1f
                        offset = Offset.Zero
                        rotation = 0f
                    }
                },
                onTap = {
                    scale = 1f
                    offset = Offset.Zero
                    rotation = 0f
                    onTap()
                }
            )
        }
}

private fun calOffset(size: Size, scale: Float, offset: Offset): Offset {
    if (size.isUnspecified) return Offset.Zero
    val px = size.width * (scale - 1f) / 2f
    val py = size.height * (scale - 1f) / 2f
    return Offset(offset.x.coerceIn(-px, px), offset.y.coerceIn(-py, py))
}
