package com.nna.customfancontroller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private enum class FanSpeed(val label : Int){
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high);

    fun next() = when (this){
         OFF -> LOW
         LOW -> MEDIUM
         MEDIUM -> HIGH
         HIGH -> OFF
    }
}
private const val RADIUS_OFFSET_LABEL = 30
private const val RADIUS_OFFSET_INDICATOR = -35

class DialView @JvmOverloads constructor(
    context: Context,
    attribut: AttributeSet,
    defStyleAttributeSet: Int = 0
) : View (context, attribut, defStyleAttributeSet) {
    private var radius = 0.0f
    private var fanSpeed = FanSpeed.OFF

    private val pointPosition : PointF = PointF(0.0f,0.0f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("",Typeface.BOLD)
    }
    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        fanSpeed = fanSpeed.next()
        contentDescription = resources.getString(fanSpeed.label)

        invalidate()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(w,h) / 2.0 * 0.8).toFloat()
    }

    private fun  PointF.computeXYForSpeed(pos : FanSpeed,radius : Float){
        val startAngel = Math.PI * (9 / 8.0)
        val angel = startAngel + pos.ordinal * (Math.PI / 4)
        x = (radius * cos(angel)).toFloat() + width /2
        y = (radius * sin(angel)).toFloat() + height /2


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//      set dial background
        paint.color = if (fanSpeed == FanSpeed.OFF) Color.GRAY else Color.GREEN
//      set dial
        canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
//      draw dial indicator
        val markerRadius = radius + RADIUS_OFFSET_INDICATOR
        pointPosition.computeXYForSpeed(fanSpeed,markerRadius)
        paint.color = Color.BLACK
        canvas?.drawCircle(pointPosition.x, pointPosition.y, radius/12 , paint)
//      draw label
        val labelRaadius = radius + RADIUS_OFFSET_LABEL
        for (i in FanSpeed.values()) {
            pointPosition.computeXYForSpeed(i, labelRaadius)
            val label = resources.getString(i.label)
            canvas?.drawText(label, pointPosition.x, pointPosition.y, paint)
        }
    }





}