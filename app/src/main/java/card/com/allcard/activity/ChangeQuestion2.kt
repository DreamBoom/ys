package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.view.View
import card.com.allcard.R
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_change_question2.*
import kotlinx.android.synthetic.main.title.*

class ChangeQuestion2 : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_change_question2
    private var mRunnable: Runnable? = null
    private var mHandler: Handler? = null
    private var captchaTime = 60
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        MyApplication.instance.addActivity(this)
        address.text = "手机验证"
        close.setOnClickListener { finish() }
        mHandler = Handler()
        val phone = mk.decodeString(Tool.PHONE, "")
        val phone1 = phone!!.substring(0, 3) + "****" + phone.substring(7, 11)
        tv_phone.setText(phone1.toCharArray(), 0, phone1.length)
        send_code.setOnClickListener {
            tv_djs2!!.visibility = View.VISIBLE
            send_code!!.visibility = View.GONE
            send_code.text = "重新获取"
            getNum(phone)
            getCaptchaTime()
        }

        bt_next.setOnClickListener {
            val code = tv_code2.text.toString().trim()
            checkCode(phone,code)
            }
    }

    private fun getNum(phone: String) {
        send_code!!.visibility = View.GONE
        tv_djs2!!.visibility = View.VISIBLE
        HttpRequestPort.instance.sandMessage(phone,
                 "4", object : BaseHttpCallBack(this) {
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

    private fun checkCode (phone:String, code:String){
        HttpRequestPort.instance.checkCode(phone,
                code,"4", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                val status = bean.result
                if (status == "0") {
                    startActivity(Intent(this@ChangeQuestion2, SetQuestion::class.java).putExtra("type", "1"))
                    finish()
                }else{
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
                    send_code!!.visibility = View.VISIBLE
                    tv_djs2!!.visibility = View.GONE
                    tv_djs2!!.text = "60秒后重试"
                    captchaTime = 60
                    return
                }
                tv_djs2!!.text =  "$captchaTime 秒后重试"
                mHandler!!.postDelayed(this, 1000)
                // 延时时长
            }
        }
        mHandler!!.postDelayed(mRunnable, 1000)
        // 打开定时器，执行操作
    }
}
