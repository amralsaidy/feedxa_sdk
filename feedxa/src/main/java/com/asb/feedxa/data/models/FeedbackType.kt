package com.asb.feedxa.data.models

import com.asb.feedxa.R

/**
 * أنواع الملاحظات
 */
enum class FeedbackType(val displayName: String, val icon: Int) {
    BUG("🐛 Bug", R.drawable.ic_bug),
    SUGGESTION("💡 Suggestion", R.drawable.ic_idea),
    COMPLAINT("⚠️ Complaint", R.drawable.ic_complaint);

    companion object {
        fun fromDisplayName(displayName: String): FeedbackType? {
            return values().find { it.displayName == displayName }
        }
    }
}