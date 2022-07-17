package com.udacity

import android.R.attr.angle
import android.R.attr.strokeWidth
import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    var progress by Delegates.observable<Int>(0) { _, _, new ->

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

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->

        invalidate()
        isClickable = (new == ButtonState.Completed)
        if (buttonState == ButtonState.Completed) progress = 0
    }

    private var rectDimens = Rect(0, 0, widthSize, heightSize)
    private var loadingText = "Download"

    val rect = RectF(
        widthSize -widthSize/2F + 800,
        widthSize/2F + 40 ,
        (200 + widthSize)/2F + 800,
        (200 + widthSize)/2F+ 40
    )

    val START_ANGLE_POINT = 270F;
    //Initial Angle (optional, it can be zero)

    //Initial Angle (optional, it can be zero)
    var angle = 0F


    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        super.performClick()
        isClickable = false
        val anim = ValueAnimator.ofInt(10, widthSize)

        anim.interpolator = DecelerateInterpolator (1f)
        anim.addUpdateListener { valueAnimator ->

            rectDimens.bottom = heightSize
            paint.color = Color.parseColor("#154c79")
            loadingText = "Loading..."
            rectDimens.right = valueAnimator.animatedValue as Int
            angle += 360 / (widthSize/10)
            invalidate()
        }

        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                loadingText = "Download"
                rectDimens.right = 0
                angle = 0F
                isClickable = true
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
        canvas?.drawText(loadingText, widthSize / 2F, heightSize / 2F + 20F, textPaint)
        canvas?.drawArc(rect, START_ANGLE_POINT, angle, true, circlePaint);
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