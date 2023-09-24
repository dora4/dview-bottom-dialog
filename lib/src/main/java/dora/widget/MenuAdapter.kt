package dora.widget

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import dora.widget.bean.BottomMenu
import dora.widget.bottomdialog.R

class MenuAdapter : BaseMultiItemQuickAdapter<BottomMenu, BaseViewHolder>() {

    init {
        addItemType(BottomMenu.NORMAL_MENU, R.layout.dview_item_menu)
        addItemType(BottomMenu.TOP_MENU, R.layout.dview_item_menu_top)
    }

    override fun convert(holder: BaseViewHolder, item: BottomMenu) {
        holder.setText(R.id.tv_menu, item.menu)
    }
}