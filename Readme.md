[![](https://jitpack.io/v/com.gitee.icris/compose-image-viewer.svg)](https://jitpack.io/#com.gitee.icris/compose-image-viewer)

ComposeImageViewer
==================
---
## 描述
Compose 图片浏览器，支持滑动切换、双指放大、双击放大，支持列表页到大图切换动画

---
## 使用
<!-- 
- 用 Box 包裹列表与大图
- 创建必要的状态：`pagerState`、`lazyGridState`、`positionTracingState`
- 同步 `pagerState` 与 `lazyGridState`
- 在列表图片上添加 `positionTracing` 跟踪图片位置
- 点击显示大图时让 `pagerState` 滚动到点击图片位置 -->
```kotlin

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGridDemo() {
    val scope = rememberCoroutineScope()
    var showImageViewer by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { images.size }
    val lazyGridState = rememberLazyGridState()
    val positionTracingState = rememberPositionTracingState()
    positionTracingState.Sync(pagerState) { lazyGridState.scrollToItem(it) }

    Box {
        LazyVerticalGrid(GridCells.Fixed(3), state = lazyGridState) {
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
            pagerState,
            positionTracingState,
            model = { images[it] },
            onBack = { showImageViewer = false },
            animDuration = 3000
        )
    }
}
```

---
## 导入
#### 添加 jitpack.io 源
```kotlin
// settings.gradle.kts

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // ...
        maven(url = "https://www.jitpack.io")
    }
}

```
#### 添加依赖
```kotlin
implementation("com.gitee.icris:compose-image-viewer:1.0.0")
```

#### libs.versions.toml
```toml
[versions]
compose-image-viewer = '1.0.0'
[libraries]
imageviewer = { group = 'com.gitee.icris', name = 'compose-image-viewer', version.ref = 'compose-image-viewer'}

```