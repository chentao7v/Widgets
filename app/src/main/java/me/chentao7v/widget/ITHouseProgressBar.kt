package me.chentao7v.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import me.chentao7v.utils.dp2px

/**
 * @author leo
 */
class ITHouseProgressBar : View {

    private val MAIN_COLOR = Color.parseColor("#e44834")
    private val BG_COLOR = Color.parseColor("#DADDDA")

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val path = Path()

    private lateinit var pathMeasure: PathMeasure

    private var startX = 0f
    private var endX = 0f
    private var startY = 0f

    // 最低点与X轴的角度
    private val angel = 12.0

    private var currentPathLength = 0f
    private var totalLength = 0f
    private var lastHalfLength = 0f

    private var triangleHeight = dp2px(10f).toFloat()
    private var progressBoundRadius = dp2px(3f).toFloat()
    private var progressBoundWidth = dp2px(40f).toFloat()
    private var progressBoundHeight = dp2px(30f).toFloat()

    private var textOffset = 0f

    private var progressText = "0%"
    private var progressTextRotateAngel = 0f

    var progress = 0f
        set(value) {
            field = value

            // 前后半段path 各平分 50%
            currentPathLength = if (field <= 50) {
                (totalLength - lastHalfLength) * progress / 50
            } else {
                (totalLength - lastHalfLength) + (lastHalfLength) * (progress - 50) / 50
            }

            progressText = "${value.toInt()}%"

//            progressTextRotateAngel = when {
//                value <= 5 -> -10f
//                value <= 10 -> -3f
//                value <= 20 -> -5f
//                value <= 30 -> 0f
//                value <= 40 -> -2f
//                else -> 0f
//            }

            invalidate()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        paint.strokeWidth = dp2px(5f).toFloat()
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND

        textPaint.color = Color.WHITE
        textPaint.textSize = dp2px(15f).toFloat()
        textPaint.textAlign = Paint.Align.CENTER

        textOffset = (textPaint.ascent() + textPaint.descent()) / 2
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        startX = dp2px(20f).toFloat()
        endX = (width - dp2px(20f)).toFloat()
        val halfX = (endX - startX) / 2

        startY = (height / 2).toFloat()
        val centerY = halfX * Math.tan(Math.toRadians(angel)) // 中间点与两边的夹角

        // 40%-50% 与 50%-100% 的夹角是一样的
        val helpY = (centerY * 4 / 5).toFloat()
        val helpX = halfX * 4 / 5

        path.reset()
        // 前面半部分
        path.moveTo(startX, startY)
        // 2%一个拐点
        path.rLineTo(helpX * 2 / 40, helpY / 2)
        // 10%一个拐点
        path.rLineTo(helpX * 5 / 40, -helpY / 6)
        // 20%一个拐点
        path.rLineTo(helpX * 10 / 40, helpY / 6)
        // 30%一个拐点
        path.rLineTo(helpX * 10 / 40, 0f)

        // 后半部分平滑
        path.lineTo((width / 2).toFloat(), (startY + centerY).toFloat())
        path.lineTo(endX, startY)

        pathMeasure = PathMeasure(path, false)
        totalLength = pathMeasure.length
        lastHalfLength = halfX / Math.cos(Math.toRadians(angel)).toFloat()

    }

    override fun onDraw(canvas: Canvas) {

        val posPoint = floatArrayOf(0f, 0f)
        pathMeasure.getPosTan(currentPathLength, posPoint, null)

        drawLines(canvas, posPoint)

        drawProgressText(canvas, posPoint)
    }

    private fun drawLines(canvas: Canvas, posPoint: FloatArray) {
        // 绘制未完成进度条
        paint.color = BG_COLOR
        canvas.drawLine(posPoint[0], posPoint[1], endX, startY, paint)

        // 绘制已完成进度条
        paint.color = MAIN_COLOR
        canvas.drawLine(startX, startY, posPoint[0], posPoint[1], paint)
    }

    private fun drawProgressText(canvas: Canvas, posPoint: FloatArray) {
        // 绘制显示的进度

        canvas.save()

        canvas.translate(posPoint[0], posPoint[1])

        canvas.rotate(progressTextRotateAngel)

        path.reset()
        path.moveTo(0f, 0f)

        // 进度框三角形

        val triangleX = triangleHeight * Math.tan(Math.toRadians(30.0)).toFloat()
        // 这里给了一点点余量，避免出现空隙
        path.rLineTo(triangleX, -triangleHeight - dp2px(1f))
        path.rLineTo(-2 * triangleX, 0f)
        path.close()

        // 进度框四边形
        canvas.drawRoundRect(
            -progressBoundWidth / 2,
            -triangleHeight - progressBoundHeight,
            progressBoundWidth / 2,
            -triangleHeight,
            progressBoundRadius, progressBoundRadius,
            paint
        )
        // 绘制进度框四边形下的三角形
        canvas.drawPath(path, paint)

        // 绘制文字
        canvas.drawText(
            progressText,
            0,
            progressText.length,
            0f,
            -(triangleHeight + progressBoundHeight / 2 + textOffset),
            textPaint
        )

        canvas.restore()
    }

//    private fun getRotateAngel(progress: Float): Float {
//
//        // progress 理解为时间
//        // 返回的是旋转角度
//
//        val keyframe1 = Keyframe.ofFloat(0f, 0f)
//        val keyframe2 = Keyframe.ofFloat(0.02f, -10f)
//        val keyframe3 = Keyframe.ofFloat(0.10f, 0f)
//        val keyframe4 = Keyframe.ofFloat(0.20f, 5f)
//        val keyframe5 = Keyframe.ofFloat(0.30f, 1f)
//        val keyframe6 = Keyframe.ofFloat(0.40f, 0f)
//        val keyframe7 = Keyframe.ofFloat(1.00f, 0f)
//
//        val keyframes = PropertyValuesHolder.ofKeyframe(
//            "angel",
//            keyframe1,
//            keyframe2,
//            keyframe3,
//            keyframe4,
//            keyframe5,
//            keyframe6,
//            keyframe7
//        )
//
//        val valueAnimator = ValueAnimator.ofPropertyValuesHolder(keyframes)
//        valueAnimator.duration = 100
//
//    }

}