package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_by_sms.*
import kotlinx.android.synthetic.main.title.*

class BySms : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_by_sms
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    var type = ""
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        MyApplication.instance.addActivity(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        address.text = "邮箱验证"
        val email = mk.decodeString(Tool.EMAIL, "")
        val split = email.split("@")
        val email1 = split[0].substring(0, 2) + "****" +split[0].substring(split[0].length-1,split[0].length)
        tv_email.text = email1+"@"+split[1]
        mHandler = Handler()
        type = intent.getStringExtra("type")
        next.setOnClickListener {
            val code = tv_code.text.toString().trim()
            if(TextUtils.isEmpty(code)){
                utils.showToast("请输入验证码")
            }else{
                bindSms(code)
            }
           }
        sendCode.setOnClickListener {
            sendCode.text="重新获取"
            getNum(email)
            getCaptchaTime()
        }
    }

    private fun getNum(eamil: String) {
        sendCode!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sendEmail(eamil, type,object : BaseHttpCallBack(this) {
            override fun onSuccess(s: String) {
                super.onSuccess(s)
                val bean = JSONObject.parseObject(s, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    utils.showToast(bean.message)
                }
            }
        })
    }

    private fun bindSms(code:String) {
        val email = mk.decodeString(Tool.EMAIL, "")
        HttpRequestPort.instance.checkEamilcode(email, type,code, object : BaseHttpCallBack(this) {
            override fun onSuccess(s: String) {
                super.onSuccess(s)
                val bean = JSONObject.parseObject(s, object : TypeReference<GetNum>() {})
                utils.showToast(bean.message)
                if (bean.result == "0") {
                    when (type) {
                        "4" -> {
                            startActivity<ByPhone2>()
                            finish()
                        }
                        "1" -> {
                            startActivity(Intent(this@BySms, SetSms::class.java).putExtra("type", "1"))
                            finish()
                        }
                    }
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("发送失败")
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
