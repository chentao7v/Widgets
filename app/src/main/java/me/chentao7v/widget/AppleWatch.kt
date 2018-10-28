package me.chentao7v.widget

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import me.chentao7v.utils.TimeUtils
import me.chentao7v.utils.dp2px

/**
 * @author leo
 */
class AppleWatch : View {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val posPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val rectF = RectF()

    // 表盘半径
    private val watchRadius = dp2px(120f)
    // 表盘边框宽度
    private val watchBorder = dp2px(10f)
    // 表盘path
    private val watchPath = Path()

    // 分->刻度path
    private val minutePath = Path()
    // 分->刻度高度
    private val minuteHeight = dp2px(10f)
    // 分->刻度宽度
    private val minuteWidth = dp2px(1.5f)
    private lateinit var minutePathEffect: PathEffect

    // 时->刻度path
    private val hourPath = Path()
    private val hourHeight = dp2px(20f)
    private val hourWidth = dp2px(3f)
    private lateinit var hourPathEffect: PathEffect

    // 时针/分针/秒针
    private val posPath = Path()

    private var hourAngel = 0f
    private var minuteAngel = 0f
    private var secondsAngel = 0f

    private val timer: Timer

    private var hourPosWidth = 0f
    private var minutePosWidth = 0f
    private var secondPosWidth = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        // 表盘，刻度Paint
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = watchBorder.toFloat()

        // 指针Paint
        posPaint.style = Paint.Style.STROKE
        posPaint.strokeCap = Paint.Cap.ROUND

        timer = Timer()

        hourPosWidth = dp2px(5f).toFloat()
        minutePosWidth = dp2px(4f).toFloat()
        secondPosWidth = dp2px(2f).toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        rectF.set(
            width.toFloat() / 2 - watchRadius,
            height.toFloat() / 2 - watchRadius,
            width.toFloat() / 2 + watchRadius,
            height.toFloat() / 2 + watchRadius
        )

        // 表盘Path
        watchPath.addOval(rectF.left, rectF.top, rectF.right, rectF.bottom, Path.Direction.CW)
        val watchPathMeasure = PathMeasure(watchPath, false)

        // 分->刻度相关
        minutePath.addRect(0f, 0f, minuteWidth.toFloat(), minuteHeight.toFloat(), Path.Direction.CW)
        val minuteAdvance = watchPathMeasure.length / 60
        minutePathEffect = PathDashPathEffect(
            minutePath,
            minuteAdvance,
            minuteWidth.toFloat() / 2,
            PathDashPathEffect.Style.ROTATE
        )

        // 时->刻度相关
        hourPath.addRect(0f, 0f, hourWidth.toFloat(), hourHeight.toFloat(), Path.Direction.CW)
        val hourAdvance = watchPathMeasure.length / 12
        hourPathEffect =
            PathDashPathEffect(hourPath, hourAdvance, hourWidth.toFloat() / 2, PathDashPathEffect.Style.ROTATE)
    }


    override fun onDraw(canvas: Canvas) {

        // 绘制表盘
        paint.pathEffect = null
        canvas.drawPath(watchPath, paint)

        // 绘制分->刻度
        paint.pathEffect = minutePathEffect
        canvas.drawPath(watchPath, paint)

        // 绘制时->刻度
        paint.pathEffect = hourPathEffect
        canvas.drawPath(watchPath, paint)

        // 绘制时针
        drawHourPos(canvas)

        // 绘制分针
        drawMinutePos(canvas)

        // 绘制秒针
        drawSecondsPos(canvas)
    }

    private fun drawSecondsPos(canvas: Canvas) {
        canvas.save()
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat())
        canvas.rotate(secondsAngel)

        val radius = dp2px(4f).toFloat()

        posPath.reset()
        posPath.addCircle(0f, 0f, radius, Path.Direction.CW)
        posPath.moveTo(0f, -radius)
        posPath.lineTo(0f, -dp2px(100f).toFloat())

        posPaint.color = Color.parseColor("#B6B5B3")
        posPaint.strokeWidth = secondPosWidth
        canvas.drawPath(posPath, posPaint)

        canvas.restore()
    }

    private fun drawMinutePos(canvas: Canvas) {

        canvas.save()
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat())
        canvas.rotate(minuteAngel)

        posPath.reset()
        posPath.addCircle(0f, 0f, dp2px(4f).toFloat(), Path.Direction.CW)
        posPath.moveTo(0f, 0f)
        posPath.lineTo(0f, -dp2px(70f).toFloat())

        posPaint.color = Color.BLACK
        posPaint.strokeWidth = minutePosWidth
        canvas.drawPath(posPath, posPaint)

        canvas.restore()
    }

    private fun drawHourPos(canvas: Canvas) {

        canvas.save()
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat())
        canvas.rotate(hourAngel)

        posPath.reset()
        posPath.addCircle(0f, 0f, dp2px(4f).toFloat(), Path.Direction.CW)
        posPath.moveTo(0f, 0f)
        posPath.lineTo(0f, -dp2px(40f).toFloat())

        posPaint.color = Color.BLACK
        posPaint.strokeWidth = hourPosWidth
        canvas.drawPath(posPath, posPaint)

        canvas.restore()
    }

    private fun resetAngels() {
        val timeAngels = TimeUtils.getTimeAngels()

        hourAngel = timeAngels[0]
        minuteAngel = timeAngels[1]
        secondsAngel = timeAngels[2]

        invalidate()
    }

    fun start() {
        if (!timer.isRunning) {
            timer.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timer.stop()
    }

    private inner class Timer : Handler(), Runnable {

        var isRunning = false

        override fun run() {
            if (!isRunning) {
                return
            }
            resetAngels()
            postDelayed(this, 1000)
        }

        fun start() {
            stop()
            isRunning = true
            postDelayed(this, 1000)
        }

        fun stop() {
            isRunning = false
            removeCallbacks(this)
        }
    }

}