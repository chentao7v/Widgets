package me.chentao7v.utils

import android.content.res.Resources
import android.util.TypedValue


fun dp2px(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics).toInt()
}

/**
 * 将对应半径与角度转换为 x坐标
 */
fun radiusToX(angel: Int, radius: Int): Int {
    // 角度变为弧度：Math.toRadians
    // 弧度变为角度：Math.toDegrees
    val degrees = Math.toRadians(angel.toDouble())
    // radius * cos(a) = x长度
    return (Math.cos(degrees) * radius).toInt()
}

/**
 * 获取适配后的Camera Z轴坐标
 */
fun getZForCamera(): Float {
    return -4 * Resources.getSystem().displayMetrics.density
}

