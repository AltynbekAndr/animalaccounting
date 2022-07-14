package kz.putinbyte.iszhfermer.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Project Truck Crew
 * Package kz.putinbyte.iszhfermer.extensions
 *
 * Kotlin extensions for this project.
 *
 * Created by Emil Zamaldinov (aka piligrim) 04.08.2020
 * Copyright © 2020 TKO-Inform. All rights reserved.
 */

fun String?.emptyIfNull(): String {
    return if (this == "null" || this == "Null") {
        ""
    } else this ?: ""
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")