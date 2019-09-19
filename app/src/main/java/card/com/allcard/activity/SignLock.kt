package card.com.allcard.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import card.com.allcard.view.AliPayPainter
import com.wangnan.library.listener.OnGestureLockListener
import kotlinx.android.synthetic.main.activity_sign_lock.*
import kotlinx.android.synthetic.main.title.*

class SignLock : BaseActivity(), OnGestureLockListener {
    override fun layoutId(): Int = R.layout.activity_sign_lock
    internal var state = 0
    private var shake: ObjectAnimator? = null
    val mkBD = SplashActivity.mkBD
    val userId = mk.decodeString(Tool.USER_ID, "")
    var num = ""
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "设置手势密码"
        shake = utils.shake(tv_sign)
        glv!!.setPainter(AliPayPainter())
        glv!!.setGestureLockListener(this)
        close.setOnClickListener { finish() }
        right_menu.setOnClickListener {
            state = 0
            right_menu!!.visibility = View.GONE
            tv_sign!!.text = "绘制解锁图案"
            mkBD!!.encode(userId+"sign", "")
            tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.black))
            s_glt!!.setThumbnailView("012345678", ContextCompat.getColor(this, R.color.NormalColor))
            glv!!.clearView()
        }
    }

    override fun onStarted() {

    }

    override fun onProgress(progress: String) {

    }

    override fun onComplete(result: String) {
        when (state) {
            0 -> if (TextUtils.isEmpty(result) || result.length < 4) {
                shake!!.start()
                tv_sign!!.text = "至少连接四个点，请重新绘制"
                glv!!.showErrorStatus(400)
                tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.red))
            } else {
                state = 1
                tv_sign!!.text = "再次绘制解锁图案"
                tv_sign!!.setTextColor(ContextCompat.getColor(this, R.color.black))
                s_glt!!.setThumbnailView(result, -0xe17422)
                num = result
                glv!!.clearView()
            }

            1 -> {
                if (num == result) {
                    val intent = Intent()
                    intent.putExtra("signOn", true)
                    intent.putExtra("signNum", result)
                    setResult(Tool.RESULTCODE_SUCCESS, intent)
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
}
