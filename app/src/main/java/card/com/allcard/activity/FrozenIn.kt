package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import card.com.allcard.R
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_frozen_in.*
import kotlinx.android.synthetic.main.title.*

class FrozenIn : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_frozen_in
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    var type = "0"
    @SuppressLint("SetTextI18n")
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address.text = "账户冻结"
        type = intent.getStringExtra("type")
        if (type != "0") {
            address.text = "账户解冻"
            bt_frozen.text = "确认解冻"
            tit.text = "解冻须知"
            ll01.visibility = View.GONE
            ll02.visibility = View.VISIBLE
        }
        val ph = mk.decodeString(Tool.PHONE, "")
        val phone1 = ph!!.substring(0, 3) + "****" + ph.substring(7, 11)
        phone.text = "发送验证码至 $phone1"
        mHandler = Handler()
        et_code!!.addTextChangedListener(PhoneWatcher(et_code))
        close.setOnClickListener {
            finish()
        }
        send_code.setOnClickListener {
            send_code.text = "再次获取"
            getNum()
            getCaptchaTime()
        }
        bt_frozen.isEnabled = false
        bt_frozen.setOnClickListener {
            check()
        }
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

    private fun getNum() {
        send_code!!.visibility = View.GONE
        send_code.text = "重新获取"
        tv_djs!!.visibility = View.VISIBLE
        val ph = mk.decodeString(Tool.PHONE, "")
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(ph, "d", object : BaseHttpCallBack(this) {})
    }

    private fun check() {
        val ph = mk.decodeString(Tool.PHONE, "")
        val trim = et_code.text.toString().trim()
        HttpRequestPort.instance.checkCode(ph, trim, "d", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    if (type == "0") {
                        frozenIn()
                    } else {
                        frozenOut()
                    }
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

    fun frozenIn() {
        utils.getProgress(this)
        val ph = mk.decodeString(Tool.PHONE, "")
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.accountFrozen(userId, ph, "1", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    utils.showToast(bean.message)
                    finish()
                } else {
                    utils.hindProgress()
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("冻结请求失败")
                utils.hindProgress()
            }
        })
    }

    fun frozenOut() {
        utils.getProgress(this)
        val ph = mk.decodeString(Tool.PHONE, "")
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.accountFrozen(userId, ph, "2", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    utils.showToast(bean.message)
                    finish()
                }else{
                    utils.hindProgress()
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("解冻请求失败")
                utils.hindProgress()
            }
        })
    }


    internal inner class PhoneWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            val y = 6
            bt_frozen.isEnabled = editText.text.length >= y
        }
    }
}
