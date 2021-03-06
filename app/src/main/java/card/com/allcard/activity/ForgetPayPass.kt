package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_forget_pay_pass.*
import kotlinx.android.synthetic.main.title.*

class ForgetPayPass : BaseActivity() {
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    override fun layoutId(): Int = R.layout.activity_forget_pay_pass

    @SuppressLint("DefaultLocale")
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "重置账户交易密码"
        var loginPhone = SplashActivity.mkBD.decodeString(Tool.LOCK_PHONE, "")
        et_phone.setText(loginPhone.toCharArray(), 0, loginPhone.length)
        mHandler = Handler()
        close.setOnClickListener {
            finish()
        }
        tv_get.setOnClickListener {
            val phone = et_phone!!.text.toString().trim()
            if (TextUtils.isEmpty(phone) || phone == "输入手机号") {
                utils.showToast("请输入手机号")
                return@setOnClickListener
            }
            getNum()
            getCaptchaTime()
            tv_get.text = "重新获取"
        }
        img_ok.setOnClickListener {
            val realName = mk.decodeString(Tool.REAL_NAME, "")
            val userNum = mk.decodeString(Tool.USER_NUM, "")
            val name = et_name.text.toString().trim()
            val num = et_num.text.toString().trim().toUpperCase()
            val code = et_code.text.toString().trim()
            if (TextUtils.isEmpty(name) || name == "请输入姓名") {
                utils.showToast("请输入姓名")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(num) || num == "请输入身份证号") {
                utils.showToast("请输入身份证号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(code) || code == "输入验证码") {
                utils.showToast("请输入验证码")
                return@setOnClickListener
            }
            if (realName!=name ) {
                utils.showToast("请输入正确姓名")
                return@setOnClickListener
            }
            if (userNum != num) {
                utils.showToast("请输入正确身份证号")
                return@setOnClickListener
            }
            check()
        }
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
                    tv_get!!.visibility = View.VISIBLE
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

    private fun getNum() {
        tv_get!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(et_phone!!.text.toString(), "c", object : BaseHttpCallBack(this) {})
    }

    private fun check(){
        utils.getProgress(this)
        val trim = et_code.text.toString().trim()
            HttpRequestPort.instance.checkCode(et_phone!!.text.toString(), trim,"c", object : BaseHttpCallBack(this) {
                override fun success(data: String) {
                    super.success(data)
                    val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                    val status = bean.result
                    if (status == "0") {
                        val bundle = Bundle()
                        bundle.putInt("type", 0)
                        bundle.putString("cardNo", "")
                        utils.startActivityBy(PayPassChangeActivity::class.java, bundle)
                        finish()
                    } else {
                        utils.showToast("验证码错误")
                    }
                }
                override fun onError(throwable: Throwable, b: Boolean) {
                    super.onError(throwable, b)
                    utils.showToast("验证码验证失败")
                    utils.hindProgress()
                }

                override fun onFinished() {
                    super.onFinished()
                    utils.hindProgress()
                }
            })
        }
}
