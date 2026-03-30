package com.asb.feedxa.data.api

import com.asb.feedxa.data.models.FeedbackData
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/**
 * واجهة API للتواصل مع الخادم
 */
interface ApiService {

    @POST("api/v1/feedback")
    suspend fun submitFeedback(
        @Header("X-API-Key") apiKey: String,
        @Body feedback: FeedbackData
    ): Response<FeedbackResponse>

    @Multipart
    @POST("api/v1/feedback/upload-screenshot")
    suspend fun uploadScreenshot(
        @Header("X-API-Key") apiKey: String,
        @Part screenshot: MultipartBody.Part
    ): Response<ScreenshotResponse>
}

/**
 * استجابة إرسال الملاحظة
 */
data class FeedbackResponse(
    val success: Boolean,
    val message: String,
    val feedbackId: String? = null
)

/**
 * استجابة رفع الصورة
 */
data class ScreenshotResponse(
    val success: Boolean,
    val url: String? = null,
    val message: String? = null
)