package me.chentao7v.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout
import me.chentao7v.utils.dp2px

/**
 * @author leo
 */
class SearchLayout : FrameLayout {

    private val path = Path()
    private val animator by lazy {
        val anim = ObjectAnimator.ofFloat(this, "radius", 0f, dp2px(500f).toFloat())
        anim.addListener(animatorListener)
        anim.duration = 1000
        return@lazy anim
    }
    var animatorListener: Animator.AnimatorListener? = null
    private var radius = 0f
        set(value) {
            field = value

            path.reset()
            path.addCircle(width.toFloat() - dp2px(20f), dp2px(20f).toFloat(), radius, Path.Direction.CW)

            invalidate()
        }


    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        // 避免不调用draw方法
        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas) {

        if (!isInEditMode) {
            canvas.clipPath(path)
        }

        super.draw(canvas)
    }

    fun start() {
        animator.start()
    }

    fun revert() {
        animator.reverse()
    }

}