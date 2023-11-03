ComposeImageViewer
==================

[![](https://jitpack.io/v/com.gitee.icris/compose-image-viewer.svg)](https://jitpack.io/#com.gitee.icris/compose-image-viewer)

---
## 描述
Compose 图片浏览器，支持滑动切换、双指放大、双击放大，支持列表页到大图切换动画

---
## 使用
```kotlin
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
                        .clickable {
                            scope.launch { imageViewerState.onClick(it) }
                        }
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .positionTracing(it, imageViewerState),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        ImageViewer(imageViewerState, 3000)
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
implementation("com.gitee.icris:compose-image-viewer:1.0.1")
```

#### libs.versions.toml
```toml
[versions]
compose-image-viewer = '1.0.1'
[libraries]
imageviewer = { group = 'com.gitee.icris', name = 'compose-image-viewer', version.ref = 'compose-image-viewer'}

```