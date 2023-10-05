package dora.widget

import android.app.Activity
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import dora.widget.bottomdialog.R

@Deprecated("使用DoraBottomDialog替换，将在未来版本移除")
open class DoraDialog {

    protected var dialogWindow: ADialogWindow? = null
    protected lateinit var decorView: FrameLayout
    protected lateinit var dialogRoot: ViewGroup
    protected lateinit var dialogContent: View
    private var pushOutAnim: Animation? = null
    private var pushInAnim: Animation? = null
    private var dismissing = false
    private lateinit var ownActivity: Activity
    private lateinit var layoutInflater: LayoutInflater
    var isShown = false
        private set
    private var onAttachListener: OnAttachListener? = null
    private var onDismissListener: OnDismissListener? = null
    private var isLoaded: Boolean = false

    protected constructor(builder: Builder) {
        ownActivity = builder.context as Activity
        layoutInflater = LayoutInflater.from(ownActivity)
        decorView = ownActivity.window.decorView as FrameLayout
        dialogWindow = builder.dialogWindow
        dialogRoot = dialogWindow!!.performInflateView(ownActivity, layoutInflater, decorView)
        dialogContent = dialogWindow!!.getContent()
        dialogWindow!!.gravity = builder.gravity
        dialogRoot.layoutParams = dialogWindow!!.shadowLayoutParams
        applyAnimation(builder)
    }

    fun setOnAttachListener(listener: OnAttachListener) {
        this.onAttachListener = listener
    }

    fun setOnDismissListener(listener: OnDismissListener) {
        this.onDismissListener = listener
    }

    protected fun applyAnimation(builder: Builder) {
        pushOutAnim = builder.pushOutAnimation
        pushInAnim = builder.pushInAnimation
    }

    fun show() {
        initViews(dialogRoot)
        isShown = true
    }

    fun toggle() {
        if (isShown) {
            dismiss()
        } else {
            show()
        }
    }

    fun dismiss() {
        if (dismissing) {
            return
        }
        pushOutAnim!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                decorView.post {
                    decorView.removeView(dialogRoot)
                    dismissing = false
                    isShown = false
                    onDismissListener?.onDismiss()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        dialogContent.startAnimation(pushOutAnim)
        dismissing = true
    }

    private fun initViews(viewRoot: ViewGroup) {
        decorView.addView(viewRoot)
        pushInAnim?.let {
            dialogContent.startAnimation(it)
        }
        viewRoot.requestFocus()
        dialogWindow!!.setOnBackListener(View.OnKeyListener { v, keyCode, event ->
            when (event.action) {
                KeyEvent.ACTION_UP -> if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss()
                    return@OnKeyListener true
                }
            }
            false
        })
        dialogWindow!!.setOnCancelListener(object : ADialogWindow.OnCancelListener {
            override fun onCancel() {
                dismiss()
            }
        })
        if (!isLoaded) {
            onAttachListener?.onFirstAttached(dialogWindow!!)
            isLoaded = true
        }
    }

    fun onAttach(l: OnAttachListener): DoraDialog {
        onAttachListener = l
        return this
    }

    fun onDismiss(l: OnDismissListener): DoraDialog {
        onDismissListener = l
        return this
    }

    interface OnAttachListener {
        fun onFirstAttached(window: ADialogWindow)
    }

    interface OnDismissListener {
        fun onDismiss()
    }

    open class Builder(context: Context) {
        var dialogWindow: ADialogWindow? = null

        /* @hide */
        internal var pushInAnim: Animation? = null

        /* @hide */
        internal var pushOutAnim: Animation? = null
        val context: Context
        var gravity = 0

        fun setPushInAnimation(animResId: Int): Builder {
            pushInAnim = AnimationUtils.loadAnimation(context, animResId)
            return this
        }

        fun setPushInAnimation(`in`: Animation): Builder {
            pushInAnim = `in`
            return this
        }

        val pushInAnimation: Animation
            get() = if (pushInAnim == null) AnimationUtils.loadAnimation(
                    context,
                    R.anim.anim_bottom_in
            ) else pushInAnim!!

        val pushOutAnimation: Animation
            get() = if (pushOutAnim == null) AnimationUtils.loadAnimation(
                context,
                R.anim.anim_bottom_out
            ) else pushOutAnim!!

        fun setPushOutAnimation(animResId: Int): Builder {
            pushOutAnim = AnimationUtils.loadAnimation(context, animResId)
            return this
        }

        fun setPushOutAnimation(out: Animation): Builder {
            pushOutAnim = out
            return this
        }

        fun gravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun create(window: DoraDialogWindow): DoraDialog {
            dialogWindow = window
            return DoraDialog(this)
        }

        init {
            require(Activity::class.java.isAssignableFrom(context.javaClass)) { "need activity context" }
            this.context = context
        }
    }
}