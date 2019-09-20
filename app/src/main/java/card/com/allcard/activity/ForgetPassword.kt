package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import card.com.allcard.R
import card.com.allcard.bean.RegisterBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.utils.MD5Utils
import card.com.allcard.utils.RegexUtils
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_forget.*
import kotlinx.android.synthetic.main.title.*

/**
 * @author Created by Dream
 */
class ForgetPassword : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_forget
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    private val str = "0"
    private var from = 0
    private var click = 0
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "忘记密码"
        et_phone!!.addTextChangedListener(phoneWatcher(et_phone))
        et_password.addTextChangedListener(passWatcher(et_password))
        et_password1.addTextChangedListener(passWatcher(et_password1))
        mHandler = Handler()
        from = intent.getIntExtra("from", 0)
        close.setOnClickListener {
            if(from == 0){
                MyApplication.instance.removeAllActivity()
                utils.startActivity(LoginActivity::class.java)
                finish()
            }else{
                finish()
            }
          }
        tvGet.setOnClickListener {
            val phone = et_phone!!.text.toString().trim()
            if (TextUtils.isEmpty(phone) || phone == "输入手机号") {
                utils.showToast("请输入手机号")
                return@setOnClickListener
            }

            if ( click == 1 ) {
                getNum()
                getCaptchaTime()
            }else{
                utils.showToast("该手机号不存在！")
            }
        }
        img_ok.setOnClickListener {
            val phone = et_phone!!.text.toString().trim()
            val pass = et_password!!.text.toString().trim()
            val pass2 = et_password1!!.text.toString().trim()
            if (TextUtils.isEmpty(phone) || phone == "输入手机号") {
                utils.showToast("请输入手机号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pass)) {
                utils.showToast("请输入密码")
                return@setOnClickListener
            }

            if (pass.length<6 || pass == "设置登录密码") {
                utils.showToast("密码长度为6-12位")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pass2)) {
                utils.showToast("请确认密码")
                return@setOnClickListener
            }
            if (pass != pass2) {
                utils.showToast("密码输入不一致")
                return@setOnClickListener
            }

            val num = et_num!!.text.toString().trim()
            if (TextUtils.isEmpty(num) || num == "输入验证码") {
                utils.showToast("请输入验证码")
                return@setOnClickListener
            }
            forgetPassword()
        }
    }

    private fun forgetPassword() {
        utils.getProgress(this)
        val trim = et_password!!.text.toString().trim()
        val encrypt = MD5Utils.encrypt(trim)
        val deviceId = utils.getDeviceId(this)
        HttpRequestPort.instance.forgotpasswd(deviceId,et_phone!!.text.toString().trim(),
                encrypt, et_num!!.text.toString().trim(),
                object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<RegisterBean>() {})
                        utils.showToast(bean.message)
                        val str = "您输入的验证码有误！"
                        if (bean.message != str) {
                            MyApplication.instance.removeAllActivity()
                            utils.startActivity(LoginActivity::class.java)
                            finish()
                        }

                    }

                    override fun onError(throwable: Throwable, b: Boolean) {
                        super.onError(throwable, b)
                        utils.showToast(""+throwable.message)
                    }
                    override fun onFinished() {
                        super.onFinished()
                        utils.hindProgress()
                    }
                })
    }

    private fun isExit(phone: String) {
        //请求网络验证手机号是否存在
        HttpRequestPort.instance.checkPhone(phone, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<RegisterBean>() {})
                val status = bean.result
                if (status == "1") {
                    //改手机号已存在
                    click = 1
                } else {
                    // 改手机号不存在
                    utils.showToast(bean.message)
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
                    tvGet!!.visibility = View.VISIBLE
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
        tvGet!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(et_phone!!.text.toString(), "6", object : BaseHttpCallBack(this) {})
    }

    internal inner class passWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString().contains(" ")) {
                val str = s.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var str1 = ""
                for (i in str.indices) {
                    str1 += str[i]
                }
                editText.setText(str1)
                editText.setSelection(start)
            }
        }

        override fun afterTextChanged(editable: Editable) {

        }
    }

    internal inner class phoneWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable) {
            val i = 11
            if (et_phone!!.text.length < i) {
                click = 0
                return
            }
            if (et_phone!!.text.length == 11) {
                if (RegexUtils.verifyUsername(et_phone!!.text.toString().trim()) != RegexUtils.VERIFY_SUCCESS) {
                    utils.showToast("您输入的手机号不正确!")
                    click = 0
                    return
                }
            }
            if (et_phone!!.text.length == 11 && RegexUtils.verifyUsername(
                            et_phone!!.text.toString().trim()) == RegexUtils.VERIFY_SUCCESS) {
                isExit(et_phone!!.text.toString().trim())
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_BACK) {
            if(from == 0){
                MyApplication.instance.removeAllActivity()
                utils.startActivity(LoginActivity::class.java)
                finish()
            }else{
                finish()
            }
            return false
        }
        return super.dispatchKeyEvent(event)
    }
}
