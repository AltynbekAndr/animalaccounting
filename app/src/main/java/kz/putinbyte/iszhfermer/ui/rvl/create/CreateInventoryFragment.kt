package kz.putinbyte.iszhfermer.ui.rvl.create

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_create_inventory.*
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.animals.*
import kz.putinbyte.iszhfermer.entities.animals.rvl.*
import kz.putinbyte.iszhfermer.entities.db.DoctorTypes
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.extensions.afterTextChanged
import kz.putinbyte.iszhfermer.extensions.showToast
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.rvl.create.CreateInventoryPresenter
import kz.putinbyte.iszhfermer.presentation.rvl.create.CreateInventoryView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.rvl.create.bottomFragment.desease.DiseaseBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.rvl.create.bottomFragment.instruments.InstrumentsBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.rvl.create.rv.desease.DiseaseAdapter
import kz.putinbyte.iszhfermer.ui.views.IszhSpinner
import kz.putinbyte.iszhfermer.utils.LogUtils
import kz.putinbyte.iszhfermer.utils.MyUtils.tuDesiredDateDay
import kz.putinbyte.iszhfermer.utils.MyUtils.tuServerDate
import toothpick.Toothpick
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateInventoryFragment : BaseFragment(), CreateInventoryView {

    private val listDoctors: ArrayList<Int> = arrayListOf()
    private var brachId: Int? = null
    private var docNumber: String = ""
    val listSicknessW: ArrayList<SicknessWithTube> = arrayListOf()
    val listInstrumentsW: ArrayList<Instrument> = arrayListOf()

    override val layoutResId = R.layout.fragment_create_inventory

    companion object {

        fun newInstance() = CreateInventoryFragment()

        private const val CURRENT_OPERATOR_KEY = "opId"
        private const val CURRENT_MODEL_KEY = "model"

        fun newInstance(opId: Int?, list: ArrayList<ListModel>) = CreateInventoryFragment().apply {
            arguments = Bundle().apply {
                if (opId != null) {
                    putInt(CURRENT_OPERATOR_KEY, opId)
                }
                if (list.size != 0) {
                    putParcelableArrayList(CURRENT_MODEL_KEY, list)
                }
            }
        }
    }

    @ProvidePresenter
    fun providePresenter(): CreateInventoryPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(CreateInventoryPresenter::class.java)
            .apply {
                animalId = arguments?.getInt(CURRENT_OPERATOR_KEY)
                modelSelectedElements = arguments?.getParcelableArrayList(CURRENT_MODEL_KEY)!!
            }
    }

    lateinit var fields : List<FieldData>

    @InjectPresenter
    lateinit var presenter: CreateInventoryPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_create_inventory))
        setListeners()
        initInputValue()

        fields = listOf(
            FieldData("sickness",layoutCreate,FieldType.INPUT),
            FieldData("sample",createGetSampleTypeSpinner,FieldType.SPINNER),
            FieldData("doctor",layoutCreateDoctorType,FieldType.INPUT),
            FieldData("branch",createBranchTypeSpinner,FieldType.SPINNER),
            FieldData("numDoc",createNumDocEdit,FieldType.INPUT),
            FieldData("docDate",researchDateEdit,FieldType.INPUT)
        )

    }

    //Функция сбора онформиация полей для сохроения
    private fun initInputValue() {

        if (researchDateEdit.itemDateEdit.text != "") {
            presenter.saveRvl.documentDate =
                tuServerDate(researchDateEdit.itemDateEdit.text.toString())
        }
        createNumDocEdit.afterTextChanged {
            presenter.saveRvl.documentNumber = it
            docNumber = it
        }

        presenter.saveRvl.sampleDateEnd = tuDesiredDateDay(5)
        presenter.saveRvl.sampleDateStart = tuDesiredDateDay(0)

        createPathologicalDataEdit.afterTextChanged {
            presenter.saveRvl.autopsyData = it
        }
        createClinicPictureEdit.afterTextChanged {
            presenter.saveRvl.clinicalPicture = it
        }
        createPathologicalMaterialEdit.afterTextChanged {
            presenter.saveRvl.pathalogicalMaterial = it
        }
        createGuessDiagnosisEdit.afterTextChanged {
            presenter.saveRvl.presumptiveDiagnosis = it
        }
        createInThePresenceEdit.afterTextChanged {
            presenter.saveRvl.witnessText = it
        }

        if (createSicknessDate.itemDateEdit.text != "") {
            presenter.saveRvl.dateOfIllness =
                tuServerDate(createSicknessDate.itemDateEdit.text.toString())
        }
        if (createSendingMaterialDate.itemDateEdit.text != "") {
            presenter.saveRvl.dateOfSending =
                tuServerDate(createSendingMaterialDate.itemDateEdit.text.toString())
        }
        presenter.saveRvl.doctors = listDoctors
        presenter.saveRvl.sicknessWithTubes = listSicknessW
        presenter.saveRvl.instruments = listInstrumentsW

        createSaveButton.setOnClickListener {
            presenter.setSaveRvlInventory()
        }

        val builder = setAsteriskMark()
        asteriskCreateSickness.text = builder
        asteriskSampleTypeSpinner.text = builder
        asteriskDoctorType.text = builder
        asteriskBranchTypeSpinner.text = builder
        asteriskNumDocEdit.text = builder
        asteriskNumDocDateEdit.text = builder
        asteriskStartDate.text = builder
        asteriskEndDate.text = builder
        asteriskSicknessDate.text = builder
        asteriskDeadDate.text = builder
        asteriskSendingMaterialDate.text = builder
        asteriskThrowExceptionEdit.text = builder
        asteriskPathologicalMaterialEdit.text = builder
        asteriskPathologicalDataEdit.text = builder
        asteriskGuessDiagnosisEdit.text = builder
        asteriskClinicPictureEdit.text = builder

    }

    //Клики
    private fun setListeners() {
        createStartDate.itemDateButton.setOnClickListener {
            showDatePickerDialog(true, createStartDate)
        }
        researchDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog(true, researchDateEdit)
        }
        createSicknessDate.itemDateButton.setOnClickListener {
            showDatePickerDialog(true, createSicknessDate)
        }
        createDeadDate.itemDateButton.setOnClickListener {
            showDatePickerDialog(true, createDeadDate)
        }
        createSendingMaterialDate.itemDateButton.setOnClickListener {
            showDatePickerDialog(true, createSendingMaterialDate)
        }
    }

    //Болезни
    override fun showDiseaseType(item: List<BaseFormat>, result: List<SicknessKindItem>) {
        val listElements = ArrayList<BaseFormat>()
        layoutCreateSickness.setOnClickListener {
            initRecyclerDisease(
                item,
                recyclerViewCreateSickness,
                textCreate,
                layoutRecycler,
                listElements, "Болезни", result
            )
        }
        imageRecycler.setOnClickListener {
            initRecyclerDisease(
                item,
                recyclerViewCreateSickness,
                textCreate,
                layoutRecycler,
                listElements, "Болезни", result

            )
        }
    }

    // Отображения инструментов
    override fun showMaterialsType(item: List<BaseFormat>, result: List<DicRvlInstrumentModel>) {
        val listElements = ArrayList<BaseFormat>()
        layoutMaterialsType.setOnClickListener {
            initRecyclerInstruments(
                item,
                recyclerViewMaterialsType,
                textCreateMaterialsType,
                layoutRecyclerMaterialsType,
                listElements, "Инструменты", result

            )
        }
        imageRecyclerMaterialsType.setOnClickListener {
            initRecyclerInstruments(
                item,
                recyclerViewMaterialsType,
                textCreateMaterialsType,
                layoutRecyclerMaterialsType,
                listElements, "Инструменты", result

            )
        }
    }

    //Принимаемый образец
    override fun showResearchType(item: List<ReceivedSampleNameModel>) {
        fillingList(item, createGetSampleTypeSpinner)
        createGetSampleTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position != 0) {
                        validationWindows(item[position - 1])
                    } else {
                        validationWindows(ReceivedSampleNameModel())
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    //Зополение списка
    private fun fillingList(list: List<ReceivedSampleNameModel>, view: IszhSpinner) {
        val newList: ArrayList<BaseFormat> = arrayListOf()
        newList.add(BaseFormat(null, null, "Сделайте выбор", null))
        for (item in list) {
            newList.add(BaseFormat(item.id, item.code.toString(), item.nameRu, item.nameKz))
            if (newList.size - 1 == list.size) {
                view.items = newList
            }
        }
    }

    //Отоброжение врочей
    override fun showResultType(items: List<DoctorTypes>) {
        val listElements = ArrayList<BaseFormat>()
        val list: ArrayList<BaseFormat> = arrayListOf()
        for (mod in items) {
            val name = mod.lastName + " " + mod.middleName + " " + mod.firstName
            list.add(BaseFormat(mod.id, null, name, name))
        }
        layoutDoctorType.setOnClickListener {
            initRecyclerDisease(
                list,
                recyclerViewCreateDoctorType,
                textCreateDoctorType,
                layoutRecyclerDoctorType,
                listElements, "Врачи"
            )
        }

        imageRecyclerDoctorType.setOnClickListener {
            initRecyclerDisease(
                list,
                recyclerViewCreateDoctorType,
                textCreateDoctorType,
                layoutRecyclerDoctorType,
                listElements, "Врачи"
            )
        }
    }

    //Филиал лаборатории и пунктов приема проб
    override fun showDoctorType(item: List<DicRvlBranchModel>) {
        doctorTypeList(item, createBranchTypeSpinner)
        createBranchTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    presenter.saveRvl.rvlBranchId = item[position].id!!
                    brachId = item[position].id!!.toInt()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    //Зополение списка
    private fun doctorTypeList(list: List<DicRvlBranchModel>, view: IszhSpinner) {
        val newList: ArrayList<BaseFormat> = arrayListOf()
        newList.add(BaseFormat(0, null, "Сделайте выбор", null))
        newList.addAll(BaseFormat.toFormat(list)!!)
        view.items = newList
    }

    override fun showAddUnitsDialog(list: List<BaseFormat>) {

    }

    override fun initRecyclerView() {

    }

    //Диологовое окно для отобьрожения списков
    //Можно вынести в пользовательский интерфейс
    private fun initRecyclerDisease(
        items: List<BaseFormat>,
        recycler: RecyclerView,
        text: View,
        recyclerLay: View,
        elements: ArrayList<BaseFormat>,
        status: String? = null,
        result: List<SicknessKindItem>? = null,
    ) {
        var listElements: ArrayList<BaseFormat> = elements
        val bottomSheetDialogFragment =
            DiseaseBottomSheetFragment(object : DiseaseBottomSheetFragment.Listener {
                override fun setOnClickListener(item: ArrayList<BaseFormat>) {
                    listElements = item
                    if (status == "Врачи") {
                        item.forEach { listDoctors.add(it.id!!) }

                    } else if (status == "Болезни") {
                        listElements.forEach { itm ->
                            val res =
                                result!!.firstOrNull { it.nameRu == itm.nameRu && it.nameKz == itm.nameKz }
                            if (res != null) {
                                listSicknessW.add(
                                    SicknessWithTube(
                                        res.infective,
                                        res.parentId,
                                        res.isAnyRvlReason,
                                        res.id,
                                        res.code,
                                        res.nameRu,
                                        res.nameKz,
                                        null,
                                        1,
                                        false
                                    )
                                )
                            }
                        }
                    }
                    val adapterDos = DiseaseAdapter(object : DiseaseAdapter.Listener {
                        override fun deleteItem(item: BaseFormat) {
                            visibilityElements(listElements, text, recyclerLay)
                        }

                        override fun srtClickItem() {
                            initRecyclerDisease(items, recycler, text, recyclerLay, elements)
                        }
                    })
                    visibilityElements(listElements, text, recyclerLay)
                    adapterDos.update(listElements)
                    recycler.adapter = adapterDos
                }
            }, items, listElements)
        bottomSheetDialogFragment.isCancelable = false
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
    }

    // Заполнения списка инструментов
    private fun initRecyclerInstruments(
        items: List<BaseFormat>,
        recycler: RecyclerView,
        text: View,
        recyclerLay: View,
        elements: ArrayList<BaseFormat>,
        status: String? = null,
        result: List<DicRvlInstrumentModel>? = null,
    ) {
        var listElements: ArrayList<BaseFormat> = elements
        val bottomSheetDialogFragment =
            InstrumentsBottomSheetFragment(object : InstrumentsBottomSheetFragment.Listener {
                override fun setOnClickListener(item: ArrayList<BaseFormat>) {
                    listElements = item
                    if (status == "Инструменты") {
                        listElements.forEach { itm ->
                            listInstrumentsW.add(
                                Instrument(itm.id, itm.nameKz?.toInt())
                            )
                        }
                    }
                    val adapterDos = DiseaseAdapter(object : DiseaseAdapter.Listener {
                        override fun deleteItem(item: BaseFormat) {
                            visibilityElements(listElements, text, recyclerLay)
                        }

                        override fun srtClickItem() {
                            initRecyclerInstruments(items, recycler, text, recyclerLay, elements)
                        }
                    })
                    visibilityElements(listElements, text, recyclerLay)
                    adapterDos.update(listElements)
                    recycler.adapter = adapterDos
                }
            }, items, listElements)
        bottomSheetDialogFragment.isCancelable = false
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
    }

    //Функция остлежовиние списка.
    fun visibilityElements(item: ArrayList<BaseFormat>, text: View, recycler: View) {
        if (item.size == 0) {
            text.isVisible = true
            recycler.isVisible = false
        } else {
            text.isVisible = false
            recycler.isVisible = true
        }
    }

    // TODO: 28.03.2022 Нужна валидация полей
    //Функция скрывание полей
    //Занесина очиска полей при изменнеие логиики отоброжений
    fun validationWindows(item: ReceivedSampleNameModel) {
        presenter.saveRvl.isPathalogical = item.isPathalogical
        presenter.saveRvl.receivedSampleId = item.id
        if (item.isAbleDownloadAccompanyingReport != null
            && item.isAbleDownloadActReport != null
            && item.isAbleDownloadTubesReport != null
        ) {
            if (item.isAbleDownloadAccompanyingReport == true
                && item.isAbleDownloadActReport == true
                && item.isAbleDownloadTubesReport == true
            ) {
                createStartDate.isVisible = item.isPathalogical == false
                createEndDate.isVisible = item.isPathalogical == false
                //Уникальные
                createInThePresenceEdit.isVisible = true
                layoutMaterialsType.isVisible = true
                //Лишние поля
                createSicknessDate.isVisible = false
                createSicknessDate.itemDateEdit.text = ""
                createDeadDate.isVisible = false
                createDeadDate.itemDateEdit.text = ""
                createPathologicalDataEdit.isVisible = false
                createPathologicalDataEdit.setText("")
                createThrowExceptionEdit.isVisible = false
                createPathologicalDataEdit.setText("")
                createClinicPictureEdit.isVisible = false
                createClinicPictureEdit.setText("")
                createGuessDiagnosisEdit.isVisible = false
                createGuessDiagnosisEdit.setText("")
                createSendingMaterialDate.isVisible = false
                createSendingMaterialDate.itemDateEdit.text = ""
                createPathologicalMaterialEdit.isVisible = false
                createPathologicalMaterialEdit.setText("")
            } else {
                //Уникальные
                createSicknessDate.isVisible = true
                createDeadDate.isVisible = true
                createPathologicalDataEdit.isVisible = true
                createThrowExceptionEdit.isVisible = true
                createClinicPictureEdit.isVisible = true
                createGuessDiagnosisEdit.isVisible = true
                createSendingMaterialDate.isVisible = true
                createPathologicalMaterialEdit.isVisible = true
                //Лишние поля
                createInThePresenceEdit.isVisible = false
                createInThePresenceEdit.setText("")
                layoutMaterialsType.isVisible = false
                createStartDate.isVisible = false
                createEndDate.isVisible = false
            }
        } else {
            presenter.saveRvl = SaveRvlInventoryModel()
            presenter.setAnimalsList()
            presenter.saveRvl.doctors = listDoctors
            presenter.saveRvl.sicknessWithTubes = listSicknessW

            if (researchDateEdit.itemDateEdit.text != "") {
                presenter.saveRvl.documentDate =
                    tuServerDate(researchDateEdit.itemDateEdit.text.toString())
            }

            presenter.saveRvl.documentNumber = docNumber
            presenter.saveRvl.rvlBranchId = brachId

            createStartDate.isVisible = false
            createEndDate.isVisible = false
            createInThePresenceEdit.isVisible = false
            layoutMaterialsType.isVisible = false
            createSicknessDate.isVisible = false
            createDeadDate.isVisible = false
            createPathologicalDataEdit.isVisible = false
            createThrowExceptionEdit.isVisible = false
            createClinicPictureEdit.isVisible = false
            createGuessDiagnosisEdit.isVisible = false
            createSendingMaterialDate.isVisible = false
        }
    }

    //Заполняет поля датой
    fun showDatePickerDialog(isStart: Boolean, data: View) {

        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        val frontFormat = SimpleDateFormat(getString(R.string.app_front_date))

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view: DatePicker, year: Int, month: Int, day: Int ->
                startDate[year, month] = day

                if (isStart) {
                    data.itemDateEdit.text = frontFormat.format(startDate.time)
                    createEndDate.itemDateEdit.text = null
                    initInputValue()
                }
            },
            startDate[Calendar.YEAR],
            startDate[Calendar.MONTH],
            startDate[Calendar.DAY_OF_MONTH]
        )
        startDate.add(Calendar.DATE, -4)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.datePicker.minDate = startDate.timeInMillis
        datePickerDialog.show()

        createEndDate.itemDateButton.setOnClickListener {
            if (createStartDate.itemDateEdit.text.isEmpty()) {
                requireActivity().showToast("Выберите дату начало транспортировки")
                return@setOnClickListener
            }

            val fromDatePickerDialog2 = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    endDate[year, monthOfYear] = dayOfMonth
                    createEndDate.itemDateEdit.text = (frontFormat.format(endDate.time))

                },
                endDate[Calendar.YEAR],
                endDate[Calendar.MONTH],
                endDate[Calendar.DAY_OF_MONTH]
            )
            fromDatePickerDialog2.datePicker.minDate = startDate.timeInMillis
            fromDatePickerDialog2.datePicker.maxDate = System.currentTimeMillis()
            fromDatePickerDialog2.show()
        }
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                layoutRvl,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun resetError() {
        presenter.validateError = false
        baseResetError(fields)
    }

    override fun showValidateError(error: Boolean, idName: String) {
        if (error) {
            presenter.validateError = true
            baseValidateError(fields,idName)
        }
    }

    private fun setAsteriskMark(): SpannableStringBuilder {
        val colored = "*"
        val builder = SpannableStringBuilder()

        builder.append()
        val start = builder.length
        builder.append(colored)
        val end = builder.length

        builder.setSpan(
            ForegroundColorSpan(Color.RED), start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }
}