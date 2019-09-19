package card.com.allcard.view

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * 功能描述: 刘海屏控制
 */
class DisplayCutout(private val mAc: Activity) {

    //在使用LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES的时候，状态栏会显示为白色，这和主内容区域颜色冲突,
    //所以我们要开启沉浸式布局模式，即真正的全屏模式,以实现状态和主体内容背景一致
    fun openFullScreenModel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mAc.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val lp = mAc.window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            mAc.window.attributes = lp
            val decorView = mAc.window.decorView
            var systemUiVisibility = decorView.systemUiVisibility
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            systemUiVisibility = systemUiVisibility or flags
            mAc.window.decorView.systemUiVisibility = systemUiVisibility
        }
    }
}
