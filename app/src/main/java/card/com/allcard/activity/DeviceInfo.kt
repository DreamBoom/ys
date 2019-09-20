package card.com.allcard.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_device_info.*
import kotlinx.android.synthetic.main.title.*

class DeviceInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_device_info
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    var num = ""
    val deviceId = utils.getDeviceId(this)
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        MyApplication.instance.addActivity(this)
        address.text = "安全终端管理"
        mHandler = Handler()
        close.setOnClickListener { finish() }
        changePhone.setOnClickListener { popup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0) }
        val name = intent.getStringExtra("name")
        val time1 = intent.getStringExtra("time")
        val api = intent.getStringExtra("API")
        val type = intent.getStringExtra("type")
        num = intent.getStringExtra("num").trim()
        API.text = api
        deviceName.text = name
        val ph = mk.decodeString(Tool.PHONE, "")
        val phone1 = ph!!.substring(0, 3) + "****" + ph.substring(7, 11)
        phone.text = phone1
        when (type) {
            "0" -> level.text = "安全认证设备"
            "1" -> level.text = "普通设备"
        }
        when (num) {
            deviceId -> changePhone.visibility = View.GONE
            else -> changePhone.visibility = View.VISIBLE
        }
        time.text = time1
        deletePop()
    }

    var sendCode: TextView?=null
    var et_code: EditText?=null
    var tv_djs: TextView?=null
    var popup: PopupWindow?=null
    private fun deletePop() {
        val ph = mk.decodeString(Tool.PHONE, "")
        val phone = ph!!.substring(0, 3) + "****" + ph.substring(7, 11)
        val view = LayoutInflater.from(this).inflate(R.layout.msg_dialog, null)
        popup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        popup!!.isTouchable = true
        popup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        popup!!.setBackgroundDrawable(dw)
        val tvPhone = view.findViewById<TextView>(R.id.phone_num)
        et_code = view.findViewById(R.id.code)
        sendCode = view.findViewById(R.id.sendCode)
        tv_djs = view.findViewById(R.id.tv_djs)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        val sure = view.findViewById<TextView>(R.id.sure)
        tvPhone.text = phone
        sendCode!!.setOnClickListener {
            sendCode!!.text = "重新获取"
            tv_djs!!.visibility = View.VISIBLE
            sendCode!!.visibility = View.GONE
            getNum(ph)
            getCaptchaTime()
        }
        cancel.setOnClickListener { popup!!.dismiss() }
        sure.setOnClickListener {
            val code0 = et_code!!.text.toString().trim()
            if(TextUtils.isEmpty(code0)){
                utils.showToast("请输入验证码")
            }else{
                utils.getProgress(this)
                checkCode(ph,code0)
            }
          }
    }

    private fun getNum(phone:String) {
        sendCode!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(phone, "5", object : BaseHttpCallBack(this) {}) }

    private fun checkCode(phone: String, code: String) {
        HttpRequestPort.instance.checkCode(phone,
                code, "5", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    changeDevice()
                } else {
                    utils.showToast("验证码错误")
                }
            }

            override fun onFinished() {
                super.onFinished()
                et_code!!.setText("".toCharArray(), 0, "".length)
                sendCode!!.text = "获取验证码"
                popup!!.dismiss()
                utils.hindProgress()
            }
        })
    }
    private fun getCaptchaTime() {
        mRunnable = object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                captchaTime--
                if (captchaTime == -1) {
                    // 异常处理
                    mHandler!!.removeCallbacks(mRunnable)
                    return
                }
                if (captchaTime <= 0) {
                    // 停止。
                    mHandler!!.removeCallbacks(mRunnable)
                    sendCode!!.visibility = View.VISIBLE
                    tv_djs!!.visibility = View.GONE
                    tv_djs!!.text = "60秒后重试"
                    captchaTime = 60
                    return
                }
                tv_djs!!.text =  "$captchaTime 秒后重试"
                mHandler!!.postDelayed(this, 1000)
                // 延时时长
            }
        }
        mHandler!!.postDelayed(mRunnable, 1000)
        // 打开定时器，执行操作
    }

    private fun changeDevice() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.changeDevice(userId,deviceId, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                utils.showToast("安全设备已变更")
                finish()
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("网络错误，请从新提交")
            }
        })
    }
}
