package kz.putinbyte.iszhfermer.ui.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import iszhfermer.R
import moxy.MvpAppCompatActivity
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import kz.putinbyte.iszhfermer.utils.LoadingAlert
import kz.putinbyte.iszhfermer.utils.LogUtils
import java.lang.Exception

abstract class BaseActivity : MvpAppCompatActivity(), BaseView {

    lateinit var alert: LoadingAlert

    companion object {

        const val REQUEST_CODE_PERMISSIONS = 333
    }

    abstract val layoutResId: Int

    private var permissionsForRequest: Pair<String, Pair<() -> Unit, () -> Unit>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        if (layoutResId != 0) setContentView(layoutResId)
        alert = LoadingAlert(this)
    }


    fun withPermission(permission: String, granted: () -> Unit, denied: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            granted()
        } else {
            permissionsForRequest = Pair(permission, Pair(granted, denied))
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun showMessage(msg: String) {
        try {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName, e.message)
        }

    }
}