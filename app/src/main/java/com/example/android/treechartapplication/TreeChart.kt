package com.example.android.treechartapplication

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.random.Random

class TreeChart @JvmOverloads constructor(
    private val ctx: Context,
    private val attr: AttributeSet? = null,
    private val defStyle: Int = 0
) : FrameLayout(ctx, attr, defStyle) {

    private var data = intArrayOf()
    private val dataMap = mutableMapOf<View, Rect>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        renderChildren(data,0, 0, width, height)
        for (i in 0..childCount) {
            val view = getChildAt(i)
            val rect = dataMap[view]
            rect?.let {
                val widthSpec = MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY)
                val heightSpec = MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY)
                view.measure(widthSpec, heightSpec)
            }
        }

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val rect = dataMap[view]
            rect?.let {
                view.layout(
                    it.left,
                    it.top,
                    it.left + it.width(),
                    it.top + it.height()
                )
            }
        }
    }

    fun setData(testData: IntArray) {
        data = testData
        dataMap.clear()
        removeAllViews()
    }

    /**
     * @param x position within the parent
     * @param y position within the parent
     * @param width current arr group width
     * @param height current arr group height
     */
    private fun renderChildren(arr: IntArray, x: Int, y: Int, width: Int, height: Int) {
        // base case
        if (arr.isEmpty()) return
        if (arr.size <= 2) {
            addChildren(arr, x, y, width, height)
            return
        }

        val total = arr.sum()
        val halfSum = total / 2
        val fraction = halfSum.toFloat() / total
        val mid = getMidPointIndex(arr, halfSum)
        val left = arr.copyOfRange(0, mid)
        val right = arr.copyOfRange(mid, arr.size)
        println("mid=$mid, arr.size=${arr.size}")
        if (width >= height) {
            // split horizontally
            val halfWidth = (fraction * width).toInt()

            renderChildren(left, x, y, halfWidth, height)
            renderChildren(right, x + halfWidth, y, width - halfWidth, height)
        } else {
            // split vertically
            val halfHeight = (fraction * height).toInt()

            // left -> top, right -> bottom
            renderChildren(left, x, y, width, halfHeight)
            renderChildren(right, x, y + halfHeight, width, height - halfHeight)
        }
    }

    private fun getMidPointIndex(arr: IntArray, halfSum: Int): Int {
        var summ = 0
        var mid = 1
        for (i in arr.indices) {
            if (summ >= halfSum) {
                mid = i - 1
                break
            }
            summ += arr[i]
        }

        return if (mid <= 0)
            1 else mid
    }

    private fun addChildren(arr: IntArray, x: Int, y: Int, width: Int, height: Int) {
        if (width >= height) {
            // place horizontally
            addChildrenHorizontal(arr, x, y, width, height)
        } else {
            // place vertically
            addChildrenVertical(arr, x, y, width, height)
        }
    }

    private fun addChildrenHorizontal(arr: IntArray, x: Int, y: Int, width: Int, height: Int) {
        val total = arr.sum().toFloat()
        var usedWidth: Int = 0
        arr.forEach { value ->
            val itemWidth = (value / total * width).toInt()
            val newX = x + usedWidth
            addChild(getChildView(value), Rect(
                newX,
                y,
                newX + itemWidth,
                y + height
            ))
            usedWidth += itemWidth
        }
    }

    private fun addChildrenVertical(arr: IntArray, x: Int, y: Int, width: Int, height: Int) {
        val total = arr.sum().toFloat()
        var usedHeight: Int = 0
        arr.forEach { value ->
            val itemHeight = (value / total * height).toInt()
            val newY = y + usedHeight
            addChild(getChildView(value), Rect(
                x,
                newY,
                x + width,
                newY + itemHeight
            ))
            usedHeight += itemHeight
        }
    }

    private fun addChild(view: View, rect: Rect) {
        addView(view)
        dataMap[view] = rect
    }

    private fun getChildView(value: Int): View {
        return TextView(context).apply {
            text = value.toString()
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            setBackgroundColor(
                Color.argb(
                    255,
                    Random.nextInt(256),
                    Random.nextInt(256),
                    Random.nextInt(256),
                )
            )
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
}