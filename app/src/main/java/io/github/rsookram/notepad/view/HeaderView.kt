package io.github.rsookram.notepad.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import io.github.rsookram.notepad.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HeaderView(context: Context) : FrameLayout(context) {

    init {
        View.inflate(context, R.layout.view_header, this)
    }
}
