package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import card.com.allcard.R
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.utils.RegexUtils
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_change_card_pass.*
import kotlinx.android.synthetic.main.title.*

class ChangeCardPass : BaseActivity() {
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    var cardNo = ""
    var phone0 = ""
    override fun layoutId(): Int = R.layout.activity_change_card_pass

    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "重置交易密码"
        mHandler = Handler()
        cardNo = intent.getStringExtra("cardNo")
        phone0 = intent.getStringExtra("phone")
        val name = intent.getStringExtra("name")
        close.setOnClickListener {
            finish()
        }
        tv_get.isEnabled = false
        et_phone!!.addTextChangedListener(PhoneWatcher(et_phone))
        tv_get.setOnClickListener {
            val phone = et_phone!!.text.toString().trim()
            if (TextUtils.isEmpty(phone) || phone == "输入办卡手机号") {
                utils.showToast("输入办卡手机号")
                return@setOnClickListener
            }
            tv_get.text= "重新获取"
            getNum()
            getCaptchaTime()
        }
        img_ok.setOnClickListener {
            val name0 = et_name.text.toString().trim()
            val num = et_phone.text.toString().trim()
            val code = et_code.text.toString().trim()
            if (TextUtils.isEmpty(name0) || name0 == "请输入姓名") {
                utils.showToast("请输入办卡姓名")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(num) || num == "请输入办卡手机号") {
                utils.showToast("请输入办卡手机号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(code) || code == "输入验证码") {
                utils.showToast("请输入验证码")
                return@setOnClickListener
            }
            if (name != name0) {
                utils.showToast("办卡人姓名错误")
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
                    bundle.putInt("type", 1)
                    bundle.putString("cardNo", cardNo)
                    utils.startActivityBy(PayPassChangeActivity::class.java, bundle)
                    finish()
                } else {
                    utils.showToast("验证码错误")
                }
            }
            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("验证码验证失败")
            }

            override fun onFinished() {
                super.onFinished()
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
            val i = 11
            if (et_phone!!.text.length < i) {
                tv_get.isEnabled = false
                return
            }
            if (et_phone!!.text.length == 11) {
                if (RegexUtils.verifyUsername(et_phone!!.text.toString().trim()) != RegexUtils.VERIFY_SUCCESS||et_phone!!.text.toString().trim()!=phone0) {
                    utils.showToast("您输入的手机号不正确!")
                    tv_get.isEnabled = false
                    return
                }
            }
            if (et_phone!!.text.length == 11 && RegexUtils.verifyUsername(
                            et_phone!!.text.toString().trim()) == RegexUtils.VERIFY_SUCCESS) {
                tv_get.isEnabled = true
            }
        }
    }
}
