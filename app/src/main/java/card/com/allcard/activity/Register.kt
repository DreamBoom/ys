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
import card.com.allcard.bean.LoginBean
import card.com.allcard.bean.RegistBean
import card.com.allcard.bean.RegisterBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MD5Utils
import card.com.allcard.utils.RegexUtils
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.title.*

class Register : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_register
    private var mRunnable: Runnable? = null
    private var mHandler: Handler? = null
    private var captchaTime = 60
    private var key: Int = 1
    val deviceId = utils.getDeviceId(this)
    private val phoneName = utils.PhoneName(this)
    private val buildVersion = utils.BuildVersion(this)
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        mHandler = Handler()
        login_password.addTextChangedListener(passWatcher(login_password))
        login_phone!!.addTextChangedListener(PhoneWatcher(login_phone))
        close.setOnClickListener { finish() }
        send_code.isEnabled = false
        bt_register.setOnClickListener {
            val name = login_phone!!.text.toString()
            if (TextUtils.isEmpty(name)) {
                utils.showToast("请输入手机号")
                return@setOnClickListener
            }
            if (RegexUtils.verifyUsername(name) != RegexUtils.VERIFY_SUCCESS) {
                utils.showToast("请输入正确手机号")
                return@setOnClickListener
            }
            if (login_password!!.text.isEmpty()) {
                utils.showToast("请输入密码")
                return@setOnClickListener
            }
            if (login_password!!.text.length < 6) {
                utils.showToast("密码长度为6-12位")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(login_code!!.text.toString().trim())) {
                utils.showToast("请输入验证码")
                return@setOnClickListener
            }
            checkCode(name, login_code!!.text.toString().trim())
        }
        send_code.setOnClickListener {
                send_code.text = "重新获取"
                getNum()
                getCaptchaTime()
        }
        tv_agree.setOnClickListener { utils.startOtherWeb(HttpRequestPort.H5_BASE_URL + "weixin/registrationAgreement.jsp?fixparam=android") }
    }

    private fun register() {
        utils.getProgress(this)
        val phone = login_phone!!.text.toString().trim()
        val trim1 = login_password!!.text.toString().trim()
        val trim2 = login_code!!.text.toString().trim()
        val password = MD5Utils.encrypt(trim1)
        HttpRequestPort.instance.register(phone, "3", password, trim2, "", "",
                "0", "0", "",
                object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<RegistBean>() {})
                        val status = bean.result
                        utils.showToast(bean.message)
                        if (status == "0") {
                            mk.encode(Tool.USER_NAME, bean.token[0].user_name)
                            mk.encode(Tool.USER_ID, bean.token[0].user_id)
                            mk.encode(Tool.PHONE, bean.token[0].phone)
                            mk.encode(Tool.IS_AUTH, bean.token[0].is_auth)
                            utils.showToast("注册成功，正在登录...")
                            login(phone, password)
                        } else {
                            utils.hindProgress()
                        }
                    }

                    override fun onError(throwable: Throwable, b: Boolean) {
                        super.onError(throwable, b)
                        utils.hindProgress()
                    }
                })
    }

    private fun login(phone: String, password: String) {
        utils.getProgress(this)
        HttpRequestPort.instance.login(phone, password, deviceId, phoneName, "Android $buildVersion",
                object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<LoginBean>() {})
                        if (bean!!.result == Tool.ZERO) {
                            val token = bean.token[0]!!
                            mk.encode(Tool.PASSWORD, password)
                            mk.encode(Tool.USER_NAME, token.user_name)
                            mk.encode(Tool.USER_ID, token.user_id)
                            mk.encode(Tool.USER_NUM, token.idCard)
                            SplashActivity.mkBD.encode(Tool.LOCK_PHONE, token.phone)
                            mk.encode(Tool.PHONE, token.phone)
                            mk.encode(Tool.IS_AUTH, token.is_auth)
                            mk.encode(Tool.HEADER, token.img)
                            mk.encode(Tool.REAL_NAME, token.real_name)
                            mk.encode(Tool.isExitDefaultDevice, token.isExitDefaultDevice)
                            mk.encode(Tool.EMAIL, token.email)
                            mk.encode(Tool.BINDQ, token.bindsecurityquestion)
                            mk.encode(Tool.BINDCARD, token.isbindcard)
                            mk.encode(Tool.BY_LOGIN, "1")
                            jPush(token.user_id + deviceId)
                            MyApplication.instance.removeAllActivity()
                            utils.startActivity(MainActivity::class.java)
                            finish()
                        } else {
                            utils.showToast(bean.message)
                        }
                    }

                    override fun onError(throwable: Throwable, b: Boolean) {
                        super.onError(throwable, b)
                        utils.showToast("登录失败,请检查网络或重新登录")
                    }

                    override fun onFinished() {
                        super.onFinished()
                        utils.hindProgress()
                    }
                })
    }
    private fun jPush(alias: String) {
        JPushInterface.setAlias(applicationContext, 0, alias)
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
                    utils.showToast("该手机号已经注册")
                } else {
                    // 改手机号不存在
                  send_code.isEnabled = true
                }
            }
        })
    }

    private fun getNum() {
        send_code!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(login_phone!!.text.toString(),
                key.toString() + "", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "1") {
                    //改手机号已存在
                    utils.showToast(bean.message)
                }
            }
        })
    }

    private fun checkCode(phone: String, code: String) {
        HttpRequestPort.instance.checkCode(phone,
                code, "1", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    register()
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
                    mHandler!!.removeCallbacks(mRunnable)
                    return
                }

                if (captchaTime <= 0) {
                    mHandler!!.removeCallbacks(mRunnable)
                    send_code!!.visibility = View.VISIBLE
                    tv_djs!!.visibility = View.GONE
                    tv_djs!!.text = "60秒后重试"
                    captchaTime = 60
                    return
                }

                tv_djs!!.text = "$captchaTime 秒后重试"
                mHandler!!.postDelayed(this, 1000)
            }
        }

        mHandler!!.postDelayed(mRunnable, 1000)
    }

    internal inner class passWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

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

    internal inner class PhoneWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            val i = 11
            if (login_phone!!.text.length < i) {
                send_code.isEnabled = false
                return
            }
            if (login_phone!!.text.length == 11) {
                if (RegexUtils.verifyUsername(login_phone!!.text.toString().trim()) != RegexUtils.VERIFY_SUCCESS) {
                    utils.showToast("您输入的手机号不正确!")
                    send_code.isEnabled = false
                    return
                }
            }
            if (login_phone!!.text.length == 11 && RegexUtils.verifyUsername(
                            login_phone!!.text.toString().trim()) == RegexUtils.VERIFY_SUCCESS) {
                isExit(login_phone!!.text.toString().trim())
            }
        }
    }
}
