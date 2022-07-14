package kz.putinbyte.iszhfermer.model.system

import com.google.firebase.crashlytics.FirebaseCrashlytics

class Logger: ILogger {

    override fun logException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}