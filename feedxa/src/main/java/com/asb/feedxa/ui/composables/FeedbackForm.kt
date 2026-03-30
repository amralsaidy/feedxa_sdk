package com.asb.feedxa.ui.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.asb.feedxa.data.datasource.DeviceInfoProvider
import com.asb.feedxa.data.models.FeedbackType
import com.asb.feedxa.ui.viewmodel.FeedbackViewModel

@Composable
fun FeedbackForm(
    viewModel: FeedbackViewModel,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val feedbackType by viewModel.feedbackType.collectAsState()
    val message by viewModel.message.collectAsState()
    val screenshotUri by viewModel.screenshotUri.collectAsState()
    val contactPhone by viewModel.contactPhone.collectAsState()
    val contactWhatsApp by viewModel.contactWhatsApp.collectAsState()
    val contactEmail by viewModel.contactEmail.collectAsState()
    val isFormValid by viewModel.isFormValid.collectAsState()

    // Launcher لاختيار الصورة من المعرض
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.updateScreenshot(it)
        }
    }

    // Launcher لالتقاط صورة
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // يتم التعامل مع الصورة الملتقطة
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // عنوان النموذج
        Text(
            text = "إرسال ملاحظة",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Chips لاختيار نوع الملاحظة
        Text(
            text = "نوع الملاحظة",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(FeedbackType.values().toList()) { type ->
                FilterChip(
                    selected = feedbackType == type,
                    onClick = { viewModel.updateFeedbackType(type) },
                    label = { Text(type.displayName) },
                    modifier = Modifier
                )
            }
        }

        // حقل الرسالة
        OutlinedTextField(
            value = message,
            onValueChange = { viewModel.updateMessage(it) },
            label = { Text("الرسالة *") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            maxLines = 6,
            isError = message.isBlank() && !isLoading,
            supportingText = {
                if (message.isBlank() && !isLoading) {
                    Text("الرسالة مطلوبة")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // قسم الصورة المرفقة
        Text(
            text = "صورة (اختياري)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Photo, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("من المعرض")
            }

            Button(
                onClick = { /* تنفيذ التقاط الصورة */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Camera, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("التقاط صورة")
            }
        }

        // عرض الصورة المرفقة
        screenshotUri?.let { uri ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Screenshot preview",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { viewModel.updateScreenshot(null) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove screenshot",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // قسم معلومات التواصل
        Text(
            text = "معلومات التواصل (اختياري)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = contactPhone ?: "",
            onValueChange = { viewModel.updateContactPhone(it.ifEmpty { null }) },
            label = { Text("رقم الهاتف") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contactWhatsApp ?: "",
            onValueChange = { viewModel.updateContactWhatsApp(it.ifEmpty { null }) },
            label = { Text("رقم واتساب") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contactEmail ?: "",
            onValueChange = { viewModel.updateContactEmail(it.ifEmpty { null }) },
            label = { Text("البريد الإلكتروني") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // أزرار التحكم
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text("إلغاء")
            }

            Button(
                onClick = {
                    val deviceInfo = DeviceInfoProvider.getDeviceInfo(context)
                    viewModel.submitFeedback(deviceInfo)
                },
                modifier = Modifier.weight(1f),
                enabled = isFormValid && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("إرسال")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SuccessScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "تم الإرسال بنجاح!",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "شكراً لمساعدتك في تحسين التطبيق",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}