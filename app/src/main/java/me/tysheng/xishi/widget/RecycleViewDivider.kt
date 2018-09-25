package me.tysheng.xishi.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import me.tysheng.xishi.R
import me.tysheng.xishi.ext.drawable

/**
 * Created by Sty
 * Date: 16/9/10 16:04.
 */
class RecycleViewDivider(context: Context) : RecyclerView.ItemDecoration() {

    private val divider: Drawable = context.drawable(R.drawable.recyclerview_divider)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}