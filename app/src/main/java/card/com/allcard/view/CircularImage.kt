package card.com.allcard.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet

/**
 * @author Created by Dream
 */
class CircularImage : MaskedImage {
    constructor(paramContext: Context) : super(paramContext) {}

    constructor(paramContext: Context, paramAttributeSet: AttributeSet) : super(paramContext, paramAttributeSet) {}

    constructor(paramContext: Context, paramAttributeSet: AttributeSet, paramInt: Int) : super(paramContext, paramAttributeSet, paramInt) {}

    override fun createMask(): Bitmap {
        val i = width
        val j = height
        val localConfig = Bitmap.Config.ARGB_8888
        val localBitmap = Bitmap.createBitmap(i, j, localConfig)
        val localCanvas = Canvas(localBitmap)
        val localPaint = Paint(1)
        localPaint.color = -16777216
        val f1 = width.toFloat()
        val f2 = height.toFloat()
        val localRectF = RectF(0.0f, 0.0f, f1, f2)
        localCanvas.drawOval(localRectF, localPaint)
        return localBitmap
    }
}
