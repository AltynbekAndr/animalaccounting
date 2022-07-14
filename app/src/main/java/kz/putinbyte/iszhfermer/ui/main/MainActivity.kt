package kz.putinbyte.iszhfermer.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.Gravity.TOP
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.activity_main.*
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.AnimalList
import kz.putinbyte.iszhfermer.entities.requests.UserInfoList
import kz.putinbyte.iszhfermer.presentation.main.MainPresenter
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseActivity
import kz.putinbyte.iszhfermer.ui.main.validation.AlertAdapter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity : BaseActivity(), MainView {

    override val layoutResId = R.layout.activity_main
    private var networkInfo: NetworkInfo? = null

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(MainPresenter::class.java)
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator: Navigator =
        SupportAppNavigator(this, supportFragmentManager, R.id.mainFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toothpick.inject(this, Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.DATA_SCOPE))
        mainBackButton.setOnClickListener {
            if (!mainTitle.text.equals(getText(R.string.mainTitle)))
                presenter.onBackArrowPressed()
        }

        this.registerReceiver(
            internetBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        mainNotificationButton.setOnClickListener {
            showPopup(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetBroadcastReceiver)
    }

    override fun showConfirmExitDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.PPKTheme_Alert)).apply {
            setTitle(R.string.logOut_dialog_title)
            setMessage("Вы уверены, что хотите выйти из аккаунта?\nВсе неотправленные данные будут удалены!")
            setPositiveButton(R.string.logOut_dialog_yes) { _, _ ->
                presenter.onLogOutConfirmed()
            }
            setNegativeButton(R.string.logOut_dialog_cancel) { button, _ ->
                button.dismiss()
            }
        }
        builder.show()
    }

    private val internetBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val cm: ConnectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            networkInfo = cm.activeNetworkInfo
            presenter.updateInternetStatus(networkInfo?.isConnected ?: false)
        }
    }

    private fun showPopup(v: View) {
        PopupMenu(this, v).apply {
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.menu_home -> {
                        presenter.backHome()
                        true
                    }
                    R.id.menu_user -> {
                        presenter.loadUserInfoById()
                        true
                    }
                    R.id.menu_notification -> {
                        presenter.showUndeliveredAnimals()
                        true
                    }
                    R.id.menu_logout -> {
                        presenter.onLogOutClicked()
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.main)

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(this)

                mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            } catch (e: Exception) {
                Log.e("Main", "Error showing menu $e")
            }
            show()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()

        super.onPause()
    }

    override fun setTitle(title: String) {
        mainTitle.text = title
    }

    // Окно сотрудник
    override fun showUserAlert(userInfo: UserInfoList.UserInfo?) {

        val userRole = userInfo?.role?.nameRu
        val userRegion = userInfo?.orgStructure?.nameRu

        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.PPKTheme_Alert)).create()
        val view = layoutInflater.inflate(R.layout.custom_use_info_dialog, null)

        val fullName = view.findViewById<TextView>(R.id.userTitleText)
        fullName.text = userInfo?.fullName

        val role = view.findViewById<TextView>(R.id.userRoleText)
        role.text = userRole

        val regionName = view.findViewById<TextView>(R.id.userRegionText)
        regionName.text = userRegion

        builder.setView(view)
        val position: WindowManager.LayoutParams = builder.window!!.attributes
        position.gravity = TOP or Gravity.LEFT
        position.x = 250 //x position
        position.y = 200 //y position
        builder.show()
    }

    // Окно Уведомление
    override fun showAlert(list: List<AnimalList.Animals>?) {

        val titleText = if(list.isNullOrEmpty()) "Уведомлений нет" else "Список животных не прошедших регистрацию"
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.PPKTheme_Alert)).create()
        val view = layoutInflater.inflate(R.layout.custom_validation_dialog, null)
        builder.setTitle(titleText)
        builder.setView(view)

        val adapter = AlertAdapter { items, position ->
            presenter.onAlertClicked(items!![position].id)
            builder.dismiss()
        }

        adapter.items = list
        val recyclerView = view.findViewById<RecyclerView>(R.id.alertRecyclerView)
        recyclerView.adapter = adapter

        val position: WindowManager.LayoutParams = builder.window!!.attributes
        position.gravity = TOP or Gravity.LEFT
        position.x = 250 //x position
        position.y = 200 //y position
        builder.show()
    }
}