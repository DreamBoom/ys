package card.com.allcard.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Xfermode
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView

/**
 * @author Created by Dream
 */
abstract class MaskedImage : ImageView {
    private var mask: Bitmap? = null
    private var paint: Paint? = null

    constructor(paramContext: Context) : super(paramContext) {}

    constructor(paramContext: Context, paramAttributeSet: AttributeSet) : super(paramContext, paramAttributeSet) {}

    constructor(paramContext: Context, paramAttributeSet: AttributeSet, paramInt: Int) : super(paramContext, paramAttributeSet, paramInt) {}

    abstract fun createMask(): Bitmap

    @SuppressLint("WrongConstant")
    override fun onDraw(paramCanvas: Canvas) {
        val localDrawable = drawable ?: return
        try {
            if (this.paint == null) {
                val localPaint1 = Paint()
                this.paint = localPaint1
                this.paint!!.isFilterBitmap = false
                val localPaint2 = this.paint
                val localXfermode1 = MASK_XFERMODE
                val localXfermode2 = localPaint2!!.setXfermode(localXfermode1)
            }
            val f1 = width.toFloat()
            val f2 = height.toFloat()
            val i = paramCanvas.saveLayer(0.0f, 0.0f, f1, f2, null, 31)
            val j = width
            val k = height
            localDrawable.setBounds(0, 0, j, k)
            localDrawable.draw(paramCanvas)
            if (this.mask == null || this.mask!!.isRecycled) {
                val localBitmap1 = createMask()
                this.mask = localBitmap1
            }
            val localBitmap2 = this.mask
            val localPaint3 = this.paint
            paramCanvas.drawBitmap(localBitmap2!!, 0.0f, 0.0f, localPaint3)
            paramCanvas.restoreToCount(i)
            return
        } catch (localException: Exception) {
            val localStringBuilder = StringBuilder()
                    .append("Attempting to draw with recycled bitmap. View ID = ")
            println("localStringBuilder==$localStringBuilder")
        }

    }

    companion object {
        private val MASK_XFERMODE: Xfermode

        init {
            val localMode = PorterDuff.Mode.DST_IN
            MASK_XFERMODE = PorterDuffXfermode(localMode)
        }
    }
}
