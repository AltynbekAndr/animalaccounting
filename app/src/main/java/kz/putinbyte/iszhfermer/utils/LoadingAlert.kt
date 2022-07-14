package kz.putinbyte.iszhfermer.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import iszhfermer.R

class LoadingAlert(private val activity: Activity) {
    private lateinit var dialog: AlertDialog

    init {
        try {
            val builder = AlertDialog.Builder(activity)
            val inflater = activity.layoutInflater
            val view = inflater.inflate(R.layout.alert_loading, null)
            builder.setView(view)
            builder.setCancelable(false)
            dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } catch (e: Exception) {
        }

    }

    fun show() {
        try {
            if (!dialog.isShowing){
                dialog.show()
            }
        } catch (e: Exception) {

        }
    }

    fun hide() {
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 500)
        } catch (e: Exception) {

        }
    }
}