package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.bean.DeviceListBean
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.title.*
import java.io.Serializable
import java.text.SimpleDateFormat

/**
 * @author Created by Dream
 * 账户与安全
 */
class AccountActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_account
    private var mHandler: Handler? = null
    private var list: MutableList<DeviceListBean.SafetyList> = mutableListOf()
    private var listAll: MutableList<DeviceListBean.DeviceList> = mutableListOf()
    var isAuth = ""
    @SuppressLint("SimpleDateFormat")
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var click = 0
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "账户与安全"
        bindPopup()
        close.setOnClickListener { finish() }

        ll_login_word.setOnClickListener { utils.startActivity(ChangePasswordActivity::class.java) }

        ll_forget_pay.setOnClickListener {
            when (isAuth) {
                "0" -> utils.startActivity(ForgetPayPass::class.java)
                else -> utils.showToast("请在个人中心先进行实名认证")
            }
        }

        sign.setOnClickListener { startActivity<SignSetting>() }

        changePhone.setOnClickListener { startActivity<ChangePhone>() }

        mHandler = Handler()
        setSms.setOnClickListener {
            when (isAuth) {
                "0" -> {
                    when (mk.decodeString(Tool.EMAIL, "")) {
                        "" -> startActivity(Intent(this, SetSms::class.java).putExtra("type", "0"))
                        else ->   startActivity(Intent(this, BySms::class.java).putExtra("type", "1"))
                    }
                }
                else -> utils.showToast("请在个人中心先进行实名认证")
            }
        }

        setQuestion.setOnClickListener {
            when (isAuth) {
                "0" -> {
                    when (qState.text) {
                        "重置" -> {
                            val bundle = Bundle()
                            bundle.putString("type","0")
                            utils.startActivityBy(ChangeQuestion::class.java,bundle)
                        }
                        else ->   startActivity(Intent(this, SetQuestion::class.java).putExtra("type", "0"))
                    }
                }
                else -> utils.showToast("请在个人中心先进行实名认证")
            }
        }

        device.setOnClickListener {
            if(click == 0){
                deviceSecurity(0)
            }
        }

        device_log.setOnClickListener {
            if(click == 0) {
                deviceSecurity(1)
            }
        }
    }

    private fun deviceSecurity(i:Int) {
        click = 1
        val phone = mk.decodeString(Tool.PHONE, "")
        HttpRequestPort.instance.deviceList(phone, "1", "10",
                object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<DeviceListBean>() {})
                        if (bean.status == "0") {
                            if (bean.deviceList.size > 0) {
                                listAll.clear()
                                listAll.addAll(bean.deviceList)
                            }
                            if (bean.safetyList.size > 0) {
                                list.clear()
                                list.addAll(bean.safetyList)
                            }
                            if(i==0){
                                val device = SplashActivity.mkBD.decodeString(Tool.isExitDefaultDevice, "1")
                                if (device == "0") {
                                    if(list.size>0){
                                        if (list[0].deviceType == "0") {
                                            val intent = Intent(this@AccountActivity, DeviceInfo::class.java)
                                            intent.putExtra( "name" , list[0].deviceName)
                                            intent.putExtra( "num" ,list[0].deviceNum)
                                            intent.putExtra( "API" , list[0].deviceApi)
                                            intent.putExtra( "time" , simpleDateFormat.format(list[0].deviceTime.time))
                                            intent.putExtra( "type", list[0].deviceType)
                                            startActivity(intent)
                                        }
                                    }else{
                                        utils.showToast("查询失败")
                                    }
                                } else {
                                    bindPopup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                                }
                            }else{
                                if(listAll.size>0){
                                    val intent = Intent()
                                    intent.setClass(this@AccountActivity,Device::class.java)
                                    intent.putExtra("list", listAll as Serializable)
                                    startActivity(intent)
                                }else{
                                    utils.showToast("查询失败")
                                }
                            }
                        }
                    }

                    override fun onError(throwable: Throwable, b: Boolean) {
                        super.onError(throwable, b)
                        utils.showToast("查询失败")
                    }

                    override fun onFinished() {
                        super.onFinished()
                        click = 0
                    }
                })
    }


    private fun login() {
        isAuth = mk.decodeString(Tool.IS_AUTH, "")
        val bindQ = mk.decodeString(Tool.BINDQ, "")
        val email = mk.decodeString(Tool.EMAIL, "")
        when {
            TextUtils.isEmpty(email) -> smsState.text = "去绑定"
            else -> smsState.text = "重置"
        }
        when {
            TextUtils.isEmpty(bindQ) -> qState.text = "去设置"
            else -> qState.text = "重置"
        }
    }

    override fun onResume() {
        super.onResume()
        login()
        val ph = mk.decodeString(Tool.PHONE)
        if(!TextUtils.isEmpty(ph)){
            val phone1 = ph!!.substring(0, 3) + "****" + ph.substring(7, 11)
            phone.text = phone1
        }
    }

    private var bindPopup: PopupWindow? = null

    private fun bindPopup() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "绑定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "是否绑定当前设备为安全设备"
        bindPopup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        bindPopup!!.isTouchable = true
        bindPopup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        bindPopup!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            bindDevice()
            bindPopup!!.dismiss()
        }
        cancel.setOnClickListener {
            bindPopup!!.dismiss()
        }
    }

    val deviceId = utils.getDeviceId(this)
    val phoneName = utils.PhoneName(this)
    val buildVersion = utils.BuildVersion(this)
    private fun bindDevice() {
        val phone = mk.decodeString(Tool.PHONE, "")
        HttpRequestPort.instance.deviceBind(phone, deviceId, phoneName, "0",
                "Android $buildVersion", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    utils.showToast("绑定成功")
                    SplashActivity.mkBD.encode(Tool.isExitDefaultDevice, "0")
                } else {
                    utils.showToast("绑定失败，请重新绑定")
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("绑定失败，请重新绑定")
            }
        })
    }
}
