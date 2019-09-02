package io.github.rsookram.notes.view

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.updatePadding
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import io.github.rsookram.notes.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CreateNoteView(context: Context) : AppCompatTextView(context) {

    init {
        height = 56.dp
        gravity = Gravity.CENTER_VERTICAL
        updatePadding(left = 16.dp, right = 16.dp)

        setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0)
        compoundDrawablePadding = 8.dp

        val value = TypedValue().also { v ->
            context.theme.resolveAttribute(R.attr.selectableItemBackground, v, true)
        }
        setBackgroundResource(value.resourceId)

        setText(R.string.new_note)
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
    }

    @CallbackProp
    fun onClick(listener: (() -> Unit)?) {
        if (listener != null) {
            setOnClickListener { listener() }
        } else {
            setOnClickListener(null)
        }
    }
}
