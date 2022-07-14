package kz.putinbyte.iszhfermer.presentation.deregister

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.Registration
import kz.putinbyte.iszhfermer.entities.animals.unregister.Unregister
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.model.interactors.CommonDataInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class UnregisterPresenter @Inject constructor(
    val referencesInteractor: ReferencesInteractor,
    val regAnimalInteractor: RegAnimalInteractor,
    val router: Router,
    private val commonDataInteractor: CommonDataInteractor,
    private val rm: ResourceManager
) : BasePresenter<UnregisterView>() {

    var animalsId: ArrayList<Int>? = null
    var injs: ArrayList<String>? = null
    var validateError: Boolean = false
    var iskato: Boolean = false
    var isOwner: Boolean = false
    var isDate: Boolean = false
    var isCommentEdit : Boolean = false
    var isCauseComment: Boolean = false
    var isChooseFile: Boolean = false
    var isSickness: Boolean = false
    var isCause: Boolean = false

    private lateinit var registrationCache: List<Registration>
    private lateinit var sicknessesCache: List<SicknessKindItem>
    val unregister = Unregister()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshLocalData()
    }

    private fun updateUI() {
        if (::registrationCache.isInitialized) BaseFormat.toFormat(registrationCache)
            ?.let { viewState.setCauseList(it) }
        if (::sicknessesCache.isInitialized) BaseFormat.toFormat(sicknessesCache)
            ?.let { viewState.showSicknessType(it) }
        viewState.showData(unregister)
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            registrationCache = commonDataInteractor.unRegister
            sicknessesCache = referencesInteractor.getSicknesses()
            updateUI()
            viewState.showLoader(false)
        }
    }

    override fun attachView(view: UnregisterView?) {
        super.attachView(view)
        updateUI()
    }

    fun saveClicked() {

        launch {
            try {
                validate(unregister)
                if (!validateError) {
                    viewState.showLoader(true)
                    animalsId.let { ids ->
                        val unregister = ids?.map { unregister.copy(animalId = it) }
                        val result = referencesInteractor.setUnregister(unregister!!)
                        handleResult(result, {
                            viewState.showLoader(false)
                            viewState.showMessage(rm.getString(R.string.unregister_success))
                            router.exit()
                        }) {
                            handleError2(it, rm) { msg ->
                                viewState.showLoader(false)
                                viewState.showMessage(msg)
                            }
                        }
                    }
                }else{
                    viewState.showMessage(rm.getString(R.string.fill_all_fields))
                }

            } catch (e: Exception) {
                Timber.e(e)
                e.printStackTrace()
            }
        }
    }
    fun setKato(katoId: Int){
        unregister.newKatoId = katoId
        regAnimalInteractor.katoId = katoId
    }

    fun validate(unregister: Unregister) {
        viewState.resetError()
        viewState.showValidateError(unregister.newKatoId == 0 && iskato,"newKato")
        viewState.showValidateError(unregister.newOwnerId == 0 && isOwner, "newOwner")
        viewState.showValidateError(unregister.cause == 0 && isCause, "cause")
        viewState.showValidateError(unregister.cause == 0, "cause")
        viewState.showValidateError(unregister.sicknessId == 0 && isSickness, "sickness")
        viewState.showValidateError(unregister.endDate.isEmpty() && isDate, "date")
        viewState.showValidateError(unregister.causeComment.isEmpty() && isCauseComment, "causeComment")
    }

    fun onCauseChanged(code: String?) {
        iskato = code in arrayOf("UnReg_Givig","UnReg_MovingOwnerAnimal","UnReg_TransferSPK","UnReg_Inheritance","UnReg_Rental")
        isOwner = code in arrayOf("UnReg_Givig","UnReg_Rental","UnReg_Inheritance","UnReg_TransferSPK")
        isSickness =  code in arrayOf("UnReg_CattleDeath", "UnReg_SanitarySlaughter")
        isCommentEdit = code in arrayOf("UnReg_Rental" ,"UnReg_Loss","UnReg_TransferSPK","UnReg_ReturnOwner")
        isCauseComment = code in arrayOf("UnReg_SlaughterForSale")
        isDate  = code in arrayOf("UnReg_Rental", "UnReg_TransferSPK")
        isChooseFile = code in arrayOf("UnReg_Withdrawal",  "UnReg_SlaughterForSale","UnReg_CattleDeath", "UnReg_SanitarySlaughter")
        viewState.showVisibility()
    }

}