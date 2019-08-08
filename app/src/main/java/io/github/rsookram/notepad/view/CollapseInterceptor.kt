package io.github.rsookram.notepad.view

import android.view.View
import me.saket.inboxrecyclerview.page.InterceptResult
import me.saket.inboxrecyclerview.page.OnPullToCollapseInterceptor

class CollapseInterceptor(private val scrollableChild: View) : OnPullToCollapseInterceptor {

    override fun invoke(downX: Float, downY: Float, upwardPull: Boolean): InterceptResult {
        val directionInt = if (upwardPull) 1 else -1
        val canScrollFurther = scrollableChild.canScrollVertically(directionInt)
        return if (canScrollFurther) InterceptResult.INTERCEPTED else InterceptResult.IGNORED
    }
}
