package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.RegexUtils
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_set_sms.*
import kotlinx.android.synthetic.main.title.*

class SetSms : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_set_sms
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    var type = ""
    override fun initView() {
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        MyApplication.instance.addActivity(this)
        close.setOnClickListener { finish() }
        type = intent.getStringExtra("type")
        when (type) {
            "0" -> address.text = "邮箱添加"
            "1" -> address.text = "邮箱修改"
            "4" -> address.text = "邮箱添加"
        }
        mHandler = Handler()
        next.setOnClickListener {
            val eamil = sms_num.text.toString().trim()
            if (TextUtils.isEmpty(eamil)) {
                utils.showToast("请输入邮箱账号")
                return@setOnClickListener
            }
            if (!RegexUtils.isEmail(eamil)) {
                utils.showToast("请输入正确的邮箱账号")
                return@setOnClickListener
            }
            val code = code.text.toString().trim()
            if (TextUtils.isEmpty(code)) {
                utils.showToast("请输入验证码")
            } else {
                bindSms(code)
            }

        }
        sendCode.setOnClickListener {
            val eamil = sms_num.text.toString().trim()
            if (TextUtils.isEmpty(eamil)) {
                utils.showToast("请输入正确的邮箱账号")
                return@setOnClickListener
            }
            if (RegexUtils.isEmail(eamil)) {
                if (type == "0") {
                    getNum(eamil, "0")
                } else if (type == "1") {
                    getNum(eamil, "1")
                } else {
                    getNum(eamil, "4")
                }
                getCaptchaTime()
                sendCode.text = "重新获取"
            } else {
                utils.showToast("请输入正确的邮箱账号")
            }
        }
    }

    private fun getNum(eamil: String, type: String) {
        sendCode!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sendEmail(eamil, type, object : BaseHttpCallBack(this) {
            override fun onSuccess(s: String) {
                super.onSuccess(s)
                val bean = JSONObject.parseObject(s, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    utils.showToast(bean.message)
                }
            }
        })
    }

    private fun bindSms(code: String) {
        val eamil = sms_num.text.toString().trim()
        val num = when (type) {
            "0" -> "0"
            "1" -> "1"
            else -> "4"
        }
        HttpRequestPort.instance.checkEamilcode(eamil, num, code, object : BaseHttpCallBack(this) {
            override fun onSuccess(s: String) {
                super.onSuccess(s)
                val bean = JSONObject.parseObject(s, object : TypeReference<GetNum>() {})
                when (bean.result) {
                    "0" -> {
                        when (type) {
                            "0" -> bind()
                            "1" -> upSms()
                            "4" -> bind()
                        }
                    }
                    else -> utils.showToast(bean.message)
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("绑定失败！")
            }
        })
    }

    fun bind() {
        val eamil = sms_num.text.toString().trim()
        val phone = mk.decodeString(Tool.PHONE, "")
        HttpRequestPort.instance.eamilBind(phone, eamil, object : BaseHttpCallBack(this) {
            override fun onSuccess(s: String) {
                super.onSuccess(s)
                val bean = JSONObject.parseObject(s, object : TypeReference<GetNum>() {})
                utils.showToast(bean.message)
                if ("0" == bean.result) {
                    mk.encode(Tool.EMAIL, eamil)
                    finish()
                }
            }
        })
    }

    private fun upSms() {
        val eamil = sms_num.text.toString().trim()
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.upEmail(userId, eamil, object : BaseHttpCallBack(this) {
            override fun onSuccess(s: String) {
                super.onSuccess(s)
                val bean = JSONObject.parseObject(s, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    mk.encode(Tool.EMAIL, eamil)
                    utils.showToast("修改成功")
                    finish()
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("修改失败")
            }

            override fun onFinished() {
                super.onFinished()
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
}
