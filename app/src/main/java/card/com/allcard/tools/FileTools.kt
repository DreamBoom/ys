package card.com.allcard.tools

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log

import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * @author Created by Dream
 */
object FileTools {
    /**
     *
     */
    fun saveMyBitmap(context: Context, bitmap: Bitmap, filename: String): File {
        val f = File(getFileRoot(context) + File.separator + filename + ".png")
        var fOut: FileOutputStream? = null
        try {
            f.createNewFile()
            fOut = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        } catch (e: IOException) {
            Log.i("Error", e.message)
        } finally {
            try {
                fOut!!.flush()
                fOut.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return f
    }


    /**文件存储根目录 */
    private fun getFileRoot(context: Context): String {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val external = context.getExternalFilesDir(null)
            if (external != null) {
                return external.absolutePath
            }
        }

        return context.filesDir.absolutePath

    }
}
