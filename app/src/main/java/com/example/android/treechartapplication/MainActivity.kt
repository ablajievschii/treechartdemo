package com.example.android.treechartapplication

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testData = intArrayOf(2, 3, 4)
        val parent = TreeChart(this).apply {
            setBackgroundColor(Color.argb(
                255,
                240,
                240,
                240,
            ))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
//                resources.getDimensionPixelSize(R.dimen.size),
                resources.getDimensionPixelSize(R.dimen.size)
            )
        }
        parent.setData(testData)

        val button = Button(this).apply {
            text = "random data"
            setOnClickListener {
                parent.setData(IntArray(6) { (Math.random() * 10 + 1).toInt() })
//                parent.setData(testData)
            }
        }
        findViewById<LinearLayout>(R.id.container).apply {
            addView(parent)
            addView(button)
        }
    }
}