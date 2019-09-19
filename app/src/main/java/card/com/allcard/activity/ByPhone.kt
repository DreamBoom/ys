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
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_by_phone.*
import kotlinx.android.synthetic.main.title.*

class ByPhone : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_by_phone
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    override fun initView() {
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        MyApplication.instance.addActivity(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        address.text = "手机验证"
        mHandler = Handler()
        val ph = mk.decodeString(Tool.PHONE)
        val phone1 = ph!!.substring(0, 3) + "****" + ph.substring(7, 11)
        phone.text = phone1
        next.setOnClickListener {
            val code = tv_code.text.toString().trim()
            if (TextUtils.isEmpty(code)) {
               utils.showToast("请输入验证码")
            } else {
                checkCode(ph, code)
            }
        }
        sendCode.setOnClickListener {
            sendCode.text = "重新获取"
            getNum(ph)
            getCaptchaTime()
        }
    }

    private fun getNum(phone: String) {
        sendCode!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(phone,
                "2", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    //改手机号已存在
                   utils.showToast(bean.message)
                }
            }
        })
    }

    private fun checkCode(phone: String, code: String) {
        HttpRequestPort.instance.checkCode(phone,
                code, "2", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    startActivity<ByPhone2>()
                    finish()
                } else {
                   utils.showToast("验证码错误")
                }
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
