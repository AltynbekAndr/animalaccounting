package kz.putinbyte.iszhfermer.di.modules

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kz.putinbyte.iszhfermer.model.system.*
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class AppModule(
    context: Context
) : Module() {

    init {
        // Navigate
        val cicerone = Cicerone.create()
        bind(Context::class.java).toInstance(context)
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
        bind(IResourceManager::class.java).toInstance(ResourceManager(context))
        bind(IDirectoryManager::class.java).toInstance(DirectoryManager(context))

        // Gps
        bind(ILocationProvider::class.java).toInstance(DeviceLocationProvider(context))

        // Shared Preferences
        val sharedPreferences = context.getSharedPreferences("PPK_REGISTRY_APP", MODE_PRIVATE)
        bind(SharedPreferences::class.java).toInstance(sharedPreferences)

        // Context
//        bind(Context::class.java).toInstance(context)
    }
}