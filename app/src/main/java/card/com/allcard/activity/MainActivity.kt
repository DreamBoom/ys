package card.com.allcard.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.TabActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.LocalBroadcastManager
import android.view.*
import android.widget.*
import card.com.allcard.R
import card.com.allcard.bean.JpushBean
import card.com.allcard.bean.VersionBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.getActivity.MyApplication.Companion.context
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.receiver.ExampleUtil
import card.com.allcard.tools.DataCleanTools
import card.com.allcard.tools.Tool
import card.com.allcard.utils.ActivityUtils
import card.com.allcard.utils.MyNetUtils
import card.com.allcard.view.CustomHorizontalProgresWithNum
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.maning.updatelibrary.InstallUtils
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.startActivity
import com.pawegio.kandroid.toast
import com.tencent.mmkv.MMKV
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionNo
import com.yanzhenjie.permission.PermissionYes
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_tab_four.*
import kotlinx.android.synthetic.main.buttom_bar.*
import kotlin.system.exitProcess


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
        initCallBack()
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
            upApp()
            tabhost!!.setCurrentTabByTag("one")
        }
        rb_service.setOnClickListener {
            upApp()
            tabhost!!.setCurrentTabByTag("two")
        }
        rb_site.setOnClickListener {
            upApp()
            tabhost!!.setCurrentTabByTag("three")
        }
        rb_user.setOnClickListener {
            upApp()
            tabhost!!.setCurrentTabByTag("four")
        }
        registerMessageReceiver()
    }

    override fun onResume() {
        super.onResume()
        upApp()
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
                exitProcess(0)
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
                if (cn.shortClassName == ".${act.localClassName}" || cn.shortClassName == ".unihome.UniHomeLauncher"
                        ||cn.shortClassName == ".launcher.Launcher") {
                    if (!act.isDestroyed) {
                        when (type) {
                            0 -> alert(act, "您的账号于 $mes 在另一台设备登录。如非本人操作，则密码可能已泄露，建议前往修改密码。")
                            1 -> alertLogin(act, "该账号于 $mes 在另一台设备修改了密码。请重新登录。")
                            2 -> alertLogin(act, "该账号于 $mes 在另一台设备修改了手机号。请重新登录。")
                        }
                    }
                    break
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
            alertDialog.dismiss()
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


    //以下为版本升级
    var s1 = ""
    var show = ""
    var downloadUrl = ""
    private fun upApp() {
        HttpRequestPort.instance.getVersion(object : BaseHttpCallBack(this) {
            override fun onSuccess(s: String) {
                super.onSuccess(s)
                val bean = JSONObject.parseObject(s, object : TypeReference<VersionBean>() {})
                if ("0" == bean.result) {
                    val num = bean.versionNum.replace("v", "")
                    val version = utils.version
                    val bv = version.replace(".", "")
                    s1 = when {
                        num.length > 2 -> num
                        else -> num + "00"
                    }
                    if (bv != s1) {
                        if (bean.remark == "1") {
                            downloadUrl = bean.downloadUrl
                            if (show == "") {
                                popup()
                                if (popupWindow != null) {
                                    popupWindow!!.showAtLocation(bt_login, Gravity.NO_GRAVITY, 0, 0)
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    var popupWindow: PopupWindow? = null
    private fun popup() {
        val v = utils.getView(this, R.layout.pop_upapp2)
        popupWindow = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow!!.contentView = v
        popupWindow!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow!!.isClippingEnabled = false
        popupWindow!!.showAsDropDown(v)
        v.findViewById<TextView>(R.id.tv_4).setOnClickListener {
            BaseActivity.mk.clearAll()
            //为该客户端设置Alias，别名（uuid 即用户名等） 极光
            JPushInterface.clearAllNotifications(this)
            jPush("")
            AndPermission.with(this)
                    .requestCode(300)
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    // .rationale(...)
                    .callback(this)
                    .start()
            popupWindow!!.dismiss()
        }
    }
    private fun jPush(alias: String) {
        JPushInterface.setAlias(applicationContext, 0, alias)
    }
    private fun dowm() {
        pop()
        InstallUtils.with(this)
                //必须-下载地址
                .setApkUrl(downloadUrl)
                //非必须，默认update
                .setApkName("兰考市民卡")
                //非必须-下载保存的路径
                //.setApkPath(Constants.APK_SAVE_PATH)
                //非必须-下载回调
                .setCallBack(downloadCallBack)
                //开始下载
                .startDownload()
    }

    private var downloadCallBack: InstallUtils.DownloadCallBack? = null
    private fun initCallBack() {
        downloadCallBack = object : InstallUtils.DownloadCallBack {
            override fun onStart() {
                show = "1"
            }

            @SuppressLint("SetTextI18n")
            override fun onComplete(path: String) {
                tv!!.text = "正在更新 100%"
                pro!!.progress = 100
                InstallUtils.installAPK(this@MainActivity, path, object : InstallUtils.InstallCallBack {
                    override fun onSuccess() {
                        Toast.makeText(this@MainActivity, "正在安装程序", Toast.LENGTH_SHORT).show()
                        System.exit(0)
                    }

                    override fun onFail(e: Exception) {
                        toast("安装失败...")
                    }
                })
                //  Log.i("===== ====>", "下载成功")
            }

            @SuppressLint("SetTextI18n")
            override fun onLoading(total: Long, current: Long) {
                // Log.i("=========>", (current * 100 / total).toInt().toString() + "%")
                val lo = (current * 100 / total).toInt().toString()
                tv!!.text = "正在更新 $lo %"
                pro!!.progress = (current * 100 / total).toInt()
            }

            override fun onFail(e: Exception) {
                // Log.i("=========>", "下载失败" + e.message)
                InstallUtils.cancleDownload()
                tv!!.text = "网络中断,下载失败"
                cancel!!.visibility = View.VISIBLE
            }

            override fun cancle() {

            }
        }
    }

    //存储权限被拒弹窗
    private fun promess() {
        val v = utils.getView(this, R.layout.pop_prossmess)
        val promess = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        promess.contentView = v
        promess.setBackgroundDrawable(ColorDrawable(0x00000000))
        promess.isClippingEnabled = false
        promess.showAsDropDown(bt_login)
        v.findViewById<TextView>(R.id.tv_3).setOnClickListener {
            promess.dismiss()
            finish()
        }
        v.findViewById<TextView>(R.id.tv_4).setOnClickListener {
            promess.dismiss()
            popupWindow = null
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }


    var cancel: TextView? = null
    var tv: TextView? = null
    var pro: CustomHorizontalProgresWithNum? = null
    private fun pop() {
        val v = utils.getView(this, R.layout.pop_upapp)
        val pop = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        pop.contentView = v
        pop.setBackgroundDrawable(ColorDrawable(0x00000000))
        pop.isClippingEnabled = false
        pop.showAsDropDown(v)
        tv = v.findViewById(R.id.tv_up_app)
        cancel = v.findViewById(R.id.cancel)
        pro = v.findViewById(R.id.progress)
        cancel!!.setOnClickListener {
            if (MyNetUtils.isNetworkConnected(this@MainActivity)) {
                if (pop.isShowing) {
                    pop.dismiss()
                }
                dowm()
            } else {
                utils.showToast("网络异常，检查后点击重试")
            }
        }
    }

    // 成功回调的方法，用注解即可，这里的300就是请求时的requestCode。
    @PermissionYes(300)
    private fun getPermissionYes(grantedPermissions: List<String>) {
        //申请权限成功
        dowm()
    }

    @PermissionNo(300)
    private fun getPermissionNo(deniedPermissions: List<String>) {
        // 申请权限失败。
        // 是否有不再提示并拒绝的权限
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用AndPermission默认的提示语。
            //AndPermission.defaultSettingDialog(this, 400).show()
            promess()
        } else {
            runDelayed(200) {
                MyApplication.instance.removeAllActivity()
                System.exit(0)
            }
        }
    }
}
