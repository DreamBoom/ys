package card.com.allcard.utils


import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import card.com.allcard.R
import card.com.allcard.activity.WebOther
import card.com.allcard.activity.WebPost
import card.com.allcard.activity.WebPostOther
import card.com.allcard.activity.WebViewActivity
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.net.URLEncoder
import java.text.DecimalFormat
import java.util.regex.Pattern

/**
 * @author Created by Dream
 */
class ActivityUtils {
    private var activityWeakReference: WeakReference<Activity>? = null
    private var fragmentWeakReference: WeakReference<Fragment>? = null

    private val activity: Activity?
        get() {
            if (activityWeakReference != null) {
                return activityWeakReference!!.get()
            }
            if (fragmentWeakReference != null) {
                val fragment = fragmentWeakReference!!.get()
                return fragment?.activity
            }
            return null
        }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    val version: String
        get() {
            val activity = activity
            return try {
                assert(activity != null)
                val manager = activity!!.packageManager
                val info = manager.getPackageInfo(activity.packageName, 0)
                info.versionName
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }

        }

    //获取显示进度条
    internal var dialog: Dialog? = null

    constructor(activity: Activity) {
        activityWeakReference = WeakReference(activity)
    }

    constructor(fragment: Fragment) {
        fragmentWeakReference = WeakReference(fragment)
    }

    var toast: Toast? = null
    @SuppressLint("ShowToast")
    fun showToast(msg: CharSequence) {
        val activity = activity ?: return
        if(toast == null){
            toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.show()
    }

    fun showToast(resId: Int) {
        val activity = activity
        if (activity != null) {
            val msg = activity.getString(resId)
            showToast(msg)
        }
    }

    fun startActivity(clazz: Class<out Activity>) {
        val activity = activity ?: return
        val intent = Intent(activity, clazz)
        activity.startActivity(intent)
    }

    fun startActivityBy(clazz: Class<out Activity>, bundle: Bundle) {
        val activity = activity ?: return
        val intent = Intent(activity, clazz)
        intent.putExtras(bundle)
        activity.startActivity(intent)
    }

    fun startOtherWeb(url: String) {
        val activity = activity ?: return
        val intent = Intent(activity, WebOther::class.java)
        intent.putExtra("url", url)
        activity.startActivity(intent)
    }

    fun startPostOther(url: String) {
        val activity = activity ?: return
        val intent = Intent(activity, WebPostOther::class.java)
        intent.putExtra("url", url)
        activity.startActivity(intent)
    }


    fun startWeb(url: String) {
        val activity = activity ?: return
        val intent = Intent(activity, WebViewActivity::class.java)
        intent.putExtra("url", url)
        activity.startActivity(intent)
    }

    fun startPostWeb(url: String) {
        val activity = activity ?: return
        val intent = Intent(activity, WebPost::class.java)
        intent.putExtra("url", url)
        activity.startActivity(intent)
    }

    fun startActivityForResult(clazz: Class<out Activity>, requestcode: Int) {
        val activity = activity ?: return
        val intent = Intent(activity, clazz)
        activity.startActivityForResult(intent, requestcode)
    }

    fun checkIdCard(idCard: String): Boolean {
        val regex = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)"
        return Pattern.matches(regex, idCard)
    }


    fun toStartActivityForResult(clazz: Class<out Activity>, requestcode: Int, bundle: Bundle) {
        val activity = activity ?: return
        val intent = Intent(activity, clazz)
        intent.putExtras(bundle)
        activity.startActivityForResult(intent, requestcode)
    }

