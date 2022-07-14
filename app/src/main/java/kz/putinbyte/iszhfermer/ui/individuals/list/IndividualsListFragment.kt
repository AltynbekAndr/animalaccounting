package kz.putinbyte.iszhfermer.ui.individuals.list

import android.os.Bundle
import android.view.View
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_individuals_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.iszh.Reference
import kz.putinbyte.iszhfermer.presentation.individuals.list.IndividualsListPresenter
import kz.putinbyte.iszhfermer.presentation.individuals.list.IndividualsListView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.individuals.IndividualPagerAdapter
import toothpick.Toothpick

class IndividualsListFragment : BaseFragment(), IndividualsListView {

    private var animalID: Int? = null

    override val layoutResId = R.layout.fragment_individuals_list

    companion object {
        fun newInstance() = IndividualsListFragment()

        const val CURRENT_KEY = "animalId"
        fun newInstance(animalId: Int?) = IndividualsListFragment().apply {
            arguments = Bundle().apply {
                if (animalId != null) {
                    putInt(CURRENT_KEY, animalId)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: IndividualsListPresenter

    @ProvidePresenter
    fun providePresenter(): IndividualsListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(IndividualsListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_KEY)
                animalID = animalId
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_individ_list))

        setListeners()
    }

    override fun showContTypes(items: List<Reference>) {

    }

    private fun setListeners() {
        individViewPager.adapter = IndividualPagerAdapter(childFragmentManager)
        individTabLayout.setupWithViewPager(individViewPager)
    }

}