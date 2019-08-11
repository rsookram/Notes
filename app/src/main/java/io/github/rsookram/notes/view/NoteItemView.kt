package io.github.rsookram.notes.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import io.github.rsookram.notes.R
import kotlinx.android.synthetic.main.view_note_item.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class NoteItemView(context: Context) : FrameLayout(context) {

    init {
        View.inflate(context, R.layout.view_note_item, this)
    }

    @ModelProp
    fun title(title: String) {
        title_label.text = title
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
