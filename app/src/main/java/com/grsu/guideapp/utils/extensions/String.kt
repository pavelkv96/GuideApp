package com.grsu.guideapp.utils.extensions

import java.math.BigInteger
import java.security.MessageDigest

fun String.toMD5(): String {
    return MessageDigest.getInstance("MD5").run {
        BigInteger(1, digest(toByteArray())).toString(16).padStart(32, '0')
    }
}