package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import card.com.allcard.tools.Tool
import card.com.allcard.utils.ActivityUtils
import com.tencent.mmkv.MMKV
import com.yatoooon.screenadaptation.ScreenAdapterTools




abstract class BaseActivity : AppCompatActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        val mk = MMKV.mmkvWithID(Tool.MMKV_OUT, MMKV.SINGLE_PROCESS_MODE)
        @SuppressLint("StaticFieldLeak")
        var isForeground = false

    }
    val utils = ActivityUtils(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        //隐藏状态栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initView()
        //屏幕适配
        ScreenAdapterTools.getInstance().loadView(window.decorView)
    }

    abstract fun layoutId(): Int
    abstract fun initView()

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 判断连续点击事件时间差
            if (utils.isFastClick()) {
                return true
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        isForeground = true
        super.onResume()
    }

    override fun onPause() {
        isForeground = false
        super.onPause()
    }

}
