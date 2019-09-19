package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.tools.Tool.RESULTCODE_SIGN_OFF
import card.com.allcard.tools.Tool.RESULTCODE_SIGN_ON
import card.com.allcard.tools.Tool.RESULTCODE_SYS
import card.com.allcard.utils.MD5Utils
import cn.jpush.android.api.JPushInterface
import com.nestia.biometriclib.BiometricPromptManager
import com.nestia.biometriclib.OpenBiometricPromptDialog
import com.nestia.biometriclib.OpenBiometricPromptManager
import com.pawegio.kandroid.startActivity
import com.pawegio.kandroid.textWatcher
import kotlinx.android.synthetic.main.activity_sign_setting.*
import kotlinx.android.synthetic.main.title.*

class SignSetting : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_sign_setting
    private var mManager: BiometricPromptManager? = null
    private var mManager1: OpenBiometricPromptManager? = null
    private var signLock: String = ""
    private var finger: String = ""
    private var loginPopup: PopupWindow? = null
    private var sure: TextView? = null
    val deviceId = utils.getDeviceId(this)
    val userId = mk.decodeString(Tool.USER_ID, "")
    val mkBD = SplashActivity.mkBD
    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        mManager = BiometricPromptManager.from(this)
        mManager1 = OpenBiometricPromptManager.from(this)
        signLock = mkBD!!.decodeString(userId + "sign", "")
        finger = mkBD!!.decodeString(userId + "finger", "")
        if (signLock != "") {
            sign_word!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.but_open))
            sign_change!!.visibility = View.VISIBLE
        } else {
            sign_word!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.but_close))
            sign_change!!.visibility = View.GONE
        }
        if (finger == deviceId) {
            im_finger!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.but_open))
        } else {
            im_finger!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.but_close))
        }
    }

    override fun initView() {
        MyApplication.instance.addActivity(this)
        if(bar.height<=0){
            bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        }
        utils.changeStatusBlack(true, window)
        address!!.text = "指纹/手势解锁"
        toSysPop()
        showLoginPop()
        sysEroPop()
        closePop()
        im_finger.setOnClickListener {
            finger = mkBD.decodeString(userId + "finger", "")
            if (finger == "") {
                //指纹锁未开启
                    if (mManager1!!.isHardwareDetected) {
                        //系统硬件支持
                        if (signLock != "") {
                            //手势密码已开启
                            if (mManager1!!.hasEnrolledFingerprints()) {
                                utils.startActivityForResult(SignLockCheck::class.java, RESULTCODE_SYS)
                            } else {
                                toSysPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                            }
                        } else {
                            utils.showToast("请先开启手势密码")
                        }
                    } else {
                        sysEroPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                    }
            } else {
                if (mManager!!.hasEnrolledFingerprints()) {
                    mManager!!.authenticate(object : BiometricPromptManager.OnBiometricIdentifyCallback {
                        override fun onFailed() {

                        }

                        override fun onUsePassword() {
                            loginPopup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                        }

                        override fun onSucceeded() {
                            mkBD.encode(userId + "finger", "")
                            im_finger.setImageDrawable(ContextCompat.getDrawable(this@SignSetting, R.drawable.but_close))
                            finger = ""
                        }

                        override fun onError(code: Int, reason: String) {
                            utils.showToast("尝试次数过多，请稍后重试")
                        }
                    })
                } else {
                    loginPopup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                }
            }
        }

        sign_word.setOnClickListener {
            signLock = mkBD.decodeString(userId + "sign", "")
            if (signLock == "") {
                utils.startActivityForResult(SignLock::class.java, RESULTCODE_SIGN_ON)
            } else {
                if (finger == deviceId) {
                    closePop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                } else {
                    utils.startActivityForResult(SignLockCheck::class.java, RESULTCODE_SIGN_OFF)
                }
            }
        }
        close.setOnClickListener { finish() }
        sign_change.setOnClickListener { startActivity<SignLockChange>() }
        about_finger.setOnClickListener { utils.startWeb(HttpRequestPort.H5_BASE_URL + "/weixin/Fingerprintprotocol.jsp?fixparam=android") }
    }


    private var toSysPop: PopupWindow? = null
    private fun toSysPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "去开通"
        cancel.text = "已录入"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "请先到手机系统中添加指纹"
        toSysPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        toSysPop!!.isTouchable = true
        toSysPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        toSysPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            mk.encode(Tool.BY_LOGIN,"1")
            val intent = Intent(Settings.ACTION_SETTINGS)
            startActivity(intent)
            im_finger.setImageDrawable(ContextCompat.getDrawable(this@SignSetting, R.drawable.but_close))
        }
        cancel.setOnClickListener {
            toSysPop!!.dismiss()
        }
    }

    private var closePop: PopupWindow? = null
    private fun closePop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "确定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "关闭手势将同步关闭指纹"
        closePop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        closePop!!.isTouchable = true
        closePop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        closePop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            closePop!!.dismiss()
            utils.startActivityForResult(SignLockCheck::class.java, RESULTCODE_SIGN_OFF)

        }
        cancel.setOnClickListener {
            closePop!!.dismiss()
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
        etNum.textWatcher {
            afterTextChanged { s ->
                when {
                    s!!.length > 5 -> {
                        sure!!.setTextColor(ContextCompat.getColor(this@SignSetting, R.color.blue))
                        sure!!.isClickable = true
                    }
                    else -> {
                        sure!!.setTextColor(ContextCompat.getColor(this@SignSetting, R.color.text_gray))
                        sure!!.isClickable = false
                    }
                }
            }
        }
        cancel.setOnClickListener { v ->
            loginPopup!!.dismiss()
            mkBD.encode(userId + "finger", "")
            mkBD.encode(userId + "sign", "")
            mk.clearAll()
            JPushInterface.deleteAlias(this@SignSetting,0)
            JPushInterface.clearAllNotifications(this@SignSetting)
            MyApplication.instance.removeAllActivity()
            utils.startActivity(ForgetPassword::class.java)
            finish()
        }
        sure!!.setOnClickListener { v ->
            val password = etNum.text.toString().trim()
            val encrypt = MD5Utils.encrypt(password)
            val localPassword = mk.decodeString(Tool.PASSWORD, "")
            if (encrypt == localPassword) {
                loginPopup!!.dismiss()
                mkBD!!.encode(userId + "finger", "")
                im_finger.setImageDrawable(ContextCompat.getDrawable(this@SignSetting, R.drawable.but_close))
                finger = ""
            } else {
                utils.showToast("密码输入错误,请重新输入")
            }
        }
    }

    private var sysEroPop: PopupWindow? = null
    private fun sysEroPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "确定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "当前系统硬件暂不支持指纹功能"
        sysEroPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        sysEroPop!!.isTouchable = true
        sysEroPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        sysEroPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            sysEroPop!!.dismiss()
        }
        cancel.setOnClickListener {
            sysEroPop!!.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Tool.RESULTCODE_SUCCESS) {
            return
        }
        when (requestCode) {
            RESULTCODE_SYS -> {
                if (mManager1!!.hasEnrolledFingerprints()) {
                    mManager1!!.authenticate(object : OpenBiometricPromptManager.Callback {
                        override fun onFailed() {

                        }

                        override fun onSucceeded() {
                            im_finger.setImageDrawable(ContextCompat.getDrawable(this@SignSetting, R.drawable.but_open))
                            finger = deviceId
                            mkBD!!.encode(userId + "finger", deviceId)
                            mk.encode(Tool.BY_LOGIN, "0")
                        }

                        override fun onError(code: Int, reason: String) {
                            if (code != 5) {
                                utils.showToast("尝试次数过多，请稍后重试")
                            }
                        }
                    })
                } else {
                    toSysPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                }
            }
            RESULTCODE_SIGN_ON -> {
                val signOn = data!!.extras!!.getBoolean("signOn", false)
                val signNum = data.extras!!.getString("signNum", "")
                if (signOn) {
                    signLock = signNum
                    sign_word!!.setImageDrawable(ContextCompat.getDrawable(this@SignSetting, R.drawable.but_open))
                    sign_change!!.visibility = View.VISIBLE
                    mkBD!!.encode(userId + "sign", signNum)
                    mk.encode(Tool.BY_LOGIN, "0")
                } else {
                    sign_word!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.but_close))
                    sign_change!!.visibility = View.GONE
                }
            }
            RESULTCODE_SIGN_OFF -> {
                val signOff = data!!.extras!!.getBoolean("signOff", false)
                if (signOff) {
                    signLock = ""
                    sign_word!!.setImageDrawable(ContextCompat.getDrawable(this@SignSetting, R.drawable.but_close))
                    sign_change!!.visibility = View.GONE
                    mkBD!!.encode(userId + "finger", "")
                    mkBD.encode(userId + "sign", "")
                    //关闭指纹
                    im_finger.setImageDrawable(ContextCompat.getDrawable(this@SignSetting, R.drawable.but_close))
                    finger = ""
                } else {
                    sign_word!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.but_open))
                    sign_change!!.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        OpenBiometricPromptDialog().Dismiss()
    }
}

