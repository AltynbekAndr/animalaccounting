package kz.putinbyte.iszhfermer.ui.diagnostic.list

import android.os.Bundle
import android.view.View
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_diagnostic_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.presentation.diagnostic.list.DiagnosticListPresenter
import kz.putinbyte.iszhfermer.presentation.diagnostic.list.DiagnosticListView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.diagnostic.PagerAdapter
import toothpick.Toothpick

class DiagnosticListFragment : BaseFragment(), DiagnosticListView {

    override val layoutResId = R.layout.fragment_diagnostic_list

    companion object {
        fun newInstance() = DiagnosticListFragment()

        const val CURRENT_KEY = "animalId"
        fun newInstance(animalId: Int?) = DiagnosticListFragment().apply {
            arguments = Bundle().apply {
                if (animalId != null) {
                    putInt(CURRENT_KEY, animalId)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: DiagnosticListPresenter

    @ProvidePresenter
    fun providePresenter(): DiagnosticListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(DiagnosticListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_KEY)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_diagnostic_list))
        setListeners()
    }

    private fun setListeners() {
        viewPager.adapter = presenter.animalId?.let { PagerAdapter(childFragmentManager, it) }
        diagnosticTabLayout.setupWithViewPager(viewPager)
    }
}