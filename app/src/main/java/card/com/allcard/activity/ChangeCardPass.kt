package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_change_card_pass.*
import kotlinx.android.synthetic.main.title.*

class ChangeCardPass : BaseActivity() {
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var captchaTime = 60
    override fun layoutId(): Int = R.layout.activity_change_card_pass

    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "重置交易密码"
        mHandler = Handler()
        close.setOnClickListener {
            finish()
        }
        tv_get.setOnClickListener {
            val phone = et_phone!!.text.toString().trim()
            if (TextUtils.isEmpty(phone) || phone == "输入办卡手机号") {
                utils.showToast("输入办卡手机号")
                return@setOnClickListener
            }
            getNum()
            getCaptchaTime()
        }
        img_ok.setOnClickListener {
            val name = et_name.text.toString().trim()
            val num = et_phone.text.toString().trim()
            val code = et_code.text.toString().trim()
            if (TextUtils.isEmpty(name) || name == "请输入姓名") {
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
            startActivity<PayPassChangeActivity>()
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
        HttpRequestPort.instance.sandMessage(et_phone!!.text.toString(), "8", object : BaseHttpCallBack(this) {})
    }
}
