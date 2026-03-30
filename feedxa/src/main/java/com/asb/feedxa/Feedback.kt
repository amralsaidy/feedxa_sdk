package com.asb.feedxa.core

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.asb.feedxa.data.repository.FeedbackRepository
import com.asb.feedxa.data.repository.FeedbackRepositoryImpl
import com.asb.feedxa.ui.viewmodel.FeedbackViewModel

/**
 * الكلاس الرئيسي لـ SDK
 * يستخدم نمط Singleton لتوفير نقطة دخول واحدة
 */
object FeedbackSDK {

    private var isInitialized = false
    private lateinit var config: FeedbackConfig
    private lateinit var repository: FeedbackRepository
    internal lateinit var viewModel: FeedbackViewModel

    // حالة عرض الـ BottomSheet
    internal val isShowing = mutableStateOf(false)

    // بيانات المستخدم الاختيارية
    private var userName: String? = null
    private var userEmail: String? = null
    private var currentScreen: String? = null

    /**
     * تهيئة الـ SDK
     * @param context سياق التطبيق
     * @param apiKey مفتاح API الخاص بالخدمة
     * @param baseUrl رابط API الأساسي
     * @param themeColors ألوان مخصصة للواجهة (اختياري)
     */
    fun init(
        context: Context,
        apiKey: String,
        baseUrl: String = "https://api.example.com",
        themeColors: FeedbackThemeColors = FeedbackThemeColors()
    ) {
        if (isInitialized) return

        config = FeedbackConfig(
            apiKey = apiKey,
            baseUrl = baseUrl,
            themeColors = themeColors
        )

        repository = FeedbackRepositoryImpl(
            apiKey = apiKey,
            baseUrl = baseUrl
        )

        viewModel = FeedbackViewModel(repository)
        isInitialized = true
    }

    /**
     * عرض نموذج الملاحظات
     */
    fun show() {
        checkInitialization()

        // تحديث البيانات في الـ ViewModel
        viewModel.updateUserData(userName, userEmail)
        viewModel.updateScreenInfo(currentScreen)

        // عرض الـ BottomSheet
        isShowing.value = true
    }

    /**
     * تعيين بيانات المستخدم
     */
    fun setUserData(name: String?, email: String?) {
        this.userName = name
        this.userEmail = email

        if (::viewModel.isInitialized) {
            viewModel.updateUserData(name, email)
        }
    }

    /**
     * تعيين اسم الشاشة الحالية
     */
    fun setScreen(screenName: String) {
        this.currentScreen = screenName

        if (::viewModel.isInitialized) {
            viewModel.updateScreenInfo(screenName)
        }
    }

    /**
     * إخفاء الـ BottomSheet
     */
    internal fun dismiss() {
        isShowing.value = false
        viewModel.resetState()
    }

    private fun checkInitialization() {
        if (!isInitialized) {
            throw IllegalStateException("FeedbackSDK must be initialized before use. Call FeedbackSDK.init() first.")
        }
    }

    /**
     * الحصول على ألوان الثيم
     */
    internal fun getThemeColors(): FeedbackThemeColors = config.themeColors
}

/**
 * إعدادات الـ SDK
 */
data class FeedbackConfig(
    val apiKey: String,
    val baseUrl: String,
    val themeColors: FeedbackThemeColors
)

/**
 * ألوان الثيم المخصصة
 */
data class FeedbackThemeColors(
    val primary: Color = Color(0xFF6200EE),
    val secondary: Color = Color(0xFF03DAC6),
    val background: Color = Color(0xFFFFFFFF),
    val surface: Color = Color(0xFFFFFFFF),
    val error: Color = Color(0xFFB00020),
    val onPrimary: Color = Color.White,
    val onSecondary: Color = Color.Black,
    val onBackground: Color = Color.Black,
    val onSurface: Color = Color.Black,
    val onError: Color = Color.White
)