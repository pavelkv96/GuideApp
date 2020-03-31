package com.grsu.guideapp.fragments.splash

import kotlinx.coroutines.delay

class SplashRepository {

    suspend fun parseData() {
        delay(500)
    }

    suspend fun copyDatabase() {
        delay(200)
//        delay(2500)
    }

    suspend fun copyPhotoFromAssets() {
        delay(100)
//        delay(1000)
    }
}