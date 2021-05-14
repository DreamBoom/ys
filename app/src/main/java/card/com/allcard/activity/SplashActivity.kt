package card.com.allcard.activity

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import card.com.allcard.R
import card.com.allcard.bean.VersionBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.LogUtils
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
import kotlinx.android.synthetic.main.activity_splash.*
import kotlin.system.exitProcess

/**
 * @author Created by Dream
 * 欢迎页
 */
class SplashActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_splash
    private val str = "0"
    var url = ""
    var s1 = ""
    //可跳至引导页
    var canJump = 0
    //强制更新显示
    var show1 = ""
    //非强制更新显示
    var show2 = ""

    companion object {
        @SuppressLint("StaticFieldLeak")
        var mkBD = MMKV.mmkvWithID(Tool.MMKV_IN, MMKV.SINGLE_PROCESS_MODE)!!
    }

    override fun initView() {
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        initCallBack()
        AndPermission.with(this@SplashActivity)
                .requestCode(200)
                .permission(
                        CAMERA,
                        RECEIVE_SMS,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_COARSE_LOCATION,
                        ACCESS_FINE_LOCATION,
                        READ_PHONE_STATE)
                // .rationale(...)
                .callback(this)
                .start()
    }

    private fun getVer() {
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
                    when {
                        bv != s1 -> when (bean.remark) {
                            "1" -> {
                                url = bean.downloadUrl
                                if (show1 == "") popup()
                                canJump = 1
                            }
                            else -> {
                                val canShow = mkBD.decodeBool(s1, true)
                                when {
                                    canShow -> {
                                        if (show2 == "") {
                                            url = bean.downloadUrl
                                            showPopup(s1)
                                        }
                                        canJump = 1
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onFinished() {
                super.onFinished()
                if (canJump == 0) {
                      jumpNextPage()
                }
            }
        })
    }

    // 成功回调的方法，用注解即可，这里的300就是请求时的requestCode。
    @PermissionYes(200)
    private fun PermissionYes(grantedPermissions: List<String>) {
        //申请权限成功
        getVer()
    }

    @PermissionNo(200)
    private fun PermissionNo(deniedPermissions: List<String>) {
        // 申请权限失败。
        promess()
    }


    private fun jumpNextPage() {
        // 判断之前有没有显示过新手引导
        val isFirst = mkBD.decodeString(Tool.isFirst, "0")
        if (isFirst == str) {
            // 跳转到新手引导页
            mkBD.encode(Tool.isFirst, "1")
            runDelayed(3000) {
                startActivity<GuideActivity>()
                finish()
            }
        } else {
            val deviceId = utils.getDeviceId(this)
            //指纹手势解锁
            val login = mk.decodeString(Tool.BY_LOGIN, "0")
            val userId = mk.decodeString(Tool.USER_ID, "")
            val signLock = mkBD.decodeString(userId + "sign", "")
            val finger = mkBD.decodeString(userId + "finger", "")
            when {
                finger == deviceId && login == "0" -> {
                    mkBD.encode("open", 1)
                    finish()
                }

                signLock != "" && login == "0" -> {
                    mkBD.encode("open", 1)
                    finish()
                }

                finger != deviceId && signLock == "" -> {
                    startActivity<MainActivity>()
                    finish()
                }
            }
        }
    }

    //以下为版本升级
    var popupWindow: PopupWindow? = null

    private fun showPopup(num: String) {
        val v = utils.getView(this, R.layout.pop_up_app)
        popupWindow = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow!!.contentView = v
        popupWindow!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow!!.isClippingEnabled = false
        popupWindow!!.showAsDropDown(bar)
        v.findViewById<TextView>(R.id.tv_3).setOnClickListener {
            popupWindow!!.dismiss()
            mkBD.encode(num, false)
            show2 = ""
            jumpNextPage()
        }
        v.findViewById<TextView>(R.id.tv_4).setOnClickListener {
            mk.clearAll()
            //为该客户端设置Alias，别名（uuid 即用户名等） 极光
            JPushInterface.clearAllNotifications(this)
            JPushInterface.deleteAlias(this@SplashActivity,0)
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

    //以下为版本升级
    private fun popup() {
        val v = utils.getView(this, R.layout.pop_upapp2)
        val popup = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        popup.contentView = v
        popup.setBackgroundDrawable(ColorDrawable(0x00000000))
        popup.isClippingEnabled = false
        popup.showAsDropDown(bar)
        v.findViewById<TextView>(R.id.tv_4).setOnClickListener {
            mk.clearAll()
            //为该客户端设置Alias，别名（uuid 即用户名等） 极光
            JPushInterface.clearAllNotifications(this)
            JPushInterface.deleteAlias(this@SplashActivity,0)
            AndPermission.with(this)
                    .requestCode(300)
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    // .rationale(...)
                    .callback(this)
                    .start()
            popup.dismiss()
        }
    }
    private fun jPush(alias: String) {
        JPushInterface.setAlias(applicationContext, 0, alias)
    }
    //存储权限被拒弹窗
    private fun promess() {
        val v = utils.getView(this, R.layout.pop_prossmess)
        val promess = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        promess.contentView = v
        promess.setBackgroundDrawable(ColorDrawable(0x00000000))
        promess.isClippingEnabled = false
        promess.showAsDropDown(bar)
        v.findViewById<TextView>(R.id.tv_3).setOnClickListener {
            promess.dismiss()
            finish()
        }
        v.findViewById<TextView>(R.id.tv_4).setOnClickListener {
            promess.dismiss()
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    private fun dowm() {
        pop()
        InstallUtils.with(this)
                //必须-下载地址
                .setApkUrl(url)
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
                show1 = "1"
                show2 = "1"
            }

            override fun onComplete(path: String) {
                tv!!.text = "正在更新 100%"
                pro!!.progress = 100
                InstallUtils.installAPK(this@SplashActivity, path, object : InstallUtils.InstallCallBack {
                    override fun onSuccess() {
                        exitProcess(0)
                        Toast.makeText(this@SplashActivity, "正在安装程序", Toast.LENGTH_SHORT).show()
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
                InstallUtils.cancleDownload()
                tv!!.text = "网络中断，下载失败"
                cancel!!.visibility = View.VISIBLE
            }

            override fun cancle() {

            }
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
        pop.showAsDropDown(bar)
        tv = v.findViewById(R.id.tv_up_app)
        cancel = v.findViewById(R.id.cancel)
        pro = v.findViewById(R.id.progress)
        cancel!!.setOnClickListener {
            if (MyNetUtils.isNetworkConnected(this@SplashActivity)) {
                if (pop.isShowing) {
                    pop.dismiss()
                }
                dowm()
            } else {
                utils.showToast("网络异常，检查后点击重试")
            }
        }
    }

    //    // 成功回调的方法，用注解即可，这里的300就是请求时的requestCode。
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
            //第一种:用AndPermission默认的提示语。
            // AndPermission.defaultSettingDialog(this, 400).show()
            promess()
        } else {
            runDelayed(200) {
                MyApplication.instance.removeAllActivity()
                exitProcess(0)
            }
        }
    }
}
