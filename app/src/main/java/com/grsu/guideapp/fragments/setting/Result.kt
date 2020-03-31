package com.grsu.guideapp.fragments.setting

sealed class Result

object PreStart : Result()
object Start : Result()
class Update(val title: String, val message: String) : Result()
object Finish : Result()