package kz.putinbyte.iszhfermer.ui.login

import android.app.Dialog
import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.activity_login.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.presentation.login.LoginPresenter
import kz.putinbyte.iszhfermer.presentation.login.LoginView
import kz.putinbyte.iszhfermer.ui.base.BaseActivity
import kz.putinbyte.iszhfermer.ui.pincode.PinCodeFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.Toothpick
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginView {

    override val layoutResId = R.layout.activity_login
    private var networkInfo: NetworkInfo? = null

    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter(): LoginPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(LoginPresenter::class.java)
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator: Navigator = SupportAppNavigator(this, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.registerReceiver(
            internetBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        Toothpick.inject(this, Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.DATA_SCOPE))
        setListeners()
    }

    private val internetBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val cm: ConnectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            networkInfo = cm.activeNetworkInfo
            presenter.isConnect = networkInfo?.isConnected ?: false
        }
    }

    private fun setListeners() {
        loginSignInButton.setOnClickListener {
            withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, {
                presenter.onSignInClicked(
                    loginEmailEdit.text.toString(),
                    loginPassEdit.text.toString()
                )
            }, {
                presenter.onPermissionDenied(getString(R.string.app_gps_permission_denied))
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetBroadcastReceiver)
    }

    override fun setEmailFieldEmptyError() {
        loginEmailEdit.error = getString(R.string.required_fields_error)
    }

    override fun setPasswordFieldEmptyError() {
        loginPassEdit.error = getString(R.string.required_fields_error)
    }

    override fun setLoginFieldsEmptyError() {
        loginEmailEdit.error = getString(R.string.required_fields_error)
        loginPassEdit.error = getString(R.string.required_fields_error)
    }

    override fun setLoadingState(value: Boolean) {
        if (value)
            alert.show()
        else
            alert.hide()

        loginSignInButton.isEnabled = !value
        loginEmailEdit.isEnabled = !value
        loginPassEdit.isEnabled = !value
    }

    override fun showErrorMessage(ex: Throwable) {
        Snackbar.make(
            clLoginActivity,
            R.string.login_error_snackbar,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.login_error_snackbar_action) {
                presenter.onErrorDetailedInfoClicked(ex)
            }
            .show()
    }

    override fun showErrorDialog(ex: Throwable) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.login_error_dialog_title)
            .setMessage(ex.stackTrace.toString())
            .setNegativeButton(R.string.login_error_dialog_negative) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .setPositiveButton(R.string.login_error_dialog_positive) { _: DialogInterface, _: Int ->
                presenter.onErrorDetailedInfoClicked(ex)
            }
    }

    override fun showErrorMessage(message: String) {
        try {
            Snackbar.make(
                clLoginActivity,
                message,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }

    override fun onResume() {
        super.onResume()

        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()

        super.onPause()
    }


    override fun setAuthData(username: String?, pass: String?) {
        loginEmailEdit.setText(username!!)
        loginPassEdit.setText(pass!!)
    }

    override fun showViews() {
        loginPassEdit.visibility = View.VISIBLE
        loginEmailEdit.visibility = View.VISIBLE
        loginSignInButton.visibility = View.VISIBLE
    }

    override fun showPinDialog() {
        val bottomSheetDialogFragment = PinCodeFragment()
        bottomSheetDialogFragment.show(
            this.supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
    }

}