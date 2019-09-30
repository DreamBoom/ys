package card.com.allcard.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import card.com.allcard.R
import card.com.allcard.bean.PhoneBean
import card.com.allcard.bean.UserDataBean
import card.com.allcard.bean.VersionBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.tools.Tool.RESULTCODE_LOGIN
import card.com.allcard.utils.MyNetUtils
import card.com.allcard.view.CustomHorizontalProgresWithNum
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.just.agentweb.AgentWebConfig
import com.maning.updatelibrary.InstallUtils
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.startActivity
import com.pawegio.kandroid.toast
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionNo
import com.yanzhenjie.permission.PermissionYes
import kotlinx.android.synthetic.main.activity_tab_four.*
import org.xutils.image.ImageOptions
import org.xutils.x

/**
 * @author Created by Dream
 * 个人中心
 */
class TabFourActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_tab_four
    private var userId: String? = null
    private var options: ImageOptions? = null
    private var phone: String? = null
    private var exitPop: PopupWindow? = null
    private var phoneKf = ""
    override fun initView() {
        utils.changeStatusBlack(false, window)
        options = ImageOptions.Builder()
                .setSize(300, 300)
                .setCrop(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.user)
                .setFailureDrawableId(R.drawable.user)
                .setUseMemCache(true)
                .setIgnoreGif(false)
                .setCircular(true).build()
        im_icon.setOnClickListener {
            when {
                !noUserId() -> startActivity<UserInfoActivity>()
            }
        }

        family.setOnClickListener {
            when {
                !noUserId() -> {
                    when (mk.decodeString(Tool.IS_AUTH, "")) {
                        "0" -> startActivity<PayActivity>()
                        else -> utils.showToast("请先进行实名认证")
                    }
                }
            }
        }

        bt_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).putExtra("from", 1))
        }

        pay.setOnClickListener {
            when {
                !noUserId() -> when (mk.decodeString(Tool.IS_AUTH, "")) {
                    "0" -> {
                        val bundle = Bundle()
                        bundle.putString("cardNo","")
                        bundle.putString("nickName", "")
                        bundle.putString("flag","0")
                        utils.startActivityBy(MoneyIn::class.java,bundle)
                    }
                    "1" -> utils.showToast("请先进行实名认证")
                }
            }
        }
        getMoney.setOnClickListener {
            utils.showToast("敬请期待")
        }

        ll_mx.setOnClickListener {
            when {
                !noUserId() -> when (mk.decodeString(Tool.IS_AUTH, "")) {
                    "0" -> {
                        val bundle = Bundle()
                        bundle.putString("cardNo", "")
                        bundle.putString("nickName", "")
                        bundle.putString("is_other", "0")
                        utils.startActivityBy(MoneyInfo::class.java, bundle)
                    }
                    "1" -> utils.showToast("请先进行实名认证")
                }
            }
        }

        ll_ye.setOnClickListener {
            when {
                !noUserId() -> when (mk.decodeString(Tool.IS_AUTH, "")) {
                    "0" -> {
                        val dj = mk.decodeString(Tool.ZHDJ, "")
                        if(dj == "2"){
                            utils.showToast("账户已冻结，无法查询")
                        }else{
                            startActivity<MoneyOfCard>()
                        }
                    }
                    "1" -> utils.showToast("请先进行实名认证")
                }
            }
        }


        ll_bind_pay.setOnClickListener {
            when {
                !noUserId() -> utils.startWeb(HttpRequestPort.H5_BASE_URL
                        + "weixin/kfz.jsp?flag&title=&user_id=" + userId + "&fixparam=android")
            }
        }

        ll_real_name.setOnClickListener {
            val i = mk.decodeString(Tool.IS_AUTH, "1")
            when {
                !noUserId() -> {
                    when (i) {
                        "0" -> startActivity<RealName>()
                        else -> startActivity<RealNameTo>()
                    }
                }
            }
        }

        ll_password.setOnClickListener {
            when {
                !noUserId() -> utils.startActivity(AccountActivity::class.java)
            }
        }

        ll_about_us.setOnClickListener {
            when {
                else -> utils.startWeb(HttpRequestPort.H5_BASE_URL + "weixin/aboutus.jsp?fixparam=android")
            }
        }

        ll_customer.setOnClickListener {
            when {
                else -> popup!!.showAtLocation(bt_login, Gravity.NO_GRAVITY, 0, 0)
            }
        }

        ll_exit.setOnClickListener {
            when {
                else -> exitPop!!.showAtLocation(bt_login, Gravity.NO_GRAVITY, 0, 0)
            }
        }
        showPop()
        exitPop()
        init()
        initCallBack()
    }

    @SuppressLint("SetTextI18n")
    fun init() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        val bandCard = mk.decodeString(Tool.BINDCARD, "1")
        val auth = mk.decodeString(Tool.IS_AUTH, "1")
        val phone = mk.decodeString(Tool.PHONE, "")
        val realName = mk.decodeString(Tool.REAL_NAME, "")
        val dj = mk.decodeString(Tool.ZHDJ, "0")
        if (dj == "2") {
            im_dj.visibility = View.VISIBLE
        } else {
            im_dj.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(userId)) {
            ll_exit.visibility = View.VISIBLE
            when (bandCard) {
                "0" -> {
                    val m = mk.decodeString(Tool.oneMoney, "0")
                    oneMoney.text = utils.save2(m.toDouble())
                }
                "1" -> {
                    oneMoney.text = "——"
                }
            }
            if (auth != Tool.ZERO) {
                bt_login!!.visibility = View.GONE
                ll!!.visibility = View.VISIBLE
                tv2!!.visibility = View.VISIBLE
                tv2!!.text = "请进行实名认证"
                tv3!!.visibility = View.VISIBLE
                val phone1 = phone!!.substring(0, 3) + "****" + phone.substring(7, 11)
                tv3!!.text = phone1
                tv_real_name!!.text = "去认证"
                im1!!.visibility = View.GONE
            } else {
                tv_real_name!!.text = "已认证"
                bt_login!!.visibility = View.GONE
                ll!!.visibility = View.VISIBLE
                tv3!!.visibility = View.VISIBLE
                tv3!!.text = realName
                tv2!!.visibility = View.VISIBLE
                val phone1 = phone!!.substring(0, 3) + "****" + phone.substring(7, 11)
                tv2.text = phone1
                im1!!.visibility = View.VISIBLE
            }
        } else {
            ll_exit.visibility = View.GONE
        }

        //获取客服电话
        HttpRequestPort.instance.baseData("telPhone", object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<PhoneBean>() {})
                if (bean.result == "0") {
                    phoneKf = bean.list[0].para_value
                    phone_num.text = bean.list[0].para_value
                }
            }
        })
    }

    private fun noUserId(): Boolean {
        userId = mk.decodeString(Tool.USER_ID, "")
        phone = mk.decodeString(Tool.PHONE, "")
        if (TextUtils.isEmpty(userId)) {
            startActivity(Intent(this, LoginActivity::class.java).putExtra("from", 1))
            return true
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        login()
        // upApp()
    }

    //退出弹窗
    @SuppressLint("InflateParams")
    private fun exitPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "退出"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "退出当前用户?"
        exitPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        exitPop!!.isTouchable = true
        exitPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        exitPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            exit()

        }
        cancel.setOnClickListener {
            exitPop!!.dismiss()
        }
    }

    private fun exit() {
        mk.clearAll()
        x.image().bind(im_icon, Tool.HEADER_IMG, options)
        im_dj.visibility = View.GONE
        JPushInterface.deleteAlias(this, 0)
        JPushInterface.clearAllNotifications(this@TabFourActivity)
        ll_exit!!.visibility = View.GONE
        if (WebActivity2.mAgentWeb != null) {
            WebActivity2.mAgentWeb!!.urlLoader.loadUrl(HttpRequestPort.H5_BASE_URL + "weixin/wxindexdd.jsp?fixparam=android&user_id=")
        }
        AgentWebConfig.clearDiskCache(this)
        MainActivity.instance!!.tabHost!!.currentTab = 0
    }

    private var popup: PopupWindow? = null
    @SuppressLint("InflateParams")
    private fun showPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "拨打"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "确定拨打客服电话?"
        popup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        popup!!.isTouchable = true
        popup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        popup!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            call(phoneKf)
        }
        cancel.setOnClickListener {
            popup!!.dismiss()
        }
    }

    /**
     * 调用拨号界面
     * @param phone 客服电话号码
     */
    private fun call(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun searchYuE() {
        val isAuth = mk.decodeString(Tool.IS_AUTH)
        val userId = mk.decodeString(Tool.USER_ID)
        if (!TextUtils.isEmpty(userId)) {
            if (isAuth != Tool.ZERO) {
                utils.showToast("请先进行实名认证")
            } else {
                login()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun login() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        if (!TextUtils.isEmpty(userId)) {
            HttpRequestPort.instance.searchData(userId, object : BaseHttpCallBack(this) {
                @SuppressLint("SetTextI18n")
                override fun success(data: String) {
                    super.success(data)
                    val bean = JSONObject.parseObject(data, object : TypeReference<UserDataBean>() {})
                    if (bean.result == "0") {
                        mk.encode(Tool.REAL_NAME, bean.token[0].real_name)
                        mk.encode(Tool.USER_NUM, bean.token[0].idCard)
                        mk.encode(Tool.PHONE, bean.token[0].phone)
                        mk.encode(Tool.IS_AUTH, bean.token[0].is_auth)
                        mk.encode(Tool.BINDCARD, bean.token[0].isbandcard)
                        mk.encode(Tool.oneMoney, bean.token[0].amt)
                        mk.encode(Tool.HEADER, bean.token[0].img)
                        mk.encode(Tool.ZHDJ, bean.token[0].frzFlag)
                        if (bean.token[0].frzFlag == "2") {
                            im_dj.visibility = View.VISIBLE
                        } else {
                            im_dj.visibility = View.GONE
                        }

                        if (bean.token[0].is_auth != Tool.ZERO) {
                            bt_login!!.visibility = View.GONE
                            ll!!.visibility = View.VISIBLE
                            tv2!!.visibility = View.VISIBLE
                            tv2!!.text = "请进行实名认证"
                            tv3!!.visibility = View.VISIBLE
                            val phone = bean.token[0].phone
                            val phone1 = phone!!.substring(0, 3) + "****" + phone.substring(7, 11)
                            tv3!!.text = phone1
                            tv_real_name!!.text = "去认证"
                            tv_real_name!!.text = "去认证"
                            im1!!.visibility = View.GONE
                            oneMoney.text = "——"
                        } else {
                            tv_real_name!!.text = "已认证"
                            bt_login!!.visibility = View.GONE
                            ll!!.visibility = View.VISIBLE
                            tv3!!.visibility = View.VISIBLE
                            tv3!!.text = bean.token[0].real_name
                            tv2!!.visibility = View.VISIBLE
                            val phone = bean.token[0].phone
                            val phone1 = phone!!.substring(0, 3) + "****" + phone.substring(7, 11)
                            tv2.text = phone1
                            im1!!.visibility = View.VISIBLE
                            val m = mk.decodeString(Tool.oneMoney, "0")
                            oneMoney.text = utils.save2(m.toDouble())
                        }
                    } else {
                        utils.showToast(bean.message)
                    }
                }
            })
            ll_exit!!.visibility = View.VISIBLE
        } else {
            tv_real_name!!.text = "去认证"
            oneMoney.text = "——"
            im_dj.visibility = View.GONE
            bt_login!!.visibility = View.VISIBLE
            ll!!.visibility = View.GONE
        }
        val img = mk.decodeString(Tool.HEADER, "")
        if (!TextUtils.isEmpty(img)) {
            x.image().bind(im_icon, img, options)
        } else {
            x.image().bind(im_icon, Tool.HEADER_IMG, options)
        }
    }

    override fun onPause() {
        super.onPause()
        if (exitPop != null) {
            exitPop!!.dismiss()
        }
        if (popup != null) {
            popup!!.dismiss()
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
                if ("0" == bean.status) {
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
                                if (popupWindow == null) {

                                }
                                popup()
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
            mk.clearAll()
            //为该客户端设置Alias，别名（uuid 即用户名等） 极光
            JPushInterface.clearAllNotifications(this)
            JPushInterface.deleteAlias(this, 0)
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

    private fun dowm() {
        pop()
        InstallUtils.with(this)
                //必须-下载地址
                .setApkUrl(HttpRequestPort.BASEURL + downloadUrl + "?fixparam=android")
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
                InstallUtils.installAPK(this@TabFourActivity, path, object : InstallUtils.InstallCallBack {
                    override fun onSuccess() {
                        Toast.makeText(this@TabFourActivity, "正在安装程序", Toast.LENGTH_SHORT).show()
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
            if (MyNetUtils.isNetworkConnected(this@TabFourActivity)) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Tool.RESULTCODE_SUCCESS) {
            return
        }
        when (requestCode) {
            RESULTCODE_LOGIN -> {
                assert(data != null)
                val signOn = data!!.getBooleanExtra("signOff", false)
                when {
                    signOn -> login()
                    else -> finish()
                }
            }
        }
    }
}
