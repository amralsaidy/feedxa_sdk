package com.asb.feedxademo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.asb.feedxa.core.FeedbackSDK
import com.asb.feedxa.ui.composables.FeedbackBottomSheet

// MainActivity.kt (Compose)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // تهيئة الـ SDK
        FeedbackSDK.init(
            context = applicationContext,
            apiKey = "your_api_key_here",
            baseUrl = "https://your-api.com"
        )

        setContent {
            MaterialTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            FeedbackSDK.setUserData(
                                name = "Ahmed Hassan",
                                email = "ahmed@example.com"
                            )
                            FeedbackSDK.setScreen("MainScreen")
                            FeedbackSDK.show()
                        }
                    ) {
                        Text("إرسال ملاحظة")
                    }
                }

                // إضافة الـ BottomSheet
                FeedbackBottomSheet()
            }
        }
    }
}