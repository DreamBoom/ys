package card.com.allcard.getActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import card.com.allcard.activity.BaseActivity
import card.com.allcard.activity.OpenActivity
import card.com.allcard.activity.OpenSign
import card.com.allcard.activity.SplashActivity
import card.com.allcard.tools.Tool
import card.com.allcard.utils.AppFrontBackHelper
import cn.jpush.android.api.JPushInterface
import com.tencent.mmkv.MMKV
import com.yatoooon.screenadaptation.ScreenAdapterTools
import org.xutils.x
import java.util.*


/**
 * @author Created by Dream
 */
class MyApplication : Application() {
    private var activists: MutableList<Activity>? = null
    var activitys: MutableList<Activity>?
        get() = activists
        set(activitys) {
            this.activists = activitys
        }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        x.Ext.init(this)
        JPushInterface.setDebugMode(true) 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this)
        ScreenAdapterTools.init(this)
        val helper = AppFrontBackHelper()
        MMKV.initialize(applicationContext)
        val mk = BaseActivity.mk
        val mkBD = SplashActivity.mkBD
        helper.register(this@MyApplication, object : AppFrontBackHelper.OnAppStatusListener {
            override fun onFront() {
                val deviceId = getDeviceId(context!!)
                //应用切到前台处理
                val userId = mk.decodeString(Tool.USER_ID, "")
                val login = mk.decodeString(Tool.BY_LOGIN, "0")
                val signLock = mkBD.decodeString(userId + "sign", "")
                val finger = mkBD.decodeString(userId + "finger", "")
                Log.i("====>", "应用切到前台")
                if (login == "0") {
                    when {
                        finger == deviceId -> {
                            val intent = Intent(this@MyApplication,OpenActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
//
                        signLock != "" ->  {
                            val intent = Intent(this@MyApplication,OpenSign::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }
                } else {
                    mk.encode(Tool.BY_LOGIN, "0")
                }
            }
            override fun onBack() {
                Log.i("====>","应用切到后台")
                //应用切到后台处理
            }
        })
    }

    override fun onTerminate() {
        // 程序终止的时候执行
        super.onTerminate()
    }

    override fun onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        // 程序在内存清理的时候执行（回收内存）
        // HOME键退出应用程序、长按MENU键，打开Recent TASK都会执行
        super.onTrimMemory(level)
    }

    init {
        activists = LinkedList()
    }

    /**添加Activity到容器中 */
    fun addActivity(activity: Activity) {
        if (activists != null && activists!!.size > 0) {
            if (!activists!!.contains(activity)) {
                activists!!.add(activity)
            }
        } else {
            activists!!.add(activity)
        }
    }

    /** 遍历所有Activity并finish */
    fun removeAllActivity() {
        if (activists != null && activists!!.size > 0) {
            for (activity in activists!!) {
                activity.finish()
            }
        }
    }


    companion object {
        private var myApplication: MyApplication? = null
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set
        //最近定位时间

        /**
         * 单例模式中获取唯一的MyApplication实例
         *
         * @return
         */
        val instance: MyApplication
            get() {
                if (null == myApplication) {
                    myApplication = MyApplication()
                }

                return myApplication as MyApplication
            }
    }

    fun getDeviceId(context: Context): String {
        return "35" +
                Build.BOARD.length%10+ Build.BRAND.length%10 +

                Build.CPU_ABI.length%10 + Build.DEVICE.length%10 +

                Build.DISPLAY.length%10 + Build.HOST.length%10 +

                Build.ID.length%10 + Build.MANUFACTURER.length%10 +

                Build.MODEL.length%10 + Build.PRODUCT.length%10 +

                Build.TAGS.length%10 + Build.TYPE.length%10 +

                Build.USER.length%10
    }
}
