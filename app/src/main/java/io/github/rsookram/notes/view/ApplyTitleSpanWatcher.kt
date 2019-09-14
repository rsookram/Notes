package io.github.rsookram.notes.view

import android.text.Editable
import android.text.NoCopySpan
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan

class ApplyTitleSpanTextWatcher : TextWatcher {

    // Use NoCopySpan to prevent copying to the clipboard
    private object Span : RelativeSizeSpan(1.5F), NoCopySpan

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

        if (s.getSpanStart(Span) != start || s.getSpanEnd(Span) != end) {
            s.removeSpan(Span)
            s.setSpan(Span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}
