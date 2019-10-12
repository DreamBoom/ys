package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import card.com.allcard.R
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.RegexUtils
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_by_phone2.*
import kotlinx.android.synthetic.main.title.*

class ByPhone2 : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_by_phone2
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    var click = 0
    override fun initView() {
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        MyApplication.instance.addActivity(this)
        close.setOnClickListener { finish() }
        address.text = "绑定新手机"
        mHandler = Handler()
        phone!!.addTextChangedListener(PhoneWatcher(phone))
        get_num.setOnClickListener {
            if(click == 1){
                get_num.text = "重新获取"
                get_num!!.setTextColor(resources.getColor(R.color.blue))
                getNum(phone.text.toString().trim())
                getCaptchaTime()
            }
        }
        bt_bind.setOnClickListener {
            val code = tv_code.text.toString().trim()
            val phone = phone.text.toString().trim()
            when {
                RegexUtils.verifyUsername(phone) != RegexUtils.VERIFY_SUCCESS ->utils.showToast("请输入手机号")
                TextUtils.isEmpty(code) ->utils.showToast("请输入验证码")
                else -> checkCode(phone, code)
            }
        }
    }

    private fun isExit(phone: String) {
        //请求网络验证手机号是否存在
        HttpRequestPort.instance.checkPhone(phone, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "1") {
                    //改手机号已存在
                    click = 0
                    get_num!!.setTextColor(resources.getColor(R.color.text_gray))
                    utils.showToast("该手机号已经注册")
                } else {
                    // 改手机号不存在
                    click = 1
                    get_num!!.setTextColor(resources.getColor(R.color.blue))
                }
            }
        })
    }

    private fun getNum(phone: String) {
        get_num!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(phone,
                "3", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                   utils.showToast(bean.message)
                }
            }
        })
    }

    private fun checkCode(phone: String, code: String) {
        HttpRequestPort.instance.checkCode(phone,
                code, "3", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    bindNewPhone()
                } else {
                   utils.showToast("验证码错误")
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
               utils.showToast("验证码验证失败")
            }
        })
    }

    fun bindNewPhone() {
        val deviceId = utils.getDeviceId(this)
        val ph = phone.text.toString().trim()
        val userId = mk.decodeString(Tool.USER_ID, "")
        utils.getProgress(this)
        HttpRequestPort.instance.upPhone(deviceId,userId, ph, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                utils.showToast(bean.message)
                val status = bean.result
                if (status == "0") {
                    utils.showToast("绑定成功")
                    SplashActivity.mkBD!!.encode(Tool.LOCK_PHONE, ph)
                    mk.clearAll()
                    JPushInterface.deleteAlias(this@ByPhone2,0)
                    JPushInterface.clearAllNotifications(this@ByPhone2)
                    MyApplication.instance.removeAllActivity()
                    startActivity<LoginActivity>()
                    finish()
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
               utils.showToast("绑定失败")
            }

            override fun onFinished() {
                super.onFinished()
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
                    get_num!!.visibility = View.VISIBLE
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

    internal inner class PhoneWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            val i = 11
            if (phone.text.length < i) {
                click = 0
                get_num!!.setTextColor(resources.getColor(R.color.text_gray))
                return
            }
            if (phone!!.text.length == 11) {
                if (RegexUtils.verifyUsername(phone!!.text.toString().trim()) != RegexUtils.VERIFY_SUCCESS) {
                    utils.showToast("您输入的手机号不正确!")
                    click = 0
                    get_num!!.setTextColor(resources.getColor(R.color.text_gray))
                    return
                }
            }
            if (phone!!.text.length == 11 && RegexUtils.verifyUsername(
                            phone!!.text.toString().trim()) == RegexUtils.VERIFY_SUCCESS) {
                isExit(phone!!.text.toString().trim())
            }
        }
    }

}

