package io.github.rsookram.notes.view

import android.text.Editable
import android.text.NoCopySpan
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan

// Use NoCopySpan to prevent copying to the clipboard
class TitleSpan : RelativeSizeSpan(1.5F), NoCopySpan

class ApplyTitleSpanTextWatcher : TextWatcher {

    private val span = TitleSpan()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
        Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
        Unit

    override fun afterTextChanged(s: Editable) {
        val start = 0
        var end = s.asSequence().indexOfFirst { it == '\n' }
        if (end < 1) {
            end = s.length
        }

        if (s.getSpanStart(span) != start || s.getSpanEnd(span) != end) {
            s.removeSpan(span)
            s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}
