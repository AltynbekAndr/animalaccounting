package kz.putinbyte.iszhfermer.presentation.pincode

import android.content.SharedPreferences
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.model.interactors.AuthInteractor
import kz.putinbyte.iszhfermer.model.system.IResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.utils.GsonUtils
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class PinCodePresenter @Inject constructor(
    private val router: Router,
    private val authInteractor: AuthInteractor,
    private val sharedPreferences: SharedPreferences,
    private val rm: IResourceManager,

    ) : BasePresenter<PinCodeView>() {

    var gsonUtils = GsonUtils()
    var firstPin: String = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        determinant()
    }

    private fun determinant() {
        val pinCode = sharedPreferences.getString("PIN_CODE", "")
        if (pinCode.isNullOrEmpty()){
            viewState.showCreateDialog()
            viewState.savePinCode()
        } else{
            viewState.showCheckDialog()
            viewState.checkPinCode(pinCode)
        }
    }

    fun savePinCode(pinCode: String){
        sharedPreferences.edit().putString("PIN_CODE", pinCode).apply()
        screenMain()
    }

    fun cleanAuthData() = authInteractor.clearAuthData()

    fun screenMain(){
        router.navigateTo(Screens.Main)
    }
}