package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var text = ""
    private var processValue = 0f


    private val paint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 20f
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
                buttonState=ButtonState.Completed
            }
        })
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                text = resources.getResourceName(R.string.button_loading)
                paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
                buttonState=ButtonState.Loading
            }

            ButtonState.Loading -> {
                valueAnimator.start()
            }

            ButtonState.Completed -> {
                text = resources.getResourceName(R.string.button_name)
                paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
            }
        }
        invalidate()
    }


    init {
        text = resources.getResourceName(R.string.button_name)
    }


    fun setNewState(state: ButtonState) {
        buttonState = state
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(buttonState==ButtonState.Loading){
            //draw rectangle
            canvas?.drawRect(0f, 0f, widthSize * processValue, heightSize.toFloat(), paint)
            canvas?.drawText(text, 0f, 0f, paint)
            //draw circle
            val diameter= 20f
            val textWidth = paint.measureText(text)
            canvas?.translate((widthSize+textWidth)/2f+diameter,(heightSize-diameter)/2f)

            paint.color=ResourcesCompat.getColor(resources, R.color.colorAccent, null)
            canvas?.drawArc(0f, 0f, diameter, diameter, 0f, 360 * processValue, true, paint)
        }else{
            //draw rectangle
            canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
            canvas?.drawText(text, 0f, 0f, paint)
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