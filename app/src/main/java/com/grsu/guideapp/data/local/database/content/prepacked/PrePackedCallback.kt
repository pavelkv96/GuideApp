package com.grsu.guideapp.data.local.database.content.prepacked

import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase

class PrePackedCallback : Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
//        val request = OneTimeWorkRequestBuilder<PrePackedWorker>().build()
//        WorkManager.getInstance(App.getInstance()).enqueue(request)
    }
}