package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    var progress by Delegates.observable<Int>(0) { p, old, new ->
        // Toast.makeText(context, "Progress changed:" + new.toString(), Toast.LENGTH_SHORT).show()
        if (new == 100) buttonState = ButtonState.Completed
        invalidate()
    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        // Toast.makeText(context, "button state changed " +buttonState.toString(), Toast.LENGTH_SHORT).show()
        invalidate()
        isClickable = (new == ButtonState.Completed)
        if (buttonState == ButtonState.Completed) progress = 0
    }

    private var rectDimens = Rect(0, 0, widthSize, heightSize)

    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        super.performClick()
        val anim = ValueAnimator.ofInt(100, widthSize)
        anim.addUpdateListener { valueAnimator ->
            rectDimens.bottom = heightSize
            paint.color = Color.GRAY
            rectDimens.right = valueAnimator.animatedValue as Int
            invalidate()
        }
        anim.duration = 700
        anim.start()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        this.setBackgroundColor(Color.GREEN)
        canvas?.drawRect(rectDimens, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}