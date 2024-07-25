package dora.widget

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import dora.widget.bottomdialog.R

class DoraBottomDialog {

    private var bottomDialog: Dialog? = null

    fun show(activity: Activity, @LayoutRes resId: Int, block: ((contentView: View) -> Unit)? = null): DoraBottomDialog {
        val contentView = LayoutInflater.from(activity).inflate(resId, null)
        show(activity, contentView, block)
        return this
    }

    fun show(activity: Activity, contentView: View, block: ((contentView: View) -> Unit)? = null) : DoraBottomDialog {
        if (bottomDialog == null && !activity.isFinishing) {
            block?.invoke(contentView)
            bottomDialog = Dialog(activity, R.style.DoraView_AlertDialog)
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

    fun dismiss() {
        bottomDialog?.dismiss()
        bottomDialog = null
    }
}