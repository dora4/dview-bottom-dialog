package dora.widget

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import dora.widget.bottomdialog.R

class MenuAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.dview_item_menu) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_menu, item)
    }
}