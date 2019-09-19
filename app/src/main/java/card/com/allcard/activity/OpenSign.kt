package card.com.allcard.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
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
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MD5Utils
import card.com.allcard.view.AliPayPainter
import cn.jpush.android.api.JPushInterface
import com.pawegio.kandroid.startActivity
import com.wangnan.library.listener.OnGestureLockListener
import kotlinx.android.synthetic.main.activity_open_sign.*
import org.xutils.image.ImageOptions
import org.xutils.x

class OpenSign : BaseActivity(), OnGestureLockListener {
    override fun layoutId() = R.layout.activity_open_sign
    private var shake: ObjectAnimator? = null
    private var sign: String? = null
    private var sure: TextView? = null
    val deviceId = utils.getDeviceId(this)
    val mkBD = SplashActivity.mkBD
    val userId = mk.decodeString(Tool.USER_ID, "")

    override fun initView() {
        MyApplication.instance.addActivity(this)
        //   bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //注意要清除 FLAG_TRANSLUCENT_STATUS flaggetWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.statusBarColor = resources.getColor(R.color.white)
        utils.changeStatusBlack(true, window)
        shake = utils.shake(tv_sign)
        val header = mk.decodeString(Tool.HEADER, "")
        val options = ImageOptions.Builder()
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
        if (!TextUtils.isEmpty(header)) {
            x.image().bind(im_icon, header, options)
        } else {
            x.image().bind(im_icon, Tool.HEADER_IMG, options)
        }
        glv!!.setPainter(AliPayPainter())
        glv!!.setGestureLockListener(this)
        change_user.setOnClickListener {
            mk.clearAll()
            JPushInterface.deleteAlias(this@OpenSign,0)
            JPushInterface.clearAllNotifications(this@OpenSign)
            MyApplication.instance.removeAllActivity()
            utils.startActivity(LoginActivity::class.java)
            finish()
        }
        change_type.setOnClickListener {
            changePop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
        }
        showLoginPop()
        showPop()
        changePop()
    }

    override fun onStarted() {

    }

    override fun onProgress(progress: String) {

    }

    @SuppressLint("SetTextI18n")
    override fun onComplete(result: String) {
        sign = mkBD!!.decodeString(userId + "sign", "")
        if (sign == result) {
            glv!!.clearView()
            mk.encode(userId + "n", 4)
            val open = SplashActivity.mkBD!!.decodeInt("open", 0)
            if (open == 1) {
                SplashActivity.mkBD!!.encode("open", 0)
                startActivity<MainActivity>()
                finish()
            } else {
                finish()
            }
        } else {
            val signNum = mk.decodeInt(userId + "n", 4)
            if (signNum < 1) {
                glv!!.clearView()
                tv_sign!!.text = "手势密码已失效，请重新登录"
                mk.clearAll()
                mkBD.encode(userId + "finger", "")
                mkBD.encode(userId + "sign", "")
                JPushInterface.deleteAlias(this@OpenSign,0)
                JPushInterface.clearAllNotifications(this@OpenSign)
                popup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
            } else {
                tv_sign!!.text = "密码错误，还可以输入" + signNum + "次"
                mk.encode(userId + "n", signNum - 1)
                tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.red))
                shake!!.start()
                glv!!.showErrorStatus(400)
            }
        }
    }

    private var changePop: PopupWindow? = null

    private fun changePop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "指纹登录"
        cancel.text = "密码登录"
        cancel.setTextColor(ContextCompat.getColor(this, R.color.blue1))
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "换方式登录"
        changePop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        changePop!!.isTouchable = true
        changePop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        changePop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            val finger = mkBD!!.decodeString(userId + "finger", "")
            if (finger == deviceId) {
                changePop!!.dismiss()
                startActivity<OpenActivity>()
                finish()
            } else {
                utils.showToast("未开启指纹解锁，登录设置后尝试")
                changePop!!.dismiss()
            }
        }
        cancel.setOnClickListener {
            changePop!!.dismiss()
            loginPopup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
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
            mkBD!!.encode(userId + "finger", "")
            mkBD!!.encode(userId + "sign", "")
            JPushInterface.deleteAlias(this@OpenSign,0)
            JPushInterface.clearAllNotifications(this@OpenSign)
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
        cancel.text = "忘记密码"
        val photoNum = view.findViewById<TextView>(R.id.photo_num)
        val ph = mk.decodeString(Tool.PHONE, "")
        val phone = ph!!.substring(0, 3) + "****" + ph.substring(7, 11)
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
            mkBD!!.encode(userId + "finger", "")
            mkBD!!.encode(userId + "sign", "")
            mk.clearAll()
            JPushInterface.deleteAlias(this@OpenSign,0)
            JPushInterface.clearAllNotifications(this@OpenSign)
            MyApplication.instance.removeAllActivity()
            utils.startActivity(ForgetPassword::class.java)
            finish()
        }
        sure!!.setOnClickListener { v ->
            val idNum = mk.decodeString(Tool.USER_ID, "")
            val password = etNum.text.toString().trim()
            val encrypt = MD5Utils.encrypt(password)
            val localPassword = mk.decodeString(Tool.PASSWORD, "")
            if (encrypt == localPassword) {
                loginPopup!!.dismiss()
                mk.encode(idNum + "n", 4)
//                val intent = Intent()
//                intent.putExtra("signOff", true)
//                setResult(Tool.RESULTCODE_SUCCESS, intent)
                val open = mkBD!!.decodeInt("open", 0)
                if (open == 1) {
                    mkBD!!.encode("open", 0)
                    startActivity<MainActivity>()
                    finish()
                } else {
                    finish()
                }
            } else if (TextUtils.isEmpty(password)) {
                utils.showToast("请输入登录密码")
            } else {
                utils.showToast("密码输入错误,请重新输入")
            }
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
    }
}