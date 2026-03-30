package com.asb.test.core

import android.content.Context
import android.os.Build

/**
 * توفير معلومات الجهاز
 */
class DeviceInfoProvider(private val context: Context) {

    /**
     * الحصول على معلومات الجهاز
     */
    fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            deviceModel = getDeviceModel(),
            osVersion = getOsVersion(),
            appVersion = getAppVersion(),
            manufacturer = Build.MANUFACTURER,
            androidVersion = Build.VERSION.RELEASE,
            sdkVersion = Build.VERSION.SDK_INT.toString()
        )
    }

    private fun getDeviceModel(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}".trim()
    }

    private fun getOsVersion(): String {
        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }

    private fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "${packageInfo.versionName} (${packageInfo.longVersionCode})"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    data class DeviceInfo(
        val deviceModel: String,
        val osVersion: String,
        val appVersion: String,
        val manufacturer: String,
        val androidVersion: String,
        val sdkVersion: String
    )
}