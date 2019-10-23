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
import kotlinx.android.synthetic.main.activity_real_name_to.*
import kotlinx.android.synthetic.main.title.*

class RealNameTo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_real_name_to
    var phone = ""
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        address.text = "请认证"
        mHandler = Handler()
        phone = mk.decodeString(Tool.PHONE, "")
        val phone1 = phone.substring(0, 3) + "****" + phone.substring(7, 11)
        et_phone.setText(phone1.toCharArray(), 0, phone1.length)
        send_code.setOnClickListener { sendCode() }
        img_ok.setOnClickListener {
            val name = et_name.text.toString().trim()
            val num = et_num.text.toString().trim()
            val code = et_code.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                utils.showToast("请输入姓名")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(num)) {
                utils.showToast("请输入身份证号")
                return@setOnClickListener
            }
            if (!utils.checkIdCard(num)) {
                utils.showToast("请输入正确的身份证号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(code)) {
                utils.showToast("请输入验证码")
                return@setOnClickListener
            }
            val userId = mk.decodeString(Tool.USER_ID, "")
            checkCode(phone,code,userId,name,num)
        }
    }

    private fun sendCode() {
        send_code.text = "重新获取"
        getNum()
        getCaptchaTime()
    }

    private fun getNum() {
        send_code!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(phone, "7", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
            }
        })
    }


    private fun checkCode(phone: String, code: String,userId: String,userName: String,userNum: String) {
        utils.getProgress(this)
        HttpRequestPort.instance.checkCode(phone,
                code, "7", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    realName(userId,userName,userNum)
                } else {
                    utils.showToast("验证码错误")
                    utils.hindProgress()
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("验证码验证失败")
                utils.hindProgress()
            }

        })
    }


    private fun realName(userId:String,userName:String,userNum:String) {
        HttpRequestPort.instance.realName(userId,userName,userNum,phone, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    startActivity<RealNameOk>()
                    finish()
                } else {
                    utils.showToast(bean.message)
                }
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
}
