package io.github.rsookram.notes.view

import android.content.Context
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import io.github.rsookram.notes.R
import io.github.rsookram.notes.view.util.dp
import io.github.rsookram.notes.view.util.selectableItemBackground

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class NoteItemView(context: Context) : AppCompatTextView(context) {

    init {
        minHeight = 56.dp
        gravity = Gravity.CENTER_VERTICAL
        setPadding(16.dp, 8.dp, 16.dp, 8.dp)
        setBackgroundResource(context.theme.selectableItemBackground)
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
    }

    @ModelProp
    fun title(title: String) {
        text = title
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
