package dora.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

open class DoraDialogWindow : ADialogWindow {
    private var canTouchOutside = false
    private var onInflateListener: OnInflateListener? = null
    private var contentView: View? = null
    private var resId: Int = View.NO_ID

    constructor(contentView: View) {
        this.contentView = contentView
    }

    @JvmOverloads constructor(layoutResId: Int, shadowColor: Int = DEFAULT_SHADOW_COLOR) {
        this.resId = layoutResId
        setShadowColorForceShadow(shadowColor)
    }

    fun setOnInflateListener(listener: OnInflateListener) {
        onInflateListener = listener
    }

    /**
     * Only valid if the shadow background is set.
     */
    fun setCanTouchOutside(canTouchOutside: Boolean) {
        this.canTouchOutside = canTouchOutside
    }

    /**
     * Must invoke after [OnInflateListener.onInflateFinish].
     */
    override fun findViewById(resId: Int): View? {
        if (contentView == null) {
            return null
        }
        return contentView!!.findViewById(resId)
    }

    override fun getContent(): View {
        return contentView!!
    }

    override fun addContent(
        inflater: LayoutInflater?,
        parent: ViewGroup?,
        viewRoot: LinearLayout
    ) {
        if (isNeedShadowView) {
            viewRoot.setBackgroundColor(shadowColor)
            setShadowViewOutsideCanDismiss(viewRoot, true)
        }
        if (contentView == null) {
            contentView = LayoutInflater.from(viewRoot.context).inflate(resId, viewRoot, false)
        }
        contentView!!.isFocusable = true
        contentView!!.isFocusableInTouchMode = true
        contentView!!.setOnKeyListener { v, keyCode, event ->
            getOnBackListener()?.onKey(v, keyCode, event)
            true
        }

        contentView!!.setOnTouchListener { v, event -> true }
        viewRoot.addView(contentView)
        onInflateListener?.onInflateFinish(contentView)
    }

    override fun setShadowViewOutsideCanDismiss(shadeView: View?, canDismiss: Boolean) {
        shadeView!!.setOnTouchListener { v, event ->
            if (canDismiss) {
                getOnCancelListener()?.onCancel()
            }
            !canTouchOutside
        }
    }

    fun setShadowColorForceShadow(shadowColor: Int) {
        this.isNeedShadowView = true
        this.shadowColor = shadowColor
    }

    interface OnInflateListener {
        fun onInflateFinish(contentView: View?)
    }
}