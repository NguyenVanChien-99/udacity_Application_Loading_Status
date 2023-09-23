package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var text = ""
    private var processValue = 0f

    private var colorPrimary =0
    private var colorPrimaryDark =0
    private var colorAccent =0
    private var colorWhite =0


    private val paint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
        style = Paint.Style.FILL
        typeface = Typeface.MONOSPACE
        textAlign = Paint.Align.CENTER
        textSize = 60f
    }

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 2000
        addUpdateListener {
            processValue = this.animatedValue as Float
            invalidate()
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                this@LoadingButton.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationStart(animation)
                this@LoadingButton.isEnabled = true
                buttonState = ButtonState.Completed
            }
        })
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> {
                text = resources.getString(R.string.button_loading)
                buttonState = ButtonState.Loading
            }

            ButtonState.Loading -> {
                valueAnimator.start()
            }

            ButtonState.Completed -> {
                text = resources.getString(R.string.button_name)
            }
        }
        invalidate()
    }


    init {
        text = resources.getString(R.string.button_name)
        context.withStyledAttributes(attrs,R.styleable.LoadingButton){
            colorPrimary= getColor(R.styleable.LoadingButton_colorPrimary,0)
            colorPrimaryDark= getColor(R.styleable.LoadingButton_colorPrimaryDark,0)
            colorAccent= getColor(R.styleable.LoadingButton_colorAccent,0)
            colorWhite= getColor(R.styleable.LoadingButton_colorWhite,0)
        }
    }


    fun setNewState(state: ButtonState) {
        buttonState = state
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //draw rectangle
        paint.color = colorPrimary
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = colorWhite
        canvas?.drawText(text, widthSize.toFloat() / 2, heightSize.toFloat() / 2 + 10f, paint)

        //draw the new one
        if (buttonState == ButtonState.Loading) {
            //draw rectangle
            paint.color = colorPrimaryDark
            canvas?.drawRect(0f, 0f, widthSize * processValue, heightSize.toFloat(), paint)
            paint.color = colorWhite
            canvas?.drawText(text, widthSize.toFloat() / 2, heightSize.toFloat() / 2 + 10f, paint)
            //draw circle
            val diameter = 60f
            val textWidth = paint.measureText(text)
            canvas?.translate((widthSize + textWidth) / 2f + diameter, (heightSize - diameter) / 2f)

            paint.color = colorAccent
            canvas?.drawArc(0f, 0f, diameter, diameter, 0f, 360 * processValue, true, paint)
        }
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