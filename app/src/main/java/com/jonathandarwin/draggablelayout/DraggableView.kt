package com.jonathandarwin.draggablelayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs

class DraggableView @JvmOverloads constructor(
    context : Context, attrs : AttributeSet? = null, defStyleAttr : Int = 0
): AppCompatImageView(context, attrs, defStyleAttr), View.OnTouchListener {

    private var dX : Float = 0f
    private var dY : Float = 0f
    private var dropListener : ((x : Float, y : Float) -> Unit)? = null
    private var moveListener : ((isMove : Boolean) -> Unit)? = null

    init {
        setOnTouchListener(this)
    }

    fun onDropListener(dropListener : (x : Float, y : Float) -> Unit) {
        this.dropListener = dropListener
    }

    fun onMoveListener(moveListener : (isMove : Boolean) -> Unit) {
        this.moveListener = moveListener
    }

    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        super.onTouchEvent(motionEvent)
        val parent = view?.parent as View

        when(motionEvent?.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x.minus(motionEvent.rawX) ?: 0f
                dY = view.y.minus(motionEvent.rawY) ?: 0f
            }
            MotionEvent.ACTION_MOVE -> {
                var x = motionEvent.rawX + dX
                var y = motionEvent.rawY + dY

                if(x < 0) x = 0f
                else if(x > parent.width - view.width) x = parent.width - view.width.toFloat()

                if(y < 0) y = 0f
                else if(y > parent.height - view.height) y = parent.height - view.height.toFloat()

                view.animate()
                    .x(x)
                    .y(y)
                    .setDuration(0)
                    .start()

                moveListener?.invoke(true)
            }
            MotionEvent.ACTION_UP -> {
                view.animate()
                    .x(view.x)
                    .y(view.y)
                    .setDuration(0)
                    .start()

                val distanceX = view.x - motionEvent.rawX
                val distanceY = view.y - motionEvent.rawY

                if(abs(distanceX) < 10 && abs(distanceY) < 10) return performClick()

                dropListener?.invoke(view.x, view.y)
                moveListener?.invoke(false)
            }
        }

        return true
    }
}