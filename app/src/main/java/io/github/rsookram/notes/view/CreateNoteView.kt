package io.github.rsookram.notes.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import io.github.rsookram.notes.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CreateNoteView(context: Context) : FrameLayout(context) {

    init {
        View.inflate(context, R.layout.view_create_note, this)
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
