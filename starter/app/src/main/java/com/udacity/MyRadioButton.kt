package com.udacity

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet

class MyRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatRadioButton(context, attrs, defStyleAttr) {
    public var linkText = ""

    init {
        var a =
            context.obtainStyledAttributes(attrs, R.styleable.MyRadioButton, defStyleAttr, 0);
        linkText = a.getText(R.styleable.MyRadioButton_linkText).toString();
        a.recycle()
    }
}