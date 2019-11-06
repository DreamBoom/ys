package card.com.allcard.activity

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import card.com.allcard.R
import card.com.allcard.bean.LoginBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MD5Utils
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.act_login.*
import kotlinx.android.synthetic.main.title.*

/**
 * @author Created by Dream
 * 登录页
 */
class LoginActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.act_login
    var loginPhone = ""
    val deviceId = utils.getDeviceId(this)
    private val phoneName = utils.PhoneName(this)
    private val buildVersion = utils.BuildVersion(this)
    private var from = 0
    override fun onResume() {
        super.onResume()
        loginPhone = SplashActivity.mkBD.decodeString(Tool.LOCK_PHONE, "")
        et_username.setText(loginPhone.toCharArray(), 0, loginPhone.length)
        et_username.setSelection(loginPhone.length)
        from = intent.getIntExtra("from",0)
    }

    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)

        if (!TextUtils.isEmpty(loginPhone)) {
            im_clear.visibility = View.VISIBLE
        }
        //禁止输入空格
        et_password.addTextChangedListener(passWatcher(et_password))
        tv_forget.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java).putExtra("from", 0))
        }
        tv_register.setOnClickListener { startActivity<Register>() }
        et_username!!.addTextChangedListener(watcher(et_username))
        close.setOnClickListener {
            if (from == 1) {
                finish()
            } else {
                startActivity<MainActivity>()
                finish()
            }
        }
        im_clear.setOnClickListener {
            im_clear.visibility = View.GONE
            et_username.setText("".toCharArray(), 0, "".length)
        }
        bt_login.setOnClickListener { it ->
            val name = et_username!!.text.toString()
            val pass = et_password!!.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                utils.showToast("请输入登录账号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pass)) {
                utils.showToast("请输入密码")
                return@setOnClickListener
            }
            if (pass.length < 6) {
                utils.showToast("密码不能少于6位")
                return@setOnClickListener
            }
            val password = MD5Utils.encrypt(pass)
            login(name, password)
        }
    }

    internal inner class passWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
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

        override fun afterTextChanged(editable: Editable) {}
    }

    internal inner class watcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            val i = 1
            if (editText.text.length < i) {
                im_clear.visibility = View.GONE
            } else {
                im_clear.visibility = View.VISIBLE
            }
        }
    }

    private fun login(phone: String, password: String) {
        bt_login.isClickable = false
        utils.getProgress(this)
        HttpRequestPort.instance.login(phone, password, deviceId, phoneName, "Android $buildVersion",
                object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<LoginBean>() {})
                        if (bean!!.result== "0") {
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
                            SplashActivity.mkBD.encode(Tool.isExitDefaultDevice, token.isExitDefaultDevice)
                            mk.encode(Tool.EMAIL, token.email)
                            mk.encode(Tool.BINDQ, token.bindsecurityquestion)
                            mk.encode(Tool.BINDCARD, token.isbindcard)
                            mk.encode(Tool.BY_LOGIN, "1")
                            jPush(token.user_id + deviceId)
                            val i = Intent(this@LoginActivity, MainActivity::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(i)
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
                        runDelayed(1000) {
                            bt_login.isClickable = true
                        }
                    }
                })
    }


    private fun jPush(alias: String) {
        JPushInterface.setAlias(applicationContext, 0, alias)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (from == 1) {
                finish()
            } else {
                startActivity<MainActivity>()
                finish()
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
