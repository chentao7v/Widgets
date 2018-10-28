package me.chentao7v.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author leo
 */

class TimeUtils {

    companion object {

        private val format = SimpleDateFormat("HH:mm:ss")

        fun getTimeAngels(): FloatArray {
            val currentTimeMillis = System.currentTimeMillis()
            val date = Date(currentTimeMillis)

            val times = format.format(date)
                .split(":")
                .map { it.toInt() }


            val hour = times[0]
            val min = times[1]
            val second = times[2]

            Log.d("Seven", "$times")


            val hourAngel = if (hour >= 12) {
                (hour - 12).toFloat() / 12 * 360f + min.toFloat() / 60 * (360 / 12)
            } else {
                hour.toFloat() / 12 * 360f + min.toFloat() / 60 * (360 / 12)
            }

            val minAngel = min.toFloat() / 60f * 360f + second.toFloat() / 60 * (360 / 60)
            val secondAngel = second.toFloat() / 60f * 360f

            val floatArrayOf = floatArrayOf(hourAngel, minAngel, secondAngel)

            Log.w("Seven", "${floatArrayOf.toList()}")


            return floatArrayOf
        }

    }

}
