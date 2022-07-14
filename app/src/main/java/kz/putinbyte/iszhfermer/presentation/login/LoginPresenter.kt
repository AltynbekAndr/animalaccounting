package kz.putinbyte.iszhfermer.presentation.login

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.network.AuthResponse
import kz.putinbyte.iszhfermer.model.data.server.Result
import kz.putinbyte.iszhfermer.model.interactors.AuthInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.IResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.utils.ConnectivityUtils
import javax.inject.Inject

class LoginPresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val referencesInteractor: ReferencesInteractor,
        private val sharedPreferences: SharedPreferences,
        private val rm: IResourceManager,
        private val context: Context

) : BasePresenter<LoginView>() {

    var isConnect = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        val username =
                sharedPreferences.getString("USERNAME", "")
        val password = sharedPreferences.getString("PASSWORD", "")

        if (!username.isNullOrEmpty() && !password.isNullOrEmpty())
            launch { loginByPinCode(username, password) }
        else {
            viewState.showViews()
        }
    }

    private suspend fun loginByPinCode(username: String, password: String) {
        viewState.setLoadingState(true)
        if (!isConnect) {
            viewState.setLoadingState(false)
            showPinCode()
            viewState.showViews()
        } else {
            val authResult = auth(username, password)
            handleResult(authResult, {
                viewState.setLoadingState(false)
                showPinCode()
                viewState.showViews()
            }) {
                viewState.setLoadingState(false)
                handleError2(it, rm) { msg ->
                    authInteractor.clearAuthData()
                    viewState.showErrorMessage(msg)
                    viewState.showViews()
                }
            }
        }
    }

    private fun showPinCode() = viewState.showPinDialog()

    fun onSignInClicked(login: String, password: String) {
        when {
            login == "" && password == "" -> viewState.setLoginFieldsEmptyError()
            login == "" -> viewState.setEmailFieldEmptyError()
            password == "" -> viewState.setPasswordFieldEmptyError()
            else -> {
                viewState.setLoadingState(true)
                launch {
                    val authResult = auth(login, password, true)
                    handleResult(authResult, {
                        viewState.setLoadingState(false)
                        showPinCode()
                    }) {
                        viewState.setLoadingState(false)
                        handleError2(it, rm) { msg ->
                            viewState.showErrorMessage(msg)
                        }
                    }
                }

            }
        }
    }

    fun onPermissionDenied(s: String) {
        viewState.showMessage(s)
        viewState.showViews()
    }

    fun onErrorDetailedInfoClicked(ex: Throwable) {
        viewState.showErrorDialog(ex)
    }

    private suspend fun auth(
            username: String,
            password: String,
            DataSync: Boolean = false
    ): Result<AuthResponse> {
        val result = authInteractor.login(
                username,
                password
        )
        if (result is Result.Success) {
            authInteractor.saveAuthData(username, password, result.data.accessToken)
            if (DataSync || ConnectivityUtils.syncAvailability(context = context)) referencesInteractor.dataSynchronization()
        }
        return result
    }
}