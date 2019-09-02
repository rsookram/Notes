package io.github.rsookram.notes.view

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.ModelView
import io.github.rsookram.notes.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HeaderView(context: Context) : AppCompatTextView(context) {

    init {
        setPadding(16.dp, 16.dp, 16.dp, 8.dp)
        setText(R.string.app_name)
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline5)
    }
}
