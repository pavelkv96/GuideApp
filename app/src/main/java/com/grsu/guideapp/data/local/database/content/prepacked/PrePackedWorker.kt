package com.grsu.guideapp.data.local.database.content.prepacked

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber

class PrePackedWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {
            try {
                //TODO code prepacked json data in database
                Result.success()
            } catch (e: Exception) {
                Timber.e(e, "Error prepacked database")
                Result.failure()
            }
        }
    }
}