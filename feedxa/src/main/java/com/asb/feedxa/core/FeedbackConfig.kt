package com.asb.test.core

import android.content.Context

/**
 * إعدادات الـ SDK
 */
data class FeedbackConfig(
    val context: Context,
    val apiKey: String,
    var userName: String? = null,
    var userEmail: String? = null,
    var currentScreen: String? = null,
    var primaryColor: Int = 0xFF6200EE.toInt(), // اللون الأساسي
    var accentColor: Int = 0xFF03DAC5.toInt()    // لون التمييز
)