package kz.putinbyte.iszhfermer

import android.app.Application
import iszhfermer.BuildConfig
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.di.modules.AppModule
import kz.putinbyte.iszhfermer.di.modules.DataModule
import timber.log.Timber
import toothpick.Toothpick

class App : Application() {
    //Comment for test commit
    override fun onCreate() {
        super.onCreate()

        initLogger()
        initAppScope()
        initDataScope()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initAppScope() {
        val appScope = Toothpick.openScope(Scopes.APP_SCOPE)
        appScope.installModules(
            AppModule(this)
        )
    }

    private fun initDataScope() {
        val dataScope = Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.DATA_SCOPE)
        dataScope.installModules(
            DataModule(
                this,
                BuildConfig.API_URL
            )
        )
    }
}