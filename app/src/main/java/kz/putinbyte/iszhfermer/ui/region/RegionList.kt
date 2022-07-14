
package kz.putinbyte.iszhfermer.ui.region

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_region_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.KatoResponse
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.region.RegionListPresenter
import kz.putinbyte.iszhfermer.presentation.region.RegionView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.region.rv.RegionAdapter
import toothpick.Toothpick

class RegionList : BaseFragment(), RegionView {

    override val layoutResId = R.layout.fragment_region_list

    companion object {

        fun newInstance() = RegionList()

        private const val CURRENT_CODE_KEY = "code"
        private const val CURRENT_KATO_KEY = "katoId"

        fun newInstance(code:Int,katoId:Int) = RegionList().apply {
            arguments = Bundle().apply {
                putInt(CURRENT_CODE_KEY,code)
                putInt(CURRENT_KATO_KEY,katoId)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: RegionListPresenter
    lateinit var adapter: RegionAdapter

    var katoId:Int? = null

    @ProvidePresenter
    fun providePresenter(): RegionListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(RegionListPresenter::class.java).apply {
                code = arguments?.getInt(CURRENT_CODE_KEY)
                kato = arguments?.getInt(CURRENT_KATO_KEY)
                katoId = kato

            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_region))

        initAdapter()
        initListeners()
    }

    private fun initAdapter(){
        adapter = RegionAdapter(this::onItemClicked,this::onAddItemClicked)


        region_rv.adapter = adapter
        region_rv.layoutManager = LinearLayoutManager(context)
    }
    private fun initListeners(){

    }

    override fun setList(list: List<KatoResponse.AnimalAmountByKato>) {
        adapter.items = list
    }

    private fun onItemClicked(items: List<KatoResponse.AnimalAmountByKato>?, position: Int){
        presenter.onOwnersClicked(items!![position].id, katoId!!)
    }

    private fun onAddItemClicked(items: List<KatoResponse.AnimalAmountByKato>?, position: Int){
        presenter.onAddClicked(items!![position].id)
    }
}