package com.newolf.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.Timer
import java.util.TimerTask

/**
 * ======================================================================
 *
 *
 * @author : NeWolf
 * @version : 1.0
 * @since :  2024-12-23
 *
 * =======================================================================
 */
@SuppressLint("CustomViewStyleable")
class VerticalScrollTextView(
    private val context: Context,
    attrs: AttributeSet? = null
) : TextSwitcher(context, attrs),
    ViewSwitcher.ViewFactory, DefaultLifecycleObserver {
    companion object {
        const val TAG = "Wolf.VerticalScroll"
    }

    private var textSize: Float
    private var textColor: Int

    private var maxlines: Int
    private var ellipse: String?
    private var textStyle: Int
    private var animDirection: Int
    private var animDuration: Long
    private var intervalTime: Long

    private val dataList = mutableListOf<String>()

    private val _inAnimation: Animation = TranslateAnimation(
        Animation.ABSOLUTE, 0f,
        Animation.ABSOLUTE, 0f,
        Animation.RELATIVE_TO_PARENT, 1f,
        Animation.ABSOLUTE, 0f
    )

    private val _outAnimation: Animation = TranslateAnimation(
        Animation.ABSOLUTE, 0f,
        Animation.ABSOLUTE, 0f,
        Animation.ABSOLUTE, 0f,
        Animation.RELATIVE_TO_PARENT, -1f
    )

    init {
        //获取属性
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.VerticalScrollTextViewStyle)

        textSize = typedArray.getDimension(R.styleable.VerticalScrollTextViewStyle_textSize, 15f)

        textColor =
            typedArray.getColor(R.styleable.VerticalScrollTextViewStyle_textColor, Color.RED)

        maxlines = typedArray.getInt(R.styleable.VerticalScrollTextViewStyle_maxLines, 1)

        ellipse = typedArray.getString(R.styleable.VerticalScrollTextViewStyle_ellipsize)

        textStyle = typedArray.getInt(R.styleable.VerticalScrollTextViewStyle_textStyle, 0)

        animDirection =
            typedArray.getInt(R.styleable.VerticalScrollTextViewStyle_animDirection, 0)

        animDuration =
            typedArray.getInt(R.styleable.VerticalScrollTextViewStyle_duration, 500).toLong()
        intervalTime =
            typedArray.getInt(R.styleable.VerticalScrollTextViewStyle_intervalTime, 2000).toLong()

        typedArray.recycle()

        _inAnimation.duration = animDuration
        _outAnimation.duration = animDuration
        when (animDirection) {
            0 -> {
                createBottomToTopAnimation()
            }

            1 -> {
                createTopToBottomAnimation()
            }
        }
        setFactory(this)
        if (context is LifecycleOwner) {

            context.lifecycle.addObserver(this)
        }
    }


    override fun makeView(): View {
        val textView = TextView(context)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        textView.setTextColor(textColor)
        textView.maxLines = maxlines
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.layoutParams = LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        return textView
    }

    fun setDataList(theData: List<String>) {
        stopScroll()

        dataList.clear()
        dataList.addAll(theData)
        startScroll()
    }


    var isStart = false
        private set


    private var timer: Timer? = null
    var num = 0
    fun startScroll(intervalTime: Long = this.intervalTime) {
        if (dataList.size == 0) {
            Log.e(TAG, "startScroll: dataList.size == 0 , return")
            return
        }
        if (timer == null) {
            timer = Timer()
        }
        if (!isStart) {
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    num++
                    if (dataList.size > 0) {
                        this@VerticalScrollTextView.post {
                            Log.e(TAG, "run: num = $num")
                            setText(dataList[num % dataList.size])
                        }
                    }
                }

            }, 0, intervalTime)
            isStart = true
        }
    }

    fun stopScroll() {
        timer?.cancel()
        timer = null
        isStart = false
    }


    fun getCurrentShow(): String {
        return if (dataList.size > 0) {
            dataList[num % dataList.size]
        } else {
            ""
        }
    }


    private fun createBottomToTopAnimation() {
        inAnimation = _inAnimation
        outAnimation = _outAnimation
    }

    private fun createTopToBottomAnimation() {
        inAnimation = _outAnimation
        outAnimation = _inAnimation
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        startScroll()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        stopScroll()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }


}