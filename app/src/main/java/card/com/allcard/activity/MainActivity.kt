package card.com.allcard.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.TabActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TabHost
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.bean.JpushBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.getActivity.MyApplication.Companion.context
import card.com.allcard.receiver.ExampleUtil
import card.com.allcard.tools.DataCleanTools
import card.com.allcard.tools.Tool
import card.com.allcard.utils.ActivityUtils
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.buttom_bar.*


/**
 * @author Created by Dream
 * Email：499276615@qq.com
 * Tab页
 */

class MainActivity : TabActivity() {
    var mk: MMKV? = null
    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: MainActivity? = null
        @SuppressLint("StaticFieldLeak")
        var tab: TabHost? = null

        @SuppressLint("StaticFieldLeak")
        val MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION"
        @SuppressLint("StaticFieldLeak")
        val KEY_TITLE = "title"
        @SuppressLint("StaticFieldLeak")
        val KEY_MESSAGE = "message"
        @SuppressLint("StaticFieldLeak")
        val KEY_EXTRAS = "extras"
    }

    private var firstTime: Long = 0
    private var utils = ActivityUtils(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyApplication.instance.addActivity(this)
        mk = BaseActivity.mk
        instance = this
        tab = tabhost
        initView()
        registerMessageReceiver()
    }

    private fun initView() {
        //  BadgeUtil.setBadgeCount(this,1,R.drawable.icon_back)
        tab!!.addTab(tab!!.newTabSpec("one").setIndicator("首页").setContent(Intent(this, TabOne::class.java)))
        tab!!.addTab(tab!!.newTabSpec("two").setIndicator("服务").setContent(Intent(this, TabTwo::class.java)))
        tab!!.addTab(tab!!.newTabSpec("three").setIndicator("网点").setContent(Intent(this, TabThree::class.java)))
        tab!!.addTab(tab!!.newTabSpec("four").setIndicator("我的").setContent(Intent(this, TabFourActivity::class.java)))
        tab!!.setOnTabChangedListener { s ->
            mk!!.encode(Tool.BY_LOGIN, "0")
            setIconBackGround()
            when (s) {
                Tool.one -> {
                    im_one!!.setBackgroundResource(R.drawable.icon_db_01_s)
                    tv_one!!.setTextColor(resources.getColor(R.color.blueTop))
                }
                Tool.two -> {
                    im_two!!.setBackgroundResource(R.drawable.icon_db_02_s)
                    tv_two!!.setTextColor(resources.getColor(R.color.blueTop))
                }
                Tool.three -> {
                    im_three!!.setBackgroundResource(R.drawable.icon_db_03_s)
                    tv_three!!.setTextColor(resources.getColor(R.color.blueTop))
                }
                Tool.four -> {
                    im_four!!.setBackgroundResource(R.drawable.icon_db_04_s)
                    tv_four!!.setTextColor(resources.getColor(R.color.blueTop))
                }
            }
        }

        rb_home.setOnClickListener {
            tabhost!!.setCurrentTabByTag("one")
        }
        rb_service.setOnClickListener {
            tabhost!!.setCurrentTabByTag("two")
        }
        rb_site.setOnClickListener {
            tabhost!!.setCurrentTabByTag("three")
        }
        rb_user.setOnClickListener {
            tabhost!!.setCurrentTabByTag("four")
        }
    }

    private fun setIconBackGround() {
        im_one!!.setBackgroundResource(R.drawable.icon_db_01)
        tv_one!!.setTextColor(resources.getColor(R.color.text_gray))
        im_two!!.setBackgroundResource(R.drawable.icon_db_02)
        tv_two!!.setTextColor(resources.getColor(R.color.text_gray))
        im_three!!.setBackgroundResource(R.drawable.icon_db_03)
        tv_three!!.setTextColor(resources.getColor(R.color.text_gray))
        im_four!!.setBackgroundResource(R.drawable.icon_db_04)
        tv_four!!.setTextColor(resources.getColor(R.color.text_gray))
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_BACK) {
            val secondTime = System.currentTimeMillis()
            if (secondTime - firstTime > 2000) {
                utils.showToast("再按一次退出程序")
                firstTime = secondTime
                return false
            } else {
                MyApplication.instance.removeAllActivity()
                DataCleanTools.cleanApplicationData(this)
                System.exit(0)
            }
        }
        return super.dispatchKeyEvent(event)
    }


    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver!!)
        super.onDestroy()
    }

    //for receive customer msg from jpush server
    private var mMessageReceiver: MessageReceiver? = null

    fun registerMessageReceiver() {
        mMessageReceiver = MessageReceiver()
        val filter = IntentFilter()
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        filter.addAction(MESSAGE_RECEIVED_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver!!, filter)
    }

    inner class MessageReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION == intent.action) {
                    val messge = intent.getStringExtra(KEY_MESSAGE)
                    val extras = intent.getStringExtra(KEY_EXTRAS)
                    val showMsg = StringBuilder()
                    showMsg.append("$KEY_MESSAGE : $messge\n")
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append("$KEY_EXTRAS : $extras\n")
                    }
                    setCostomMsg(showMsg.toString())
                }
            } catch (e: Exception) {
            }

        }
    }

    private fun setCostomMsg(msg: String) {
        val split = msg.split("{")
        val bean = JSONObject.parseObject("{${split[1]}", object : TypeReference<JpushBean>() {})
        when (bean.msgType) {
            "login" -> {
                pop(this, bean.datetime, 0)
            }
            "updatePwd" -> {
                val mk = BaseActivity.mk
                val userId = mk.decodeString(Tool.USER_ID, "")
                mk.clearAll()
                SplashActivity.mkBD.encode(userId + "finger", "")
                SplashActivity.mkBD.encode(userId + "sign", "")
                jPush(this, "")
                JPushInterface.clearAllNotifications(context)
                pop(this, bean.datetime, 1)
            }
            "modPhone" -> {
                val mk = BaseActivity.mk
                val userId = mk.decodeString(Tool.USER_ID, "")
                mk.clearAll()
                SplashActivity.mkBD.encode(userId + "finger", "")
                SplashActivity.mkBD.encode(userId + "sign", "")
                jPush(this, "")
                JPushInterface.clearAllNotifications(context)
                pop(this, bean.datetime, 2)
            }
        }
    }

    private fun jPush(context: Context, alias: String) {
        JPushInterface.setAliasAndTags(context, alias, null) { i, s, set -> }
    }

    private fun pop(context: Context, mes: String, type: Int) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            for (act in MyApplication.instance.activitys!!) {
                if (".${act.localClassName}" == cn.shortClassName
                        || cn.shortClassName == ".unihome.UniHomeLauncher"
                        || cn.shortClassName == ".launcher.Launcher") {
                    if (!act.isDestroyed) {
                        when (type) {
                            0 -> alert(act, "您的账号于 $mes 在另一台设备登录。如非本人操作，则密码可能已泄露，建议前往修改密码。")
                            1 -> alertLogin(act, "该账号于 $mes 在另一台设备修改了密码。请重新登录。")
                            2 -> alertLogin(act, "该账号于 $mes 在另一台设备修改了手机号。请重新登录。")
                        }
                    }
                }
            }
        }
    }

    private fun alertLogin(context: Activity, str: String) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.jpush_dialog, null)
        builder.setView(view)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val title = view.findViewById<TextView>(R.id.title)
        title.text = str
        val sure = view.findViewById<TextView>(R.id.sure)
        sure.setOnClickListener {
            context.startActivity<LoginActivity>()
        }
    }

    private fun alert(context: Activity, str: String) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.jpush_login_dialog, null)
        builder.setView(view)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val title = view.findViewById<TextView>(R.id.title)
        title.text = str
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "修改密码"
        sure.setOnClickListener {
            alertDialog.dismiss()
            context.startActivity<ChangePasswordActivity>()
        }
        cancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}
