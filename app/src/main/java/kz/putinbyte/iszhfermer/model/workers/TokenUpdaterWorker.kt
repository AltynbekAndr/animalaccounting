package kz.putinbyte.iszhfermer.model.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kz.putinbyte.iszhfermer.model.data.server.Result as NetRes
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.model.interactors.AuthInteractor
import toothpick.Toothpick
import javax.inject.Inject

class TokenUpdaterWorker(
    context: Context,
    workerParameters: WorkerParameters
) :
    CoroutineWorker(context, workerParameters) {

    @Inject
    lateinit var authInteractor: AuthInteractor


    override suspend fun doWork(): Result {
        val appScope = Toothpick.openScope(Scopes.DATA_SCOPE)
        Toothpick.inject(this, appScope)
        val result = authInteractor.tokenUpdater()
        // TODO запустить обновитель токена
        return if(result is NetRes.Success) Result.success() else Result.retry()
    }
}