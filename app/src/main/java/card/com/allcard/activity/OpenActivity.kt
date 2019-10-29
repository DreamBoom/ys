package card.com.allcard.activity

import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.activity.SplashActivity.Companion.mkBD
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MD5Utils
import cn.jpush.android.api.JPushInterface
import com.nestia.biometriclib.OpenBiometricPromptManager
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_open.*
import org.xutils.image.ImageOptions
import org.xutils.x

class OpenActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_open
    private var mManager: OpenBiometricPromptManager? = null
    private var loginPopup: PopupWindow? = null
    private var sure: TextView? = null
    val deviceId = utils.getDeviceId(this)
    val userId = mk.decodeString(Tool.USER_ID, "")
    override fun initView() {
        MyApplication.instance.addActivity(this)
   //     bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        mManager = OpenBiometricPromptManager.from(this)
        Handler().postDelayed({
            if (mManager!!.hasEnrolledFingerprints()) {
                mManager!!.authenticate(initCallBack())
            }else{
                utils.showToast("系统指纹已关闭,请使用其他方式验证")
                mkBD!!.encode(userId+"finger","")
                popup!!.showAtLocation(im_icon, Gravity.NO_GRAVITY, 0, 0)
            }
        }, 300)    //延时执行
        val header = mk.decodeString(Tool.HEADER,"")
        val options =  ImageOptions.Builder()
                //设置图片的大小
                .setSize(300, 300)
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true)
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //设置加载过程中的图片
                .setLoadingDrawableId(R.drawable.img_user)
                //设置加载失败后的图片
                .setFailureDrawableId(R.drawable.img_user)
                //设置使用缓存
                .setUseMemCache(true)
                //设置支持gif
                .setIgnoreGif(false)
                //设置显示圆形图片
                .setCircular(true).build()
        if(!TextUtils.isEmpty(header)){
            x.image().bind(im_icon, header,options)
        }else{
            x.image().bind(im_icon, Tool.HEADER_IMG,options)
        }
        im_finger.setOnClickListener {
                if (mManager!!.hasEnrolledFingerprints()) {
                    mManager!!.authenticate(initCallBack())
                } else {
                    utils.showToast("尝试次数过多，请稍后重试")
                }
        }

        change_user.setOnClickListener {
            mk.clearAll()
            jPush("")
            JPushInterface.clearAllNotifications(this@OpenActivity)
            MyApplication.instance.removeAllActivity()
            utils.startActivity(LoginActivity::class.java)
            finish()
        }

        change_type.setOnClickListener { popup!!.showAtLocation(im_icon, Gravity.NO_GRAVITY, 0, 0) }
        showLoginPop()
        bindPopup()
    }
    private fun jPush(alias: String) {
        JPushInterface.setAlias(applicationContext, 0, alias)
    }
    private var popup: PopupWindow? = null

    private fun bindPopup() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "手势登录"
        cancel.text = "密码登录"
        cancel.setTextColor(ContextCompat.getColor(this,R.color.blue1))
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "换方式登录"
        popup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        popup!!.isTouchable = true
        popup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        popup!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            popup!!.dismiss()
            startActivity<OpenSign>()
            finish()
        }
        cancel.setOnClickListener {
            popup!!.dismiss()
            loginPopup!!.showAtLocation(im_icon, Gravity.NO_GRAVITY, 0, 0)
        }
    }


    private fun showLoginPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.login_dialog, null)
        sure = view.findViewById(R.id.sure)
        window.statusBarColor = resources.getColor(R.color.white)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        val photoNum = view.findViewById<TextView>(R.id.photo_num)
        var phone = mk.decodeString(Tool.PHONE,"")
        phone = phone!!.substring(0, 3) + "****" + phone.substring(7, 11)
        photoNum.text = phone
        val etNum = view.findViewById<EditText>(R.id.et_num)
        loginPopup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        loginPopup!!.isTouchable = true
        loginPopup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        loginPopup!!.setBackgroundDrawable(dw)
        cancel.setOnClickListener { v ->
            loginPopup!!.dismiss()
            mkBD!!.encode(userId+"finger", "")
            mkBD!!.encode(userId+"sign", "")
            mk.clearAll()
          jPush("")
            JPushInterface.clearAllNotifications(this)
            MyApplication.instance.removeAllActivity()
            utils.startActivity(ForgetPassword::class.java)
            finish()
        }

        sure!!.setOnClickListener { v ->
            val password = etNum.text.toString().trim()
            val pass = MD5Utils.encrypt(password)
            val localPassword = mk.decodeString(Tool.PASSWORD,"")
            if (pass == localPassword) {
                loginPopup!!.dismiss()
                val open = mkBD!!.decodeInt("open", 0)
                if(open == 1){
                    mkBD!!.encode("open", 0)
                    startActivity<MainActivity>()
                    finish()
                }else{
                    finish()
                }
            } else {
                utils.showToast("密码输入错误,请重新输入")
            }
        }
    }

    //调用指纹验证弹窗
    private fun initCallBack(): OpenBiometricPromptManager.Callback {
        return object : OpenBiometricPromptManager.Callback {

            override fun onFailed() {

            }

            override fun onSucceeded() {
                runDelayed(100){
                    val open = mkBD!!.decodeInt("open", 0)
                    if(open == 1){
                        mkBD!!.encode("open", 0)
                        startActivity<MainActivity>()
                        finish()
                    }else{
                        finish()
                    }
                }
            }

            override fun onError(code: Int, reason: String) {
                if(code!= 5){
                    utils.showToast("尝试次数过多，请稍后重试")
                }
            }
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
    }
}
