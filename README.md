dview-bottom-dialog
![Release](https://jitpack.io/v/dora4/dview-bottom-dialog.svg)
--------------------------------
![DORA视图 底部召唤阵](https://github.com/user-attachments/assets/dcc72744-8f52-4649-9941-91fa2758d450)

##### 卡名：Dora视图 BottomDialog 
###### 卡片类型：效果怪兽
###### 属性：暗
###### 星级：4
###### 种族：魔法师族
###### 攻击力/防御力：1500/1500
###### 效果：此卡不会因为对方卡的效果而破坏，并可使其无效化。此卡攻击里侧守备表示的怪兽时，若攻击力高于其守备力，则给予对方此卡原攻击力的伤害，并抽一张卡。每当我方名字带有「Dora视图」的怪兽召唤成功时，给这张卡放置一个视图指示物，可以移除一个视图指示物，让我方场上一只名字带有「Dora视图」的怪兽的攻击力上升500点，直到回合结束。

#### Gradle依赖配置

```groovy
// 添加以下代码到项目根目录下的build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
// 添加以下代码到app模块的build.gradle
dependencies {
    implementation 'com.github.dora4:dview-bottom-dialog:1.13'
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
dialog.show(this, arrayOf("外部浏览器打开", "刷新网页"))
```

过时的底部弹窗。
```kotlin
val dialogWindow = DoraDialogWindow()
DoraDialog.Builder(this)
    .create(dialogWindow)
    .show()
```
