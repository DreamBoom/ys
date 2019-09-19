package card.com.allcard.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView


/**
 * @author Created by Dream
 */
class MyGridView : GridView {

    var hasScrollBar = true

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var expandSpec = heightMeasureSpec
        if (hasScrollBar) {
            expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2,
                    View.MeasureSpec.AT_MOST)
            super.onMeasure(widthMeasureSpec, expandSpec)// 注意这里,这里的意思是直接测量出GridView的高度
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
