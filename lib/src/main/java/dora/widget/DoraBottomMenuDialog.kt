package dora.widget

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import dora.widget.bean.BottomMenu
import dora.widget.bottomdialog.R

class DoraBottomMenuDialog : View.OnClickListener, OnItemChildClickListener {

    private var bottomDialog: Dialog? = null
    private var listener: OnMenuClickListener? = null

    interface OnMenuClickListener {
        fun onMenuClick(position: Int, menu: String)
    }

    fun setOnMenuClickListener(listener: OnMenuClickListener) : DoraBottomMenuDialog {
        this.listener = listener
        return this
    }

    fun show(activity: Activity, menus: Array<String>): DoraBottomMenuDialog {
        if (bottomDialog == null && !activity.isFinishing) {
            bottomDialog = Dialog(activity, R.style.DoraView_AlertDialog)
            val contentView =
                LayoutInflater.from(activity).inflate(R.layout.dview_dialog_content, null)
            initView(contentView, menus)
            bottomDialog!!.setContentView(contentView)
            bottomDialog!!.setCanceledOnTouchOutside(true)
            bottomDialog!!.setCancelable(true)
            bottomDialog!!.window!!.setGravity(Gravity.BOTTOM)
            bottomDialog!!.window!!.setWindowAnimations(R.style.DoraView_BottomDialog_Animation)
            bottomDialog!!.show()
            val window = bottomDialog!!.window
            window!!.decorView.setPadding(0, 0, 0, 0)
            val lp = window.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = lp
        } else {
            bottomDialog!!.show()
        }
        return this
    }

    private fun initView(contentView: View, menus: Array<String>) {
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.rv_menu)
        val adapter = MenuAdapter()
        val list = mutableListOf<BottomMenu>()
        menus.forEachIndexed { index, s ->
            when (index) {
                0 -> {
                    list.add(BottomMenu(s, BottomMenu.TOP_MENU))
                }
                else -> {
                    list.add(BottomMenu(s, BottomMenu.NORMAL_MENU))
                }
            }
        }
        adapter.setList(list)
        recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(contentView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        adapter.addChildClickViewIds(R.id.tv_menu)
        adapter.setOnItemChildClickListener(this)
        val tvCancel = contentView.findViewById<TextView>(R.id.tv_cancel)
        tvCancel.setOnClickListener(this)
    }

    private fun dismiss() {
        bottomDialog?.dismiss()
        bottomDialog = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cancel -> dismiss()
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        listener?.onMenuClick(position, (adapter.getItem(position) as BottomMenu).menu)
        dismiss()
    }
}