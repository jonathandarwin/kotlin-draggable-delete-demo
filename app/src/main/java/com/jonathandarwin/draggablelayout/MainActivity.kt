package com.jonathandarwin.draggablelayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var draggableLayoutParam : ViewGroup.LayoutParams? = null
    private var widthDevice = 0
    private var heightDevice = 0

    private var deleteX1 = 0f
    private var deleteY1 = 0f
    private var deleteX2 = 0f
    private var deleteY2 = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setDeviceSize()

        val scale = resources.displayMetrics.density + 0.5f
        draggableLayoutParam = ViewGroup.LayoutParams(48 * scale.toInt(), 48 * scale.toInt())

        draggableView.onDropListener { x, y ->
            setDeleteBoxCoordinate()

            if(x in deleteX1..deleteX2 && y in deleteY1..deleteY2){
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
                (draggableView.parent as ViewGroup).removeView(draggableView)
            }
        }

        draggableView.onMoveListener {
            if(it) {
                tvDelete.visibility = View.VISIBLE
                llBottom.setBackgroundColor(ResourcesCompat.getColor(resources, android.R.color.holo_red_dark, null))
            }
            else {
                tvDelete.visibility = View.GONE
                llBottom.setBackgroundColor(ResourcesCompat.getColor(resources, android.R.color.transparent, null))
            }
        }

        btnAdd.setOnClickListener {
            val view = DraggableView(this)
            view.layoutParams = draggableLayoutParam
            view.setImageResource(R.drawable.ic_android)

            var randomX = 0f
            var randomY = 0f

            setDeleteBoxCoordinate()

            Log.d("masuksiniga", view.width.toString())
            Log.d("masuksiniga", "${view.height}")


            do{
                // rendering view in safe X and Y coordinate (not exceed the screen)
                // and outside delete region
                randomX = Random.nextInt(widthDevice - 48 * scale.toInt()).toFloat()
                randomY = Random.nextInt(heightDevice - 100 * scale.toInt()).toFloat()
            }
            while(randomX in deleteX1..deleteX2 && randomY in deleteY1..deleteY2)

            view.x = randomX
            view.y = randomY

            (btnAdd.parent as ViewGroup).addView(view)

            view.onDropListener { x, y ->
                setDeleteBoxCoordinate()

                if(x in deleteX1..deleteX2 && y in deleteY1..deleteY2){
                    Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
                    (view.parent as ViewGroup).removeView(view)
                }
            }

            view.onMoveListener {
                if(it) {
                    tvDelete.visibility = View.VISIBLE
                    llBottom.setBackgroundColor(ResourcesCompat.getColor(resources, android.R.color.holo_red_dark, null))
                }
                else {
                    tvDelete.visibility = View.GONE
                    llBottom.setBackgroundColor(ResourcesCompat.getColor(resources, android.R.color.transparent, null))
                }
            }
        }
    }

    private fun setDeviceSize() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthDevice = displayMetrics.widthPixels
        heightDevice = displayMetrics.heightPixels
    }

    private fun setDeleteBoxCoordinate() {
        deleteX1 = llBottom.x
        deleteY1 = llBottom.y
        deleteX2 = llBottom.width + deleteX1
        deleteY2 = llBottom.height + deleteY1
    }
}
