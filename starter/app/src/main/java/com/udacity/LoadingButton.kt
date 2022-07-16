package com.udacity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    var progress by Delegates.observable<Int>(0) { p, old, new ->

        if (new == 100) buttonState = ButtonState.Completed
        invalidate()
    }

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 60F
        color = Color.WHITE
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        invalidate()
        isClickable = (new == ButtonState.Completed)
        if (buttonState == ButtonState.Completed) progress = 0
    }

    private var rectDimens = Rect(0, 0, widthSize, heightSize)
    private var loadingText = "Download"

    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        super.performClick()
        val anim = ValueAnimator.ofInt(10, widthSize)

        anim.interpolator = DecelerateInterpolator (1f)
        anim.addUpdateListener { valueAnimator ->

            rectDimens.bottom = heightSize
            paint.color = Color.parseColor("#154c79")
            loadingText = "Loading..."
            rectDimens.right = valueAnimator.animatedValue as Int
            invalidate()


        }

        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                loadingText = "Download"
                rectDimens.right = 0
            }
        })
        anim.duration = 2500
        anim.start()

        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.setBackgroundColor(Color.parseColor("#e28743"))
        canvas?.drawRect(rectDimens, paint)
        canvas?.drawText(loadingText, widthSize / 2F, heightSize / 2F + 20F as Float, textPaint)

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