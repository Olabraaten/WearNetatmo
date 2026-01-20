package solutions.silly.wearnetatmo.work

import android.content.ComponentName
import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import solutions.silly.wearnetatmo.complication.ComplicationProvider
import solutions.silly.wearnetatmo.repository.NetatmoRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit

class NetatmoSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val netatmoRepository = EntryPointAccessors.fromApplication(
            applicationContext,
            WorkerEntryPoint::class.java
        ).netatmoRepository()
        val result = netatmoRepository.getSelectedStation()
        if (result.isSuccess && result.getOrNull() != null) {
            val requester = ComplicationDataSourceUpdateRequester.create(
                context = applicationContext,
                complicationDataSourceComponent = ComponentName(
                    applicationContext,
                    ComplicationProvider::class.java
                )
            )
            requester.requestUpdateAll()
            return Result.success()
        }
        Timber.w(result.exceptionOrNull(), "Netatmo sync failed")
        return Result.success()
    }

    companion object {
        private const val UNIQUE_PERIODIC = "NetatmoPeriodicSync"
        private const val UNIQUE_ONE_TIME = "NetatmoOneTimeSync"

        fun enqueuePeriodic(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = PeriodicWorkRequestBuilder<NetatmoSyncWorker>(
                15,
                TimeUnit.MINUTES
            ).setConstraints(constraints).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun enqueueOneTime(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<NetatmoSyncWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_ONE_TIME,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerEntryPoint {
        fun netatmoRepository(): NetatmoRepository
    }
}
