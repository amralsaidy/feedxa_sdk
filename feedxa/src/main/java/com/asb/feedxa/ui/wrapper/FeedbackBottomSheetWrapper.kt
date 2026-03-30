package com.asb.feedxa.ui.wrapper

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import com.asb.feedxa.ui.composables.FeedbackBottomSheet

/**
 * Wrapper لاستخدام Compose BottomSheet في تطبيقات XML
 */
class FeedbackBottomSheetWrapper : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setContentView(ComposeView(requireContext()).apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    FeedbackBottomSheet(
                        onDismiss = { dismiss() }
                    )
                }
            })
        }
    }

    companion object {
        fun newInstance(): FeedbackBottomSheetWrapper {
            return FeedbackBottomSheetWrapper()
        }

        fun show(context: Context) {
            val fragment = newInstance()
            val fragmentManager = (context as? androidx.appcompat.app.AppCompatActivity)?.supportFragmentManager
            fragmentManager?.let {
                fragment.show(it, "feedback_bottom_sheet")
            }
        }
    }
}