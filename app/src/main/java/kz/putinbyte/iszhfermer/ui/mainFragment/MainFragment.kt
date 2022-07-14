package kz.putinbyte.iszhfermer.ui.mainFragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_main.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.mainPresenter.MainFragmentPresenter
import kz.putinbyte.iszhfermer.presentation.mainPresenter.MainFragmentView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import toothpick.Toothpick

class MainFragment : BaseFragment(), MainFragmentView {

    override val layoutResId = R.layout.fragment_main

    companion object {

        fun newInstance() = MainFragment()
    }

    @InjectPresenter
    lateinit var presenter: MainFragmentPresenter

    @ProvidePresenter
    fun providePresenter(): MainFragmentPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(MainFragmentPresenter::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.mainTitle))

        btn_count_animals.setOnClickListener {
            presenter.onAnimalsClicked()
        }

        btn_administration.setOnClickListener {
            presenter.textClick()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}