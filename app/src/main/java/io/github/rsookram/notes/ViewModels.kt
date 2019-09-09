package io.github.rsookram.notes

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Returns a [Lazy] delegate to access the [ViewModel] returned by
 * [createViewModel].
 *
 * This simplifies the common case of an Activity requiring only one
 * [ViewModel].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> ComponentActivity.viewModel(
    noinline createViewModel: () -> VM
): Lazy<VM> = viewModels {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return createViewModel() as T
        }
    }
}
