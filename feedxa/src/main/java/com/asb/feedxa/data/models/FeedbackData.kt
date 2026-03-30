package com.asb.feedxa.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * نموذج بيانات الملاحظة
 */
@Parcelize
data class FeedbackData(
    val type: FeedbackType,
    val message: String,
    val screenshotUri: String? = null,
    val userName: String? = null,
    val userEmail: String? = null,
    val contactPhone: String? = null,
    val contactWhatsApp: String? = null,
    val contactEmail: String? = null,
    val deviceInfo: DeviceInfo,
    val screenName: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable

/**
 * معلومات الجهاز
 */
@Parcelize
data class DeviceInfo(
    val model: String,
    val manufacturer: String,
    val osVersion: String,
    val sdkVersion: Int,
    val appVersion: String,
    val appVersionCode: Long
) : Parcelable

/**
 * نتيجة إرسال الملاحظة
 */
sealed class FeedbackResult {
    data class Success(val message: String) : FeedbackResult()
    data class Error(val exception: Exception) : FeedbackResult()
    object Loading : FeedbackResult()
}