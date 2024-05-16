package dora.widget

import android.widget.FrameLayout
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.widget.LinearLayout
import android.os.Build
import android.view.*
import dora.widget.bottomdialog.R
import java.lang.ClassCastException
import java.lang.Exception
import java.lang.reflect.Field

@Deprecated("使用DoraBottomDialog替换，将在未来版本移除")
abstract class ADialogWindow {
    val shadowLayoutParams = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
    )
    private var onCancelListener: OnCancelListener? = null
    private var onBackListener: View.OnKeyListener? = null
    var gravityLayoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT,
        Gravity.BOTTOM
    )
    var gravity = Gravity.NO_GRAVITY
    var isNeedShadowView = false
    protected var shadowColor = INVALID_COLOR

    /**
     * DialogView的销毁将会引发PopupDialog的dismiss。
     */
    fun destroy() {
        if (onCancelListener != null) {
            onCancelListener!!.onCancel()
        }
    }

    fun getOnBackListener() : View.OnKeyListener? {
        return onBackListener
    }

    fun getOnCancelListener() : OnCancelListener? {
        return onCancelListener
    }

    fun performInflateView(
        activity: Activity,
        inflater: LayoutInflater,
        parent: FrameLayout?
    ): ViewGroup {
        val dialogView = inflater.inflate(R.layout.dora_dialog_window, parent, false) as ViewGroup
        val dialogViewRoot =
            dialogView.findViewById<View>(R.id.dialog_window_content) as LinearLayout
        // 有底部导航栏的情况下要上移，不能遮住NavigationBar
        if (isShowNavigationBar(activity)) {
            dialogViewRoot.setPadding(0, 0, 0, getNavigationBarHeight(activity))
        }
        if (gravity != Gravity.NO_GRAVITY) {
            dialogViewRoot.gravity = gravity
        }
        dialogViewRoot.setOnKeyListener { v, keyCode, event ->
            if (onBackListener != null) {
                onBackListener!!.onKey(v, keyCode, event)
            }
            false
        }
        addContent(inflater, parent, dialogViewRoot)
        return dialogView
    }

    abstract fun findViewById(resId: Int): View?
    abstract fun getContent() : View
    protected abstract fun addContent(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        viewRoot: LinearLayout
    )
    protected abstract fun setShadowViewOutsideCanDismiss(shadeView: View?, canDismiss: Boolean)

    fun setOnCancelListener(listener: OnCancelListener) {
        onCancelListener = listener
    }

    fun setOnBackListener(listener: View.OnKeyListener) {
        onBackListener = listener
    }

    interface OnCancelListener {
        fun onCancel()
    }

    companion object {
        const val DEFAULT_SHADOW_COLOR = 0x60000000
        protected const val INVALID_COLOR = 0
        private fun getRealHeight(context: Context): Int {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                wm.defaultDisplay.getRealSize(point)
            } else {
                wm.defaultDisplay.getSize(point)
            }
            return point.y
        }

        /**
         * 获得状态栏高度。
         */
        private fun getStatusBarHeight(context: Context): Int {
            val clazz: Class<*>
            val obj: Any
            val field: Field
            val x: Int
            var statusBarHeight = 0
            try {
                clazz = Class.forName("com.android.internal.R\$dimen")
                obj = clazz.newInstance()
                field = clazz.getField("status_bar_height")
                x = field[obj].toString().toInt()
                statusBarHeight = context.resources.getDimensionPixelSize(x)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return statusBarHeight
        }

        fun isShowNavigationBar(activity: Activity?): Boolean {
            if (activity == null) {
                return false
            }
            val contentRect = Rect()
            try {
                activity.window.decorView.getWindowVisibleDisplayFrame(contentRect)
            } catch (e: ClassCastException) {
                e.printStackTrace()
                return false
            }
            val activityHeight = contentRect.height()
            val statusBarHeight = getStatusBarHeight(activity)
            val remainHeight = getRealHeight(activity) - statusBarHeight
            return activityHeight != remainHeight
        }

        /**
         * 获得导航栏高度。
         */
        private fun getNavigationBarHeight(context: Context): Int {
            val clazz: Class<*>
            val obj: Any
            val field: Field
            val x: Int
            var navigationBarHeight = 0
            try {
                clazz = Class.forName("com.android.internal.R\$dimen")
                obj = clazz.newInstance()
                field = clazz.getField("navigation_bar_height")
                x = field[obj].toString().toInt()
                navigationBarHeight = context.resources.getDimensionPixelSize(x)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return navigationBarHeight
        }
    }
}