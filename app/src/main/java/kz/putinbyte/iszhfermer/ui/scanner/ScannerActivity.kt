package kz.putinbyte.iszhfermer.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.snackbar.Snackbar
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.ui.base.BaseActivity
import toothpick.Toothpick
import iszhfermer.R
import kotlinx.android.synthetic.main.activity_scanner.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.presentation.scanner.ScannerPresenter
import kz.putinbyte.iszhfermer.presentation.scanner.ScannerView
import kz.putinbyte.iszhfermer.utils.LogUtils

class ScannerActivity : BaseActivity(), ScannerView {

    override val layoutResId = R.layout.activity_scanner

    private lateinit var codeScanner: CodeScanner

    companion object {

        private const val requestCodeCameraPermission = 101
        fun newInstance() = ScannerActivity()

    }

    @InjectPresenter
    lateinit var presenter: ScannerPresenter

    @ProvidePresenter
    fun providePresenter(): ScannerPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(ScannerPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpPermission()
        codeScanner()
    }

    private fun codeScanner() {

        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    GlobalScope.launch {
                        presenter.decodeCode(it.text)
                    }
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Tag", "Camera error: ${it.message}")
                }
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setUpPermission() {
        val permission: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun showErrorMessage(message: String) {

        try {
            Snackbar.make(
                clScannerActivity,
                message,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCodeCameraPermission -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "Нужно дать разрешение на использование камеры",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //success
                }

            }
        }
    }

    override fun back() {
        finish()
    }
}