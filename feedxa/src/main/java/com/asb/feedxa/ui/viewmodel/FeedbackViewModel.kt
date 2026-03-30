package com.asb.feedxa.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asb.feedxa.data.models.FeedbackData
import com.asb.feedxa.data.models.FeedbackResult
import com.asb.feedxa.data.models.FeedbackType
import com.asb.feedxa.data.repository.FeedbackRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel لإدارة حالة النموذج
 */
class FeedbackViewModel(
    private val repository: FeedbackRepository
) : ViewModel() {

    // حالة النموذج
    private val _feedbackType = MutableStateFlow<FeedbackType>(FeedbackType.SUGGESTION)
    val feedbackType: StateFlow<FeedbackType> = _feedbackType.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _screenshotUri = MutableStateFlow<Uri?>(null)
    val screenshotUri: StateFlow<Uri?> = _screenshotUri.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _contactPhone = MutableStateFlow<String?>(null)
    val contactPhone: StateFlow<String?> = _contactPhone.asStateFlow()

    private val _contactWhatsApp = MutableStateFlow<String?>(null)
    val contactWhatsApp: StateFlow<String?> = _contactWhatsApp.asStateFlow()

    private val _contactEmail = MutableStateFlow<String?>(null)
    val contactEmail: StateFlow<String?> = _contactEmail.asStateFlow()

    private val _screenName = MutableStateFlow<String?>(null)
    val screenName: StateFlow<String?> = _screenName.asStateFlow()

    // حالة الإرسال
    private val _submitState = MutableStateFlow<FeedbackResult?>(null)
    val submitState: StateFlow<FeedbackResult?> = _submitState.asStateFlow()

    // حالة التحقق من صحة البيانات
    val isFormValid: StateFlow<Boolean> = combine(
        _message,
        _feedbackType
    ) { message, _ ->
        message.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun updateFeedbackType(type: FeedbackType) {
        _feedbackType.value = type
    }

    fun updateMessage(message: String) {
        _message.value = message
    }

    fun updateScreenshot(uri: Uri?) {
        _screenshotUri.value = uri
    }

    fun updateContactPhone(phone: String?) {
        _contactPhone.value = phone
    }

    fun updateContactWhatsApp(whatsapp: String?) {
        _contactWhatsApp.value = whatsapp
    }

    fun updateContactEmail(email: String?) {
        _contactEmail.value = email
    }

    fun updateUserData(name: String?, email: String?) {
        _userName.value = name
        _userEmail.value = email
    }

    fun updateScreenInfo(screenName: String?) {
        _screenName.value = screenName
    }

    fun submitFeedback(deviceInfo: com.asb.feedxa.data.models.DeviceInfo) {
        viewModelScope.launch {
            val feedbackData = FeedbackData(
                type = _feedbackType.value,
                message = _message.value,
                screenshotUri = _screenshotUri.value?.toString(),
                userName = _userName.value,
                userEmail = _userEmail.value,
                contactPhone = _contactPhone.value,
                contactWhatsApp = _contactWhatsApp.value,
                contactEmail = _contactEmail.value,
                deviceInfo = deviceInfo,
                screenName = _screenName.value
            )

            repository.submitFeedbackFlow(feedbackData)
                .collect { result ->
                    _submitState.value = result
                }
        }
    }

    fun resetState() {
        _feedbackType.value = FeedbackType.SUGGESTION
        _message.value = ""
        _screenshotUri.value = null
        _contactPhone.value = null
        _contactWhatsApp.value = null
        _contactEmail.value = null
        _submitState.value = null
    }
}