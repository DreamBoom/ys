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
import kotlinx.android.synthetic.main.activity_pay_bind.*
import kotlinx.android.synthetic.main.title.*

class PayBind : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_pay_bind
    private var mRunnable: Runnable? = null
    private var mHandler: Handler? = null
    private var captchaTime = 60
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address.text = "请绑定"
        val nickName = intent.getStringExtra("nickName")
        mHandler = Handler()
        send_code.setOnClickListener {
            val phone = et_phone!!.text.toString()
            if (!TextUtils.isEmpty(phone) && RegexUtils.verifyUsername(phone) == RegexUtils.VERIFY_SUCCESS) {
                send_code.text = "重新获取"
                getNum()
                getCaptchaTime()
            } else {
                utils.showToast("请输入正确手机号")
            }
        }
        img_ok.setOnClickListener {
            val phone = et_phone!!.text.toString()
            if (TextUtils.isEmpty(phone)) {
                utils.showToast("请输入手机号")
                return@setOnClickListener
            }
            val name = et_name!!.text.toString()
            if (TextUtils.isEmpty(name)) {
                utils.showToast("请输入姓名")
                return@setOnClickListener
            }
            val num = et_num!!.text.toString()
            if (TextUtils.isEmpty(num)) {
                utils.showToast("请输入身份证号")
                return@setOnClickListener
            }
            val code = et_code!!.text.toString()
            if (TextUtils.isEmpty(code)) {
                utils.showToast("请输入验证码")
                return@setOnClickListener
            }
            checkCode(name,nickName,num,phone, code)
        }
    }

    private fun getNum() {
        send_code!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(et_phone!!.text.toString(), "m", object : BaseHttpCallBack(this) {})
    }

    private fun checkCode(realName: String,nickName: String,certNo: String,phone: String, code: String) {
        HttpRequestPort.instance.checkCode(phone,
                code, "m", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    bind(realName,nickName,certNo,phone)
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
                    send_code!!.visibility = View.VISIBLE
                    tv_djs!!.visibility = View.GONE
                    tv_djs!!.text = "60秒后重试"
                    captchaTime = 60
                    return
                }
                tv_djs!!.text = "$captchaTime 秒后重试"
                mHandler!!.postDelayed(this, 1000)
                // 延时时长
            }
        }
        mHandler!!.postDelayed(mRunnable, 1000)
        // 打开定时器，执行操作
    }

    private fun bind(realName: String,nickName:String,certNo:String,phone:String) {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.smrz(userId,realName, nickName,certNo,phone, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                   finish()
                }else{
                    utils.showToast(bean.message)
                }
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })

    }
}
