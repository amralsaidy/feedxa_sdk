package com.asb.feedxa.data.repository

import com.asb.feedxa.data.models.FeedbackData
import com.asb.feedxa.data.models.FeedbackResult
import kotlinx.coroutines.flow.Flow

/**
 * واجهة المستودع
 */
interface FeedbackRepository {

    suspend fun submitFeedback(feedbackData: FeedbackData): FeedbackResult

    fun submitFeedbackFlow(feedbackData: FeedbackData): Flow<FeedbackResult>
}