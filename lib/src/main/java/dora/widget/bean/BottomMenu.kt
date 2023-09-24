package dora.widget.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

class BottomMenu(val menu: String, override val itemType: Int) : MultiItemEntity {

    companion object {
        const val NORMAL_MENU = 0
        const val TOP_MENU = 1
    }
}