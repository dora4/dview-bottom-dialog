dview-bottom-dialog
![Release](https://jitpack.io/v/dora4/dview-bottom-dialog.svg)
--------------------------------

#### gradle依赖配置

```groovy
// 添加以下代码到项目根目录下的build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
// 添加以下代码到app模块的build.gradle
dependencies {
    implementation 'com.github.dora4:dview-bottom-dialog:1.7'
}
```

#### 使用控件
```kotlin
// 打开底部弹窗
val dialog = DoraBottomDialog()
dialog.show(this, R.layout.dialog_content) {
    // 在这里初始化你的内容数据
}
// 打开底部菜单弹窗
val dialog = DoraBottomMenuDialog()
dialog.setOnMenuClickListener(object : DoraBottomMenuDialog.OnMenuClickListener {
    override fun onMenuClick(position: Int, menu: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
})
dialog.show(this, arrayOf("外部浏览器打开"))
```
