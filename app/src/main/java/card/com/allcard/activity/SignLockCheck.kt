package card.com.allcard.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MD5Utils
import card.com.allcard.view.AliPayPainter
import cn.jpush.android.api.JPushInterface
import com.wangnan.library.listener.OnGestureLockListener
import kotlinx.android.synthetic.main.activity_sign_lock_check.*
import kotlinx.android.synthetic.main.title.*


class SignLockCheck : BaseActivity(), OnGestureLockListener {
    override fun layoutId(): Int = R.layout.activity_sign_lock_check
    private var shake: ObjectAnimator? = null
    private var sign: String? = null
    private var sure: TextView? = null
    internal var canBack = 0
    val mkBD = SplashActivity.mkBD
    val userId = mk.decodeString(Tool.USER_ID, "")
    override fun initView() {
        //防止退出后台进入验证界面
        mk.encode(Tool.BY_LOGIN, "1")
        MyApplication.instance.addActivity(this)
        val mkBD = SplashActivity.mkBD
        val userId = mk.decodeString(Tool.USER_ID, "")
        sign = mkBD!!.decodeString(userId+"sign", "")
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        shake = utils.shake(tv_sign)
        address!!.text = "验证手势密码"
        close.setOnClickListener {
            if (canBack == 1) {
                popup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
            } else {
                finish()
            }
        }
        glv!!.setPainter(AliPayPainter())
        glv!!.setGestureLockListener(this)
        sign_login.setOnClickListener { loginPopup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0) }
        showLoginPop()
        showPop()
    }

    override fun onStarted() {

    }

    override fun onProgress(progress: String) {

    }

    @SuppressLint("SetTextI18n")
    override fun onComplete(result: String) {
        sign = mkBD!!.decodeString(userId + "sign", "")
        val idNum = mk.decodeString(Tool.USER_ID, "")
        mk.encode(Tool.BY_LOGIN, "0")
        if (sign == result) {
            glv!!.clearView()
            mk.encode(userId + "n", 4)
            val intent = Intent()
            intent.putExtra("signOff", true)
            setResult(Tool.RESULTCODE_SUCCESS, intent)
            finish()
        } else {
            val signNum = mk.decodeInt(idNum + "n", 4)
            if (signNum < 1) {
                glv!!.clearView()
                canBack = 1
                tv_sign!!.text = "手势密码已失效，请重新登录"
                mk.clearAll()
                mkBD.encode(userId + "finger", "")
                mkBD.encode(userId + "sign", "")
                JPushInterface.deleteAlias(this@SignLockCheck,0)
                JPushInterface.clearAllNotifications(this@SignLockCheck)
                popup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
            } else {
                tv_sign!!.text = "密码错误，还可以输入" + signNum + "次"
                mk.encode(idNum + "n", signNum - 1)
                tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.red))
                shake!!.start()
                glv!!.showErrorStatus(400)
            }

        }
    }

    private var popup: PopupWindow? = null
    private fun showPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.to_login_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "请重新登录,手势密码已失效"
        sure.text = "重新登录"
        popup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        popup!!.isTouchable = true
        popup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        popup!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            popup!!.dismiss()
            mk.clearAll()
            mkBD!!.encode(userId+"finger","")
            mkBD!!.encode(userId+"sign","")
            JPushInterface.deleteAlias(this@SignLockCheck,0)
            JPushInterface.clearAllNotifications(this@SignLockCheck)
            MyApplication.instance.removeAllActivity()
            utils.startActivity(LoginActivity::class.java)
            finish()
        }
    }

    private var loginPopup: PopupWindow? = null
    private fun showLoginPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.login_dialog, null)
        sure = view.findViewById(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        val etNum = view.findViewById<EditText>(R.id.et_num)
        cancel.text = "取消"
        cancel!!.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
        cancel.setOnClickListener { v ->
            etNum.setText("")
            loginPopup!!.dismiss()
        }
        val photoNum = view.findViewById<TextView>(R.id.photo_num)
        val ph = mk.decodeString(Tool.PHONE, "")
        val phone = ph!!.substring(0, 3) + "****" + ph.substring(7, 11)
        photoNum.text = phone

        loginPopup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        loginPopup!!.isTouchable = true
        loginPopup!!.isOutsideTouchable = false

        val dw = ColorDrawable(0x00000000)
        loginPopup!!.setBackgroundDrawable(dw)
//        cancel.setOnClickListener { v ->
//            loginPopup!!.dismiss()
//            mk.clearAll()
//            mkBD.encode(userId+"finger","")
//            mkBD.encode(userId+"sign","")
//            jPush("")
//            JPushInterface.clearAllNotifications(this@SignLockCheck)
//            MyApplication.instance.removeAllActivity()
//            utils.startActivity(LoginActivity::class.java)
//            finish()
//        }
        sure!!.setOnClickListener { v ->
            val idNum = mk.decodeString(Tool.USER_ID, "")
            val password = etNum.text.toString().trim()
            val encrypt = MD5Utils.encrypt(password)
            val localPassword = mk.decodeString(Tool.PASSWORD, "")
            when {
                encrypt == localPassword -> {
                    loginPopup!!.dismiss()
                    canBack = 0
                    mk.encode(idNum + "n", 4)
                    val intent = Intent()
                    intent.putExtra("signOff", true)
                    setResult(Tool.RESULTCODE_SUCCESS, intent)
                    finish()
                }
                TextUtils.isEmpty(password) -> utils.showToast("请输入登录密码")
                else -> utils.showToast("密码输入错误,请重新输入")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onBackPressed() {
        if (canBack == 1) {
            popup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
        } else {
            super.onBackPressed()//注释掉这行,back键不退出activity
        }
    }
}
