package kz.putinbyte.iszhfermer.presentation.main

import android.util.Log
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.animals.AnimalList
import kz.putinbyte.iszhfermer.model.interactors.AuthInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val router: Router,
    private val authInteractor: AuthInteractor,
    private val regAnimalInteractor: RegAnimalInteractor,
    private val referencesInteractor: ReferencesInteractor
) : BasePresenter<MainView>() {

    var isConnect = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.newRootScreen(Screens.MainFrag)
    }

    fun onLogOutConfirmed() {
        authInteractor.logout()
        router.newRootScreen(Screens.Login)
    }

    fun showUndeliveredAnimals() {
        launch {
            try {
                val result = regAnimalInteractor.getOfflineRegisteredAnimals()
                var animalList = listOf<AnimalList.Animals>()
                handleResult(result, { res ->
                    animalList = res.data.listAnimals.filter { !it.comment.isNullOrEmpty() }
                }) {
                }
                viewState.showAlert(animalList)
            } catch (e: Exception) {
                viewState.showAlert(listOf())
            }

        }
    }

    fun updateInternetStatus(connectionAvailable: Boolean) {
        isConnect = connectionAvailable
        if (connectionAvailable) {
            launch {
                regAnimalInteractor.sendUndeliveredData()
            }
        }
    }
    fun backHome() {
        router.navigateTo(Screens.Main)
    }

    fun loadUserInfoById() {
        launch {
            try {
                val allUsers = referencesInteractor.getAllUsers()
                allUsers
                val userId =
                    allUsers?.lists?.firstOrNull { it.login == referencesInteractor.phoneNumber }?.id ?: 0
                val result = userId.let { referencesInteractor.getUserbyId(it) }
                val lastName = result.lastName ?: ""
                val firstName = result.firstName ?: ""
                val middleName = result.middleName ?: ""
                result.fullName = "$lastName $firstName $middleName"
                viewState.showUserAlert(result)
            } catch (e: Exception) {
                Log.e("tag", e.message.toString())
            }

        }
    }

    fun onLogOutClicked() {
         viewState.showConfirmExitDialog()
    }

    fun onBackArrowPressed() {
        router.exit()
    }

    fun onAlertClicked(id: Int) {
        router.navigateTo(Screens.Detail(id))
    }

}