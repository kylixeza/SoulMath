package com.ramiyon.soulmath.data.source.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreUtil {
    const val DATA_STORE_NAME = "SOUL_MATH_DATA_STORE"
    val REMEMBER_ME = booleanPreferencesKey("rememberMe")
    val HAVE_RUN_APP_BEFORE = booleanPreferencesKey("isFirstTime")
    val STUDENT_ID = stringPreferencesKey("studentId")
}