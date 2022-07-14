package kz.putinbyte.iszhfermer.presentation.deposit.current

import android.net.Uri
import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.animals.deposit.Deposit
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class DepositePresenter @Inject constructor(
    private val router: Router,
    private val iszhInteractor: RegAnimalInteractor,
    private val rm: ResourceManager
) : BasePresenter<DepositView>() {

    var animalId: Int? = null
    var validateError: Boolean = false

    val deposit = Deposit()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        animalId?.let {
            deposit.animalId = it
        }
//        deposit.fileId = 1581
    }

    fun onSaveClicked() {
        launch {
            validate(deposit)
            if (!validateError) {
                viewState.showLoader(true)
                val result = iszhInteractor.setDeposit(deposit)
                if (result?.isSuccessful!! && result.body() != null) {
                    viewState.showLoader(false)
                    viewState.showMessage(rm.getString(R.string.success))
                    router.exit()
                } else {
                    viewState.showLoader(false)
                    viewState.showMessage(result.message())
                }
            } else
                viewState.showMessage(rm.getString(R.string.fill_all_fields))
        }

    }

    private fun validate(deposit: Deposit) {
        viewState.resetError()
        viewState.showValidateError(deposit.contractStartDate.isEmpty(), "StartDate")
        viewState.showValidateError(deposit.contractEndDate.isEmpty(), "EndDate")
        viewState.showValidateError(deposit.borrowerId == 0, "Borrower")
        viewState.showValidateError(deposit.pledgeeId == 0, "Pledgee")
        viewState.showValidateError(deposit.registerNumber.isEmpty(), "RegisterNum")
        viewState.showValidateError(deposit.contractNumber.isEmpty(), "ContractNum")
        viewState.showValidateError(deposit.pledgeSum == 0.0, "Summ")
        viewState.showValidateError(deposit.fileId == 0, "File")
    }

    fun onPermissionDenied(string: String) {
        viewState.showMessage(string)
    }

    // TODO Запрос отправки файла
    fun onUploadFileClicked(uri: Uri) {

//        launch {
//            val result = iszhInteractor.uploadFile(uri.toFile())
//
//            handleResult(result,{res->
//                val fileId = res.data
//                fileId
//                fileId
//                fileId
//            }){
//                handleError2(it, rm) { msg ->
//                    viewState.showLoader(false)
//                    viewState.showMessage(msg)
//                }
//            }
//
//        }
    }
}