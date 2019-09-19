package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.utils.RegexUtils
import kotlinx.android.synthetic.main.activity_pay_bind.*

class PayBind : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_pay_bind
    private var mRunnable: Runnable? = null
    private var mHandler: Handler? = null
    private var captchaTime = 60
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        mHandler = Handler()
        send_code.setOnClickListener {
            val name = et_phone!!.text.toString()
            if (!TextUtils.isEmpty(name) && RegexUtils.verifyUsername(name) == RegexUtils.VERIFY_SUCCESS) {
                    send_code.text = "重新获取"
                    getNum()
                    getCaptchaTime()
                }
            }
    }

    private fun getNum() {
        send_code!!.visibility = View.GONE
        tv_djs!!.visibility = View.VISIBLE
        //请求网络发送验证码
        HttpRequestPort.instance.sandMessage(et_phone!!.text.toString(),
                "", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
//                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
//                val status = bean.message[0].status
//                if (status == str) {
//                    //改手机号已存在
//                    utils.showToast(bean.message[0].message)
//                }
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
