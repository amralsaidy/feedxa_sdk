package com.asb.feedxa.ui.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.asb.feedxa.core.FeedbackSDK
import com.asb.feedxa.ui.viewmodel.FeedbackViewModel
import kotlinx.coroutines.launch

/**
 * BottomSheet الرئيسي لنموذج الملاحظات
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackBottomSheet(
    viewModel: FeedbackViewModel = FeedbackSDK.viewModel,
    onDismiss: () -> Unit = { FeedbackSDK.dismiss() }
) {
    val showSheet by FeedbackSDK.isShowing

    // ✅ ضمان استدعاء show() عند دخول الـ Composable
    LaunchedEffect(Unit) {
        FeedbackSDK.show()
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            ),
            containerColor = FeedbackSDK.getThemeColors().surface
        ) {
            FeedbackFormContent(viewModel, onDismiss)
        }
    }
}

@Composable
fun FeedbackFormContent(
    viewModel: FeedbackViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val submitState by viewModel.submitState.collectAsState()

    // التعامل مع حالات الإرسال
    LaunchedEffect(submitState) {
        when (submitState) {
            is com.asb.feedxa.data.models.FeedbackResult.Success -> {
                // عرض شاشة النجاح ثم الإغلاق
                coroutineScope.launch {
                    // يمكن إظهار Snackbar أو Toast
                    android.widget.Toast.makeText(
                        context,
                        (submitState as com.asb.feedxa.data.models.FeedbackResult.Success).message,
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                    onDismiss()
                }
            }
            is com.asb.feedxa.data.models.FeedbackResult.Error -> {
                // عرض رسالة الخطأ
                val errorMessage = (submitState as com.asb.feedxa.data.models.FeedbackResult.Error).exception.message
                android.widget.Toast.makeText(
                    context,
                    errorMessage ?: "حدث خطأ أثناء الإرسال",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
            else -> {}
        }
    }

    if (submitState is com.asb.feedxa.data.models.FeedbackResult.Success) {
        SuccessScreen()
    } else {
        FeedbackForm(viewModel, onDismiss, submitState is com.asb.feedxa.data.models.FeedbackResult.Loading)
    }
}