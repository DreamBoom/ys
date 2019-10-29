package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.bean.PhoneBean
import card.com.allcard.bean.UserDataBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.tools.Tool.RESULTCODE_LOGIN
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.just.agentweb.AgentWebConfig
import com.pawegio.kandroid.startActivity
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
                        startActivity<MoneyOfCard>()
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
        jPush("")
        JPushInterface.clearAllNotifications(this@TabFourActivity)
        ll_exit!!.visibility = View.GONE
        AgentWebConfig.clearDiskCache(this)
        MainActivity.instance!!.tabHost!!.currentTab = 0
    }

    private fun jPush(alias: String) {
        JPushInterface.setAlias(applicationContext, 0, alias)
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
            ll_exit.visibility = View.GONE
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
