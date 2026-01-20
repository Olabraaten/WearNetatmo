package solutions.silly.wearnetatmo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import solutions.silly.wearnetatmo.work.NetatmoSyncWorker
import timber.log.Timber

@HiltAndroidApp
class WearNetatmoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        NetatmoSyncWorker.enqueuePeriodic(this)
    }
}