    fun hideSoftKeyboard() {
        val activity = activity ?: return
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun getView(context: Context, viewId: Int): View {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(viewId, null)
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    fun getDeviceId(context: Context): String {
        return "35" +
                Build.BOARD.length % 10 + Build.BRAND.length % 10 +

                Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +

                Build.DISPLAY.length % 10 + Build.HOST.length % 10 +

                Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +

                Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +

                Build.TAGS.length % 10 + Build.TYPE.length % 10 +

                Build.USER.length % 10
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    fun PhoneName(context: Context): String {
        return Build.MODEL
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    fun BuildVersion(context: Context): String {
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * 判断是否有sd卡
     *
     * @return
     */
    fun sdCardexit(): Boolean {
        return android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
    }

    @SuppressLint("InflateParams")
    fun getProgress(context: Context) {
        dialog = Dialog(context, R.style.MyDialog)
        dialog!!.show()
        dialog!!.window!!.setContentView(LayoutInflater.from(context).inflate(R.layout.progress, null))
        dialog!!.setCancelable(true)
    }

    //隐藏进度条
    fun hindProgress() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }


    //保留两位小数
    fun save2(d: Double): String {
        return DecimalFormat("0.00").format(d)
    }

    fun getUTF8XMLString(xml: String): String {
        // A StringBuffer Object
        val sb = StringBuffer()
        sb.append(xml)
        var xmString = ""
        var xmlUTF8 = ""
        try {
            xmString = String(sb.toString().toByteArray(charset("UTF-8")))
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            //  Auto-generated catch block
            e.printStackTrace()
        }
        // return to String Formed
        return xmlUTF8
    }

    fun getView(layoutId: Int): View {
        val activity = activity
        return LayoutInflater.from(activity).inflate(layoutId,
                RelativeLayout(activity))
    }

    //获取状态栏高度
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    fun getDaoHangHeight(context: Context): Int {
        val result = 0
        var resourceId = 0
        val rid = context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
        if (rid != 0) {
            resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return context.resources.getDimensionPixelSize(resourceId)
        } else
            return 0
    }

    /**
     * 获取手机屏幕高度
     */
    fun getHeight(window: Window): Int {
        val windowManager = window.windowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    /**
     * 获取屏幕真实高度（包括虚拟键盘）
     *
     */
    fun getRealHeight(window: Window): Int {
        val display = window.windowManager.defaultDisplay
        val dm = DisplayMetrics()
        display.getRealMetrics(dm)
        return dm.heightPixels
    }

    /**
     * 判断虚拟导航栏是否显示
     */
    fun checkBarShow(activity: Activity): Boolean {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        val realSize = Point()
        display.getSize(size)
        display.getRealSize(realSize)
        val result = realSize.y != size.y
        return realSize.y != size.y
    }

    //改变状态栏字体颜色
    fun changeStatusBlack(isBlack: Boolean, window: Window) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//设置状态栏黑色字体
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE//恢复状态栏白色字体
            }
        }
    }

    fun shake(view: View): ObjectAnimator {
        // 8dp 左右抖动的幅度
        val delta = view.resources.getDimensionPixelOffset(R.dimen.size11)
        val pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.10f, (-delta).toFloat()),
                Keyframe.ofFloat(.26f, delta.toFloat()),
                Keyframe.ofFloat(.42f, (-delta).toFloat()),
                Keyframe.ofFloat(.58f, delta.toFloat()),
                Keyframe.ofFloat(.74f, (-delta).toFloat()),
                Keyframe.ofFloat(.90f, delta.toFloat()),
                Keyframe.ofFloat(1f, 0f)
        )
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).setDuration(500)
    }

    companion object {

        /**
         * 安装apk
         *
         * @param path
         */
        fun installAPK(aty: Activity, path: String) {
            val intent = Intent("android.intent.action.VIEW")
            intent.setDataAndType(Uri.parse("file://$path"), "application/vnd.android.package-archive")
            aty.startActivity(intent)
        }

        /**
         * 获取旋转角度
         * @param path
         * @return
         */
        fun degree(bitmap: Bitmap): Bitmap {
            var matrix = Matrix() //旋转图片 动作
            matrix.setRotate(90f)//旋转角度
            var width = bitmap.width
            var height = bitmap.height// 创建新的图片
            var resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
            return resizedBitmap
        }
    }

    private val MIN_DELAY_TIME = 200  // 两次点击间隔不能少于1000ms
    private var lastClickTime = 0L
    fun isFastClick(): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false
        }
        lastClickTime = currentClickTime
        return flag
    }

    /**
     * 获取旋转角度
     * @param path
     * @return
     */
    fun degree(bitmap: Bitmap): Bitmap {
        val matrix = Matrix() //旋转图片 动作
        matrix.setRotate(90f)//旋转角度
        val width = bitmap.width
        val height = bitmap.height// 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }
}
