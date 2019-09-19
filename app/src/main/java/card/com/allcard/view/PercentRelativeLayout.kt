package card.com.allcard.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import card.com.allcard.R

/**
 * @author Created by Dream
 * Email：499276615@qq.com
 * On 2017/12/2
 * 百分比布局
 */

class PercentRelativeLayout : RelativeLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return PercentRelativeLayout.LayoutParams(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //获取当前控件宽高
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        //获取子控件数量
        val iChildCount = childCount
        var heightPercent = 0f
        var widthPercent = 0f
        for (i in 0 until iChildCount) {
            val child = getChildAt(i)
            val layoutParams = child.layoutParams
            //判断子控件如果是PercentRelativeLayout的子类，对子控件进行长宽的设定
            if (layoutParams is PercentRelativeLayout.LayoutParams) {
                heightPercent = layoutParams.heightPercent
                widthPercent = layoutParams.widthPercent
            }
            if (heightPercent > 0) {
                layoutParams.height = (height * heightPercent).toInt()
            }
            if (widthPercent > 0) {
                layoutParams.width = (width * widthPercent).toInt()
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    class LayoutParams(c: Context, attrs: AttributeSet) : RelativeLayout.LayoutParams(c, attrs) {

        internal var heightPercent: Float = 0.toFloat()
            set
        internal var widthPercent: Float = 0.toFloat()
            set

        init {
            @SuppressLint("CustomViewStyleable")
            val array = c.obtainStyledAttributes(attrs, R.styleable.Percent)
            heightPercent = array.getFloat(R.styleable.Percent_height_percent, heightPercent)
            widthPercent = array.getFloat(R.styleable.Percent_width_percent, widthPercent)
            array.recycle()
        }
    }
}
