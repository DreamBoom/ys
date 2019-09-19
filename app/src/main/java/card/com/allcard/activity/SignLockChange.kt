package card.com.allcard.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MD5Utils
import card.com.allcard.view.AliPayPainter
import cn.jpush.android.api.JPushInterface
import com.wangnan.library.listener.OnGestureLockListener
import kotlinx.android.synthetic.main.activity_sign_lock_change.*
import kotlinx.android.synthetic.main.title.*

class SignLockChange : BaseActivity(), OnGestureLockListener {
    override fun layoutId(): Int = R.layout.activity_sign_lock_change
    internal var state = 0
    internal var canBack = 0
    private var shake: ObjectAnimator? = null
    private var sign: String? = null
    private var loginPopup: PopupWindow? = null
    private var sure: TextView? = null
    val mkBD = SplashActivity.mkBD
    val userId = mk.decodeString(Tool.USER_ID, "")
    override fun initView() {
        MyApplication.instance.addActivity(this)
        sign = mkBD!!.decodeString(userId+"sign", "")
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "验证手势密码"
        showPop()
        close.setOnClickListener {
            if (canBack == 1) {
                showPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
            } else {
                finish()
            }
        }
        shake = utils.shake(tv_sign)
        glv!!.setPainter(AliPayPainter())
        glv!!.setGestureLockListener(this)
        right_menu.setOnClickListener {
            state = 1
            right_menu!!.visibility = View.GONE
            tv_sign!!.text = "绘制解锁图案"
            mkBD.encode(userId+"sign", "")
            tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.black))
            s_glt!!.setThumbnailView("012345678", ContextCompat.getColor(this, R.color.NormalColor))
            glv!!.clearView()
        }
        sign_login.setOnClickListener { loginPopup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0) }
        showLoginPop()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onStarted() {

    }

    override fun onProgress(progress: String) {

    }

    @SuppressLint("SetTextI18n")
    override fun onComplete(result: String) {
        mk.encode(Tool.BY_LOGIN, "0")
        val idNum = mk.decodeString(Tool.USER_ID, "")
        val signNum = mk.decodeInt(idNum + "n", 4)
        when (state) {
            0 -> if (sign == result) {
                address!!.text = "设置手势密码"
                val linearParams = s_glt.layoutParams as RelativeLayout.LayoutParams //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
                linearParams.height = 220// 控件的宽强制设成60
                s_glt.layoutParams = linearParams //使设置好的布局参数应用到控件
                s_glt!!.visibility = View.VISIBLE
                state = 1
                tv_sign!!.text = "绘制解锁图案"
                sign_login!!.visibility = View.GONE
                glv!!.clearView()
                tv_sign!!.setTextColor(ContextCompat.getColor(this, card.com.allcard.R.color.black))
                mk.encode(idNum + "n", 4)
            } else {
                val signNum = mk.decodeInt(idNum + "n", 4)
                if (signNum < 1) {
                    glv!!.clearView()
                    canBack = 1
                    tv_sign!!.text = "手势密码已失效，请重新登录"
                    showPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                } else {
                    tv_sign!!.text = "密码错误，还可以输入" + signNum + "次"
                    mk.encode(idNum + "n", signNum - 1)
                    tv_sign!!.setTextColor(ContextCompat.getColor(this, card.com.allcard.R.color.red))
                    shake!!.start()
                    glv!!.showErrorStatus(400)
                }

            }
            1 -> if (TextUtils.isEmpty(result) || result.length < 4) {
                shake!!.start()
                tv_sign!!.text = "至少连接四个点,请重新输入"
                glv!!.showErrorStatus(400)
                tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.red))
            } else {
                state = 2
                tv_sign!!.text = "再次绘制解锁图案"
                tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.black))
                s_glt!!.setThumbnailView(result, -0xe17422)
                mkBD!!.encode(userId+"sign", result)
                glv!!.clearView()
            }
            2 -> {
                val sign = mkBD!!.decodeString(userId+"sign", "")
                if (sign == result) {
                    finish()
                } else {
                    shake!!.start()
                    right_menu!!.visibility = View.VISIBLE
                    tv_sign!!.text = "与上次绘制不一致,请重新绘制"
                    glv!!.showErrorStatus(400)
                    tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.red))
                }
            }
        }
    }

    private var showPop: PopupWindow? = null

    private fun showPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.to_login_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        sure.text = "重新登录"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "手势密码已失效,请重新登录"
        showPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        showPop!!.isTouchable = true
        showPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        showPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            showPop!!.dismiss()
            mk.clearAll()
            mkBD!!.encode(userId+"finger","")
            mkBD!!.encode(userId+"sign","")
            JPushInterface.deleteAlias(this@SignLockChange,0)
            JPushInterface.clearAllNotifications(this@SignLockChange)
            MyApplication.instance.removeAllActivity()
            utils.startActivity(LoginActivity::class.java)
            finish()
        }
    }

    private fun showLoginPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.login_dialog, null)
        sure = view.findViewById(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        val photoNum = view.findViewById<TextView>(R.id.photo_num)
        var phone = mk.decodeString(Tool.PHONE, "")
        phone = phone!!.substring(0, 3) + "****" + phone.substring(7, 11)
        photoNum.text = phone
        val etNum = view.findViewById<EditText>(R.id.et_num)
        loginPopup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        loginPopup!!.isTouchable = true
        loginPopup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        loginPopup!!.setBackgroundDrawable(dw)
        cancel.text = "取消"
        cancel!!.setTextColor(ContextCompat.getColor(this@SignLockChange, R.color.text_gray))
        cancel.setOnClickListener { v ->
            etNum.setText("")
            loginPopup!!.dismiss()
        }
        sure!!.setOnClickListener { v ->
            val password = etNum.text.toString().trim()
            val encrypt = MD5Utils.encrypt(password)
            val localPassword = mk.decodeString(Tool.PASSWORD, "")
            if (encrypt == localPassword) {
                loginPopup!!.dismiss()
                address!!.text = "设置手势密码"
                val linearParams = s_glt.layoutParams as RelativeLayout.LayoutParams //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
                linearParams.height = 220// 控件的宽强制设成60
                s_glt.layoutParams = linearParams //使设置好的布局参数应用到控件
                s_glt!!.visibility = View.VISIBLE
                state = 1
                canBack = 0
                tv_sign!!.text = "绘制解锁图案"
                sign_login!!.visibility = View.GONE
                glv!!.clearView()
                tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.black))
                val idNum = mk.decodeString(Tool.USER_ID, "")
                mk.encode(idNum + "n", 4)
            } else if(TextUtils.isEmpty(password)){
                utils.showToast("请输入登录密码")
            }else{
                utils.showToast("密码输入错误,请重新输入")
            }
        }
    }

    override fun onBackPressed() {
        if (canBack == 1) {
            showPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
        } else {
            super.onBackPressed()//注释掉这行,back键不退出activity
        }
    }
}
