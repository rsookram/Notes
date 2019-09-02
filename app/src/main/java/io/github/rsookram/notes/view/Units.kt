package io.github.rsookram.notes.view

import android.content.res.Resources
import androidx.annotation.Px

/**
 * Treats the receiver as a value in DP, and returns the conversion to pixels
 */
val Int.dp: Int
    @Px get() = (this * Resources.getSystem().displayMetrics.density).toInt()
