package com.asb.feedxa.data.datasource

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.asb.feedxa.data.models.DeviceInfo

/**
 * مزود معلومات الجهاز
 */
object DeviceInfoProvider {

    fun getDeviceInfo(context: Context): DeviceInfo {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

        return DeviceInfo(
            model = "${Build.MANUFACTURER} ${Build.MODEL}",
            manufacturer = Build.MANUFACTURER,
            osVersion = Build.VERSION.RELEASE,
            sdkVersion = Build.VERSION.SDK_INT,
            appVersion = packageInfo.versionName ?: "Unknown",
            appVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        )
    }
}