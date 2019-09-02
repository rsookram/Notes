package io.github.rsookram.notes.view.util

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AnyRes
import io.github.rsookram.notes.R

/**
 * Returns the resolved resource for [R.attr.selectableItemBackground] from the
 * receiver Theme
 */
val Resources.Theme.selectableItemBackground: Int
    @AnyRes get() {
        val value = TypedValue().also { v ->
            this.resolveAttribute(R.attr.selectableItemBackground, v, true)
        }
        return value.resourceId
    }
