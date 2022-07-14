package kz.putinbyte.iszhfermer.ui.search

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.animals.search.SearchResponse
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.presentation.search.SearchPresenter
import kz.putinbyte.iszhfermer.presentation.search.SearchView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.add.owners.OwnersBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.search.dialogRegions.RegionsBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.search.empty.SearchEmptyBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.search.rv.SearchAdapter
import toothpick.Toothpick

class SearchFragment : BaseFragment(), SearchView {

    override val layoutResId = R.layout.fragment_search

    private var itemMode: ArrayList<SearchResponse.Lists> = arrayListOf()
    var pageIndex = 0

    companion object {

        fun newInstance() = SearchFragment()
        const val KATO_NAME_KEY = "name"
        const val KATO_KEY = "katoId"
        fun newInstance(katoId: Int?, katoName: String?) = SearchFragment().apply {
            arguments = Bundle().apply {
                if (katoId != null) putInt(KATO_KEY, katoId)
                if (katoName != null) putString(KATO_NAME_KEY, katoName)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter(): SearchPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(SearchPresenter::class.java)
    }

    lateinit var adapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_filters))
        initAdapters()
        setClickListeners()
    }

    private fun initAdapters() {
        adapter = SearchAdapter(this::onItemClick)
        searchListRec.adapter = adapter
        searchListRec.layoutManager = LinearLayoutManager(context)
    }

    private fun setClickListeners() {

        searchOkButton.setOnClickListener {
            presenter.loadFindAnimalList(0)
        }

        searchKatoButton.setOnClickListener {
            it.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                showAlertRegions()
            }, 500)
        }
        searchOwnerButton.setOnClickListener {
            it.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                showAlertOwners()
            }, 500)
        }

        searchScroller.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight && adapter.items?.size!! > 9) {
                pageIndex++
                presenter.loadFindAnimalList(pageIndex)
            }

        })

        searchShowButton.setOnClickListener {

            if (searchShowButton.text == getText(R.string.icon_hide)) {
                searchInjEdit.visibility = View.GONE
                asteriskKato.visibility = View.GONE
                searchKatoButton.visibility = View.GONE
                searchAnimalKindSpinner.visibility = View.GONE
                searchStatusSpinner.visibility = View.GONE
                searchOwnerButton.visibility = View.GONE
                searchShowButton.text = getText(R.string.icon_show)
            } else {
                searchInjEdit.visibility = View.VISIBLE
                asteriskKato.visibility = View.VISIBLE
                searchKatoButton.visibility = View.VISIBLE
                searchAnimalKindSpinner.visibility = View.VISIBLE
                searchStatusSpinner.visibility = View.VISIBLE
                searchOwnerButton.visibility = View.VISIBLE
                searchShowButton.text = getText(R.string.icon_hide)
            }
        }

        searchAnimalKindSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.onAnimalKindChanged(searchAnimalKindSpinner.items[position].id)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        searchStatusSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.onStatusChanged(searchStatusSpinner.items[position].id)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        searchInjEdit.afterTextChanged { presenter.inj = it.ifEmpty { null } }

        val builder = setAsteriskMark()
        asteriskKato.text = builder
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    override fun setList(list: ArrayList<SearchResponse.Lists>, search: Boolean) {
       if (search) itemMode = list else list.forEach { item -> itemMode.add(item) }
        adapter.items = itemMode
    }

    override fun showKatoName(katoName: String?) {
        searchKatoButton.text = katoName
    }

    override fun showKindOfAnimal(items: List<BaseFormat>) {
        searchAnimalKindSpinner.items = items
    }

    override fun showStatus(items: List<BaseFormat>) {
        searchStatusSpinner.items = items
    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showEmptyMessage() {
        val bottomSheetDialogFragment = SearchEmptyBottomSheetFragment()
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
    }

    private fun showAlertRegions() {
        val bottomSheetDialogFragment =
            RegionsBottomSheetFragment(object : RegionsBottomSheetFragment.Listener {
                override fun setOnClick(items: Region.AnimalAmountByKato) {
                    searchKatoButton.text = items.name
                    presenter.onKatoIdChanged(items.id)
                }
            })
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
        searchKatoButton.isEnabled = true
    }

    private fun showAlertOwners() {
        val bottomSheetDialogFragment =
            OwnersBottomSheetFragment(object : OwnersBottomSheetFragment.Listener {
                override fun setOnClick(items: Owners) {
                    if (items.fullName != null) {
                        searchOwnerButton.text = items.fullName
                    } else {
                        searchOwnerButton.text =
                            items.lastName.plus(" " + items.firstName + " " + items.middleName)
                    }
                    presenter.onOwnerChanged(items.id!!)
                }

            })
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
        searchOwnerButton.isEnabled = true
    }

    fun onItemClick(items: ArrayList<SearchResponse.Lists>?, position: Int) {
        items?.let {
            presenter.onItemClicked(items[position].id)
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