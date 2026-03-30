package com.asb.feedxa.data.repository

import com.asb.feedxa.data.api.ApiClient
import com.asb.feedxa.data.models.FeedbackData
import com.asb.feedxa.data.models.FeedbackResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * تنفيذ المستودع
 */
class FeedbackRepositoryImpl(
    private val apiKey: String,
    private val baseUrl: String
) : FeedbackRepository {

    private val apiService = ApiClient.getInstance(baseUrl)

    override suspend fun submitFeedback(feedbackData: FeedbackData): FeedbackResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.submitFeedback(apiKey, feedbackData)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        FeedbackResult.Success(body.message)
                    } else {
                        FeedbackResult.Error(Exception(body?.message ?: "Unknown error"))
                    }
                } else {
                    FeedbackResult.Error(HttpException(response))
                }
            } catch (e: IOException) {
                FeedbackResult.Error(Exception("Network error: ${e.message}", e))
            } catch (e: Exception) {
                FeedbackResult.Error(Exception("Unexpected error: ${e.message}", e))
            }
        }
    }

    override fun submitFeedbackFlow(feedbackData: FeedbackData): Flow<FeedbackResult> = flow {
        emit(FeedbackResult.Loading)
        val result = submitFeedback(feedbackData)
        emit(result)
    }
}