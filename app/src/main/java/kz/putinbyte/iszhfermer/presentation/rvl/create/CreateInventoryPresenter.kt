package kz.putinbyte.iszhfermer.presentation.rvl.create

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.DicRvlInstrumentModel
import kz.putinbyte.iszhfermer.entities.animals.ListModel
import kz.putinbyte.iszhfermer.entities.animals.rvl.*
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.ui.rvl.create.bottomFragment.instruments.Units
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class CreateInventoryPresenter @Inject constructor(
    private val router: Router,
    private val iszhInteractor: RegAnimalInteractor,
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<CreateInventoryView>() {

    var animalId: Int? = null
    var saveRvl = SaveRvlInventoryModel()
    var modelSelectedElements = arrayListOf<ListModel>()

    var validateError: Boolean = false

    override fun attachView(view: CreateInventoryView?) {
        super.attachView(view)
        setDicRvlInstrument()
        setDicRvlBranch()
        setDoctor()
        loadSickness()
        setReceivedSampleName()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setAnimalsList()
    }

    fun setAnimalsList() {
        val animals = ArrayList<Animal>()
        modelSelectedElements.map {
            animals.add(Animal(it.id))
        }

        saveRvl.animals = animals
    }

    //Получение крови
    private fun setReceivedSampleName() {
        launch {
            val result = iszhInteractor.setReceivedSampleName()
            handleResult(result, {
                viewState.showResearchType(it.data)
            }) {
                handleError2(it, rm) { msg ->
                    viewState.showMessage(msg)
                }
            }
        }
    }

    //Филиал лаборатории и пунктов приема проб
    private fun setDicRvlBranch() {
        launch {
            val result = iszhInteractor.setDicRvlBranch()
            handleResult(result, {
                viewState.showDoctorType(it.data)
            }) {
                handleError2(it, rm) { msg ->
                    viewState.showMessage(msg)
                }
            }
        }
    }

    // TODO: 28.03.2022 Возможно будущем будут нужно отображать
    //Израсходованные материалы
    fun setDicRvlInstrument() {
        launch {
            val result = iszhInteractor.setDicRvlInstrument()
            handleResult(result, {
                viewState.showMaterialsType(stringFromResult(it.data), result = it.data)
            }) {
                handleError2(it, rm) { msg ->
                    viewState.showMessage(msg)
                }
            }
        }
    }

    //Доктор
    private fun setDoctor() {
        // TODO: 26.03.2022 Нужно разобраться откуда получать имя и индекс
        launch {
            try {
                val result = referencesInteractor.getDoctorType()
                viewState.showResultType(result.lists)
            } catch (e: Exception) {
                viewState.showMessage(e.message.toString())
            }
        }
    }

    // Сохранение описи РВЛ
    fun setSaveRvlInventory() {

        val r = saveRvl
        saveRvl
        saveRvl
        launch {
            validate(saveRvl)
            if (!validateError) {
                viewState.showLoader(true)
                val result = iszhInteractor.setSaveRvlInventoryModel(saveRvl)
                handleResult(result, { response ->
                    val body = response.data.body()
                    body
                    viewState.showMessage(rm.getString(R.string.success))
                    viewState.showLoader(false)
                    router.navigateTo(Screens.Rvl)
                }) {
                    handleError2(it, rm) { msg ->
                        viewState.showMessage(msg)
                        viewState.showLoader(false)
                    }
                }
            } else
                viewState.showMessage(rm.getString(R.string.fill_all_fields))
        }
    }

    private fun loadSickness() {
        launch {
            try {
                val result = referencesInteractor.getSicknesses()
                viewState.showDiseaseType(stringFromSickness(result), result)
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }
        }
    }

    private fun stringFromSickness(result: List<SicknessKindItem>): List<BaseFormat> {
        val newList = arrayListOf<BaseFormat>()
        for (item in result) {
            newList.add(BaseFormat(item.id, item.code, item.nameRu, item.nameKz))
        }
        return newList
    }

    private fun stringFromResult(result: List<DicRvlInstrumentModel>): List<BaseFormat> {
        val newList = arrayListOf<BaseFormat>()
        for (item in result) {
            newList.add(BaseFormat(item.id, item.unitName, item.nameRu, item.nameKz))
        }
        return newList
    }

    // Список ед измерения
    fun loadUnits() {
        launch {
            try {
                val result = referencesInteractor.getUnits()
                viewState.showAddUnitsDialog(result)
            } catch (e: Exception) {
                viewState.showMessage(e.message.toString())
            }
        }
    }

    // Добавление нового ед изм
    fun setUnits(units: Units) {
        viewState.showLoader(true)
        launch {
            try {
                val result = referencesInteractor.setUnits(units)
                handleResult(result, {
                    setDicRvlInstrument()
                    viewState.showMessage(rm.getString(R.string.success))
                    viewState.showLoader(false)
                }) {
                    handleError2(it, rm) { msg ->
                        viewState.showMessage(msg)
                        viewState.showLoader(false)
                    }
                }

            } catch (e: Exception) {
                viewState.showMessage(e.message.toString())
                viewState.showLoader(false)
            }
        }
    }

    private fun validate(saveRvl: SaveRvlInventoryModel) {
        viewState.resetError()
        viewState.showValidateError(saveRvl.sicknessWithTubes?.size == 0, "sickness")
        viewState.showValidateError(saveRvl.receivedSampleId == null, "sample")
        viewState.showValidateError(saveRvl.doctors?.size == 0, "doctor")
        viewState.showValidateError(saveRvl.rvlBranchId == 0, "branch")
        viewState.showValidateError(saveRvl.documentNumber.isNullOrEmpty(), "numDoc")
        viewState.showValidateError(saveRvl.documentDate == null, "docDate")
    }

}