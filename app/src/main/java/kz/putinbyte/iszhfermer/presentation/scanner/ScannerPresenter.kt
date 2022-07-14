package kz.putinbyte.iszhfermer.presentation.scanner

import iszhfermer.R
import kz.putinbyte.iszhfermer.entities.animals.ScanData
import kz.putinbyte.iszhfermer.entities.db.Identity
import kz.putinbyte.iszhfermer.model.interactors.CommonDataInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.utils.LogUtils
import ru.terrakok.cicerone.Router
import java.lang.Exception
import javax.inject.Inject

class ScannerPresenter @Inject constructor(
    private val router: Router,
    private val regAnimalInteractor: RegAnimalInteractor,
    private val referencesInteractor: ReferencesInteractor,
    private val commonDataInt: CommonDataInteractor,
    private val rm: ResourceManager
) : BasePresenter<ScannerView>() {

    private val TAG = javaClass.simpleName

    suspend fun decodeCode(code: String) {
        try {
            val explodeData = code.toCharArray()
            var typeIdentification: Identity? = null
            var inj = ""

            when {
                code.length == 14 -> {
                    typeIdentification = referencesInteractor.getTypeIdentifications()
                        .firstOrNull { it.code == "BR" }
                }
                code.length == 15 -> {
                    typeIdentification = referencesInteractor.getTypeIdentifications()
                        .firstOrNull { it.code == "CH" }
                }
                else -> {
                    viewState.showMessage(rm.getString(R.string.scan_error_dialog_title))
                    regAnimalInteractor.scanData = null
                    viewState.back()
                }
            }

            val scanRegion =
                commonDataInt.decRegions.firstOrNull { it.number == explodeData[3]+explodeData[4].toString() }

            val kindOfAnimal = referencesInteractor.getKindsOfAnimals()
                .firstOrNull { it.code == commonDataInt.decAnimalKinds[explodeData[5].digitToInt()] }

            explodeData.drop(6).takeLast(13).forEach { inj += it.toString() }


            if (scanRegion != null && kindOfAnimal != null && typeIdentification != null) {
                val injText = "KZ".plus(scanRegion.code2 +explodeData[5]+ inj)
                val result = ScanData(
                    inj = injText,
                    causeRegistration = commonDataInt.registration.firstOrNull { code == "Reg_Offspring" },
                    typeIdentification = typeIdentification,
                    animalKind = kindOfAnimal,
                    kato = referencesInteractor.getRegions().firstOrNull{ it.code == scanRegion.code},
                    country = referencesInteractor.getCountries().firstOrNull{ it.code == "KZ"}
                )
                regAnimalInteractor.scanData = result
                viewState.back()

            }
        } catch (e: Exception) {
            LogUtils.error(TAG, e.message)
            viewState.showMessage(rm.getString(R.string.scan_error_dialog_title))
            viewState.back()
        }
    }

}