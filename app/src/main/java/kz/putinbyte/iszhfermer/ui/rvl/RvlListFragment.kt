package kz.putinbyte.iszhfermer.ui.rvl

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_rvl_list.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.ListAnimalsModel
import kz.putinbyte.iszhfermer.entities.animals.ListModel
import kz.putinbyte.iszhfermer.entities.animals.TypeAnimalModel
import kz.putinbyte.iszhfermer.entities.db.places.Kato
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.rvl.RvlListPresenter
import kz.putinbyte.iszhfermer.presentation.rvl.RvlListView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.rvl.rv.RvlAdapter
import kz.putinbyte.iszhfermer.ui.views.IszhSpinner
import toothpick.Toothpick
import androidx.core.widget.NestedScrollView
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.ui.rvl.bottomFragment.RvlMessageBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.rvl.bottomFragment.SaveMessageBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.rvl.bottomFragment.SearchMessageBottomSheetFragment
import kz.putinbyte.iszhfermer.utils.AlinNotification

class RvlListFragment : BaseFragment(), RvlListView {

    private var idKa = -1
    private var animalLindCode = ""
    private var scrollInput = false
    private var animalSlaughter = true
    private var animalSlaughterScroll = true
    private var itemMode: ArrayList<ListModel> = arrayListOf()
    private var pageIndex = -1
    private var booleanAnimals = false
    private var booleanAreas = false

    val adapter = RvlAdapter(this::onItemClick)

    override val layoutResId = R.layout.fragment_rvl_list

    companion object {
        var animalID: Int? = null

        fun newInstance() = RvlListFragment()

        const val CURRENT_KEY = "animal"

        fun newInstance(animalId: Int?) = RvlListFragment().apply {
            arguments = Bundle().apply {
                if (animalId != null) {
                    putInt(CURRENT_KEY, animalId)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: RvlListPresenter

    @ProvidePresenter
    fun providePresenter(): RvlListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(RvlListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_KEY)
                animalID = animalId
            }
    }

    private var modelSelectedElements = ArrayList<ListModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_rvl))
        setListeners()
        initClick()
    }

    private fun initClick() {
        rvlSaveItemsIm.setOnClickListener {
            saveTransition()
        }

        rvlSendToRvlButton.setOnClickListener {
            saveTransition()
        }

        rvlAnimalSlaughter.setOnClickListener {
            alert.show()
            itemMode.clear()
            animalSlaughter = false
            presenter.gettingListAnimals(isSpk = true, isDead = true)
        }
    }

    private fun setListeners() {
        //Слушает прокрутку скрола.
        scroller.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY < oldScrollY) {
            }
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight && itemMode.size > 9) {
                val s = itemMode.size
                if (animalSlaughter) {
                    alert.show()
                    presenter.gettingListAnimals(
                        id = idKa,
                        animalLindCode = animalLindCode,
                        pageIndex = pageIndex,
                        pageSize = 10)
                    scrollInput = true
                } else {
                    presenter.gettingListAnimals(
                        isSpk = true, isDead = true,
                        pageIndex = pageIndex,
                        pageSize = 10)
                    animalSlaughterScroll = false
                }
            }
        })

        searchFloat.setOnClickListener {
            val bottomSheetDialogFragment = SearchMessageBottomSheetFragment(object :
                SearchMessageBottomSheetFragment.Listener {
                override fun setOnClickListener(item: ArrayList<ListModel>) {
                    item.forEach { i ->
                        if (modelSelectedElements.size != 0) {
                            if (modelSelectedElements.firstOrNull { it.id == i.id && it.inj == i.inj } == null) {
                                modelSelectedElements.add(i)
                            }
                        } else {
                            modelSelectedElements.add(i)
                        }
                        burialElements()
                    }
                }
            })
            bottomSheetDialogFragment.isCancelable = false
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,
                bottomSheetDialogFragment.tag)
        }
    }

    private fun saveTransition() {
        if (modelSelectedElements.size != 0){
            val bottomSheetDialogFragment = SaveMessageBottomSheetFragment(object :
                SaveMessageBottomSheetFragment.Listener {
                override fun savedData() {
                    if (modelSelectedElements.size != 0)
                        presenter.onAddClicked(animalID, modelSelectedElements)
                    else {
                        openMessage()
                    }
                }

                override fun setOnClearClickListener(boolean: Boolean, item: ListModel) {
                    if (!boolean) {
                        modelSelectedElements.remove(item)
                        burialElements()
                    }
                }
            }, modelSelectedElements)
            bottomSheetDialogFragment.isCancelable = false
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,
                bottomSheetDialogFragment.tag)
        }else{
            openMessage()
        }
    }

    //Открытие сообщения о пустоте списка
    fun openMessage(){
        val bottomSheetDialogFragment = RvlMessageBottomSheetFragment()
        bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag)
    }

    //Логика RecyclerView
    override fun setGettingListAnimals(list: ListAnimalsModel?) {
        if (list != null) {
            pageIndex++
            if (scrollInput && !list.lists.isNullOrEmpty()) {
                list.lists!!.forEach { im ->
                    val mod = itemMode.firstOrNull { it.id == im.id }
                    if (mod == null) {
                        itemMode.add((im))
                        adapter.addItem(im)
                    }
                }
            } else {
                if (list.lists!!.isEmpty()) {
                    Toast.makeText(requireContext(),
                        resources.getString(R.string.Upon_new_data_was_found),
                        Toast.LENGTH_LONG).show()
                }
                scrollInput = false
            }
        } else {
            scrollInput = false
        }

        if (!scrollInput && list != null) {
            if (list.lists!!.isNotEmpty()) {
                itemMode = list.lists as ArrayList<ListModel>
            }
            if (animalSlaughterScroll) {
                adapter.items = itemMode
                rvlListRec.adapter = adapter
            }
        }
        scrollInput = false
    }

    //Получение областей
    override fun setListKato(list: List<Kato>) {
        alert.show()
        rvlKatoObSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position != 0) {
                        booleanAreas = true
                        getSroValue()
                        presenter.getKato(list[position - 1].code, RvlListPresenter.Roles.REGION)
                    } else {
                        idKa = -1
                        // TODO: 08.04.2022 После тестов убрать
//                      animalLindCode = ""
                        booleanAreas = false
                        rvlKatoRgSpinner.isVisible = false
                        rvlKatoCiRgSpinner.isVisible = false
                        adapter.update(arrayListOf())
                        if (!booleanAnimals && !booleanAreas) {
                            alert.show()
                            presenter.gettingListAnimals()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        fillingList(list, rvlKatoObSpinner)
    }

    //Получение регионов
    override fun setKatoRegion(list: List<Kato>) {
        rvlKatoRgSpinner.isVisible = true
        rvlKatoRgSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position != 0) {
                        getSroValue()
                        presenter.getKato(list[position - 1].code, RvlListPresenter.Roles.CITE)
                    } else {
                        idKa = -1
                        animalLindCode = ""
                        rvlKatoCiRgSpinner.isVisible = false
                        adapter.update(arrayListOf())
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        fillingList(list, rvlKatoRgSpinner)
    }

    // Получение городов
    override fun setKatoCite(list: List<Kato>) {
        rvlKatoCiRgSpinner.isVisible = true
        rvlKatoCiRgSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position != 0) {
                        if (!scrollInput) {
                            alert.show()
                            getSroValue()
                            modelSelectedElements.clear()
                            idKa = list[position - 1].id
                            presenter.gettingListAnimals(
                                id = list[position - 1].id,
                                animalLindCode = animalLindCode
                            )
                            pageIndex = 0
                        }
                    } else {
                        idKa = -1
//                        animalLindCode = ""
                        adapter.update(arrayListOf())
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        fillingList(list, rvlKatoCiRgSpinner)
    }

    //Заполнение списка Spinner
    private fun fillingList(list: List<Kato>, view: IszhSpinner) {
        val newList: ArrayList<BaseFormat> = arrayListOf()
        newList.add(BaseFormat(null, null, "Сделайте выбор", null))
        for (item in list) {
            newList.add(BaseFormat(item.id, item.code.toString(), item.nameRu, item.nameKz))
            if (newList.size == list.size) {
                view.items = newList
            }
        }
    }

    //Вид животного
    override fun setTypeAnimal(list: List<TypeAnimalModel>) {
        alert.show()
        val newList: ArrayList<BaseFormat> = arrayListOf()
        rvlAnimalKindSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position != 0) {
                        if (!scrollInput) {
                            alert.show()
                            booleanAnimals = true
                            getSroValue()
                            modelSelectedElements.clear()
                            animalLindCode = list[position - 1].code.toString()
                            presenter.gettingListAnimals(
                                id = idKa,
                                animalLindCode = list[position - 1].code)
                            pageIndex = 0
                        }
                    } else {
                        alert.show()
                        // TODO: 08.04.2022 После тестов убрать
//                        idKa = -1
                        animalLindCode = ""
                        booleanAnimals = false
                        adapter.update(arrayListOf())
                        // TODO: 08.04.2022 После тестов убрать
//                        if (!booleanAnimals && !booleanAreas) {
//                            presenter.gettingListAnimals()
//                        }
                        presenter.gettingListAnimals(
                            id = idKa,
                            animalLindCode = animalLindCode)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        newList.add(BaseFormat(null, null, "Сделайте выбор", null))
        for (item in list) {
            newList.add(BaseFormat(item.id, item.code.toString(), item.nameRu, item.nameKz))
            if (newList.size == list.size) {
                rvlAnimalKindSpinner.items = newList
            }
        }
    }

    override fun setSuccessfulLoading(areas: Boolean?, animals: Boolean?, list: Boolean?) {
        if (areas!! && animals!! && list!!) {
            alert.hide()
        }
    }

    override fun setErrorMessage(text: String) {
        alert.hide()
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    override fun errorMessage() {
        alert.hide()
    }

    //Выполняет очистку общей структуры
    fun getSroValue() {
        itemMode.clear()
        animalSlaughter = true
        animalSlaughterScroll = true
    }

    private fun onItemClick(items: ListModel?, boolean: Boolean) {
        if (boolean) {
            if (items != null) {
                modelSelectedElements.add(items)
            }
        } else {
            val l = modelSelectedElements.indexOfFirst {
                it.inj == items?.inj && it.fullName == items?.fullName && it.birthDate == items?.birthDate
            }
            modelSelectedElements.removeAt(l)
        }

        burialElements()
    }

    fun burialElements() {
        if (modelSelectedElements.size != 0) {
            rvlSaveItemsIm.text = modelSelectedElements.size.toString()
            AlinNotification.isStopAnim(rvlSaveItemsIm)
            AlinNotification.animFlashing(rvlSaveItemsIm)
        } else {
            rvlSaveItemsIm.text = "0"
            AlinNotification.isStopAnim(rvlSaveItemsIm)
            presenter.gettingListAnimals(
                id = id,
                animalLindCode = animalLindCode
            )
        }
    }

    override fun onStart() {
        super.onStart()
        modelSelectedElements.clear()
    }
}