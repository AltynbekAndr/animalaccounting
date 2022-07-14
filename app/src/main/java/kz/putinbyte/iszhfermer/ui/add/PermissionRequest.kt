package kz.putinbyte.iszhfermer.ui.add

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
class PermissionRequest {

    var context: Context? = null
    var activity: Activity? = null
    val REQUEST_ID_READ_WRITE_PERMISSION = 100


    constructor(context: Context?,activity: Activity?) {
        this.context = context
        this.activity = activity
    }

    fun getMyApplicationPermissions() {
        val managePermission = ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        );

        if (managePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity!!.requestPermissions(
                    arrayOf(
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    ),
                    REQUEST_ID_READ_WRITE_PERMISSION
                )
            }
        }
    }

    fun getManageStoragePermission(){
        val readPermission = ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )

        if(readPermission != PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", context!!.getPackageName(), null)
            intent.data = uri
            context!!.startActivity(intent)
        }
    }
}