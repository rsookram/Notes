package io.github.rsookram.notes.view

import android.view.View
import me.saket.inboxrecyclerview.page.InterceptResult
import me.saket.inboxrecyclerview.page.OnPullToCollapseInterceptor

class CollapseInterceptor(private val scrollableChild: View) : OnPullToCollapseInterceptor {

    override fun invoke(downX: Float, downY: Float, upwardPull: Boolean): InterceptResult {
        // Only allow collapsing by swiping down from the top
        if (upwardPull) {
            return InterceptResult.INTERCEPTED
        }

        val canScrollFurther = scrollableChild.canScrollVertically(-1)
        return if (canScrollFurther) InterceptResult.INTERCEPTED else InterceptResult.IGNORED
    }
}
