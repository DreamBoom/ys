package card.com.allcard.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import org.xutils.image.ImageOptions
import java.io.*



/**
 * @author Created by Dream
 */
object ReadImgToBinary {

    private var imageOptions: ImageOptions? = null
    fun getImageOptions(): ImageOptions? {
        if (imageOptions == null) {
            imageOptions = ImageOptions.Builder().setFailureDrawableId(card.com.allcard.R.drawable.img_sy_fezn)
                    .setImageScaleType(ImageView.ScaleType.FIT_XY).build()
        }
        return imageOptions
    }
    /**
     *
     * @param imgPath
     * @param bitmap
     * @param imgFormat 图片格式
     * @return
     */
    fun imgToBase64(imgPath: String?, bitmap: Bitmap?, imgFormat: String): String? {
        var bitmap = bitmap
        if (imgPath != null && imgPath.length > 0) {
            bitmap = readBitmap(imgPath)
        }
        if (bitmap == null) {
            //bitmap not found!!
        }
        var out: ByteArrayOutputStream? = null
        try {
            out = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, out)

            out.flush()
            out.close()

            val imgBytes = out.toByteArray()
            return Base64.encodeToString(imgBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            return null
        } finally {
            try {
                out!!.flush()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun readBitmap(imgPath: String): Bitmap? {
        try {
            return BitmapFactory.decodeFile(imgPath)
        } catch (e: Exception) {
            return null
        }

    }

    /**
     *
     * @param base64Data
     * @param imgName
     * @param imgFormat 图片格式
     */
    fun base64ToBitmap(base64Data: String, imgName: String, imgFormat: String) {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        val myCaptureFile = File("/sdcard/", imgName)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myCaptureFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        if (isTu) {
            try {
                assert(fos != null)
                fos!!.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            try {
                assert(fos != null)
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    //    public static String img_android = "/9j/4AAQSkZJRgABAgAAAQABAAD
}
