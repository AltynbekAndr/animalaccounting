package kz.putinbyte.iszhfermer.utils

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.View
import iszhfermer.R

object AlinNotification{
   private val drawable = AnimationDrawable()

    fun animFlashing(view: View){
        val handler = Handler()
        drawable.addFrame(ColorDrawable(Color.RED), 1000)
        drawable.addFrame(ColorDrawable(Color.BLUE), 1000)
        drawable.isOneShot = false
        view.setBackgroundDrawable(drawable)
        handler.postDelayed({ drawable.start() }, 500)
    }

    fun isStopAnim(view: View){
        view.setBackgroundColor(view.resources.getColor(R.color.ppkPrimary))
        drawable.stop()
    }
}
