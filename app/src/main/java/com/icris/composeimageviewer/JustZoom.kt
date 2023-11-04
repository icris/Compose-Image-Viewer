package com.icris.composeimageviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import com.icris.imageviewer.zoom

@Composable
fun JustZoomDemo() {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).fillMaxHeight()) {
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.LightGray)
                .zoom { },
                Alignment.Center) {
                Text(text = "放大内容")
            }
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Gray)
                .zoom { },
                Alignment.Center) {
                Text(text = "放大内容")
            }
        }
        Column(Modifier.weight(1f).fillMaxHeight()) {
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .zoom { }
                .background(Color.White),
                Alignment.Center) {
                Text(text = "放大背景")
            }
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds()
                .zoom { }
                .background(Color.Black),
                Alignment.Center) {
                Text(text = "不超过边界", color = Color.White)
            }
        }
    }
}