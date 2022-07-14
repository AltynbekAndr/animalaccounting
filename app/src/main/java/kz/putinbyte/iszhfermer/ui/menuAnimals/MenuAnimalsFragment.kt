package kz.putinbyte.iszhfermer.ui.menuAnimals

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_menu_animals.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.menuAnimals.MenuAnimalsFragmentPresenter
import kz.putinbyte.iszhfermer.presentation.menuAnimals.MenuAnimalsFragmentView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import toothpick.Toothpick

class MenuAnimalsFragment : BaseFragment(), MenuAnimalsFragmentView {

    override val layoutResId = R.layout.fragment_menu_animals
    private var networkInfo: NetworkInfo? = null

    companion object {

        fun newInstance() = MenuAnimalsFragment()
    }

    @InjectPresenter
    lateinit var presenter: MenuAnimalsFragmentPresenter

    @ProvidePresenter
    fun providePresenter(): MenuAnimalsFragmentPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(MenuAnimalsFragmentPresenter::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.registerReceiver(
            internetBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        (activity as MainView).setTitle(getString(R.string.title_menu_animals))

        initListeners()
    }

    private val internetBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val cm: ConnectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            networkInfo = cm.activeNetworkInfo
            presenter.updateInternetStatus(networkInfo?.isConnected ?: false)
        }
    }

    override fun onChangeInternetConnect(isConnected: Boolean) {
        menuSearchAnimalsButton.isClickable = isConnected
        menuRegisterRvlButton.isClickable = isConnected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.unregisterReceiver(internetBroadcastReceiver)
    }

    private fun initListeners() {

        menuRegisteredAnimalsButton.setOnClickListener {
            presenter.onAnimalsClicked()
        }

        menuSearchAnimalsButton.setOnClickListener {
            presenter.onSearchButtonClicked()
        }

        menuRegisterRvlButton.setOnClickListener {
            presenter.onRvlClicked()
        }

        menuOfflineButton.setOnClickListener {
            presenter.onOfflineOwnersClicked()
        }

    }
}