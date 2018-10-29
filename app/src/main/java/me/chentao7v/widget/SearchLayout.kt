package me.chentao7v.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout
import me.chentao7v.utils.dp2px

/**
 * @author leo
 */
class SearchLayout : FrameLayout {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//    private val rectF = RectF()
    private val path = Path()

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private val transferMode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    init {
        paint.color = Color.BLACK
        setWillNotDraw(false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

//        rectF.set(
//            0f, 0f, width.toFloat(), height.toFloat()
//        )

        path.reset()
        path.addCircle((width / 2).toFloat(), (height / 2).toFloat(), dp2px(200f).toFloat(), Path.Direction.CW)
    }

    override fun draw(canvas: Canvas) {

        canvas.clipPath(path)



//        val saveLayer = canvas.saveLayer(rectF, paint)
//
//        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), dp2px(200f).toFloat(), paint)
//        paint.xfermode = transferMode
        super.draw(canvas)



//        paint.xfermode = null
//
//        canvas.restoreToCount(saveLayer)

    }

}