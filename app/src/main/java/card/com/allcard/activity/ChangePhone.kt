package card.com.allcard.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_change_phone.*
import kotlinx.android.synthetic.main.title.*

class ChangePhone : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_change_phone
    override fun initView() {
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        MyApplication.instance.addActivity(this)
        address.text = "修改手机号"
        bindPopup()
        bindQ()
        close.setOnClickListener { finish() }
        changeInfo.setOnClickListener {
            when {
                tip.visibility == View.GONE -> tip.visibility = View.VISIBLE
                else -> tip.visibility = View.GONE
            }
        }
        fra.setOnClickListener {
            if (tip.visibility == View.VISIBLE) {
                tip.visibility = View.GONE
            }
        }

        title_click.setOnClickListener {
            if (tip.visibility == View.VISIBLE) {
                tip.visibility = View.GONE
            }
        }
        byPhone.setOnClickListener {
            if (tip.visibility == View.VISIBLE) {
                tip.visibility = View.GONE
            } else {
                startActivity<ByPhone>()
            }
        }
        bySms.setOnClickListener {
            if (tip.visibility == View.VISIBLE) {
                tip.visibility = View.GONE
            } else {
                val isAuth = mk.decodeString(Tool.IS_AUTH, "")
                val email = mk.decodeString(Tool.EMAIL, "")
                if ("0" == isAuth) {
                    if (TextUtils.isEmpty(email)) {
                        bindPopup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                    } else {
                        startActivity(Intent(this, BySms::class.java).putExtra("type", "4"))
                    }
                } else {
                    utils.showToast("请在个人中心先进行实名认证")
                }
            }
        }
        byQuestion.setOnClickListener {
            val isAuth = mk.decodeString(Tool.IS_AUTH, "")
            when (isAuth) {
                "0" -> {
                    val q = mk.decodeString(Tool.BINDQ, "")
                    if (TextUtils.isEmpty(q)) {
                        bindQ!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                    } else {
                        startActivity<ByQuestion>()
                    }
                }
                else -> utils.showToast("请在个人中心先进行实名认证")
            }
        }
    }

    private var bindPopup: PopupWindow? = null

    private fun bindPopup() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "去绑定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "您当前未绑定邮箱，是否前往绑定?"
        bindPopup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        bindPopup!!.isTouchable = true
        bindPopup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        bindPopup!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            startActivity(Intent(this, SetSms::class.java).putExtra("type", "0"))
            bindPopup!!.dismiss()
        }
        cancel.setOnClickListener {
            bindPopup!!.dismiss()
        }
    }

    private var bindQ: PopupWindow? = null
    private fun bindQ() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "去绑定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "您当前未绑定密保问题，是否前往绑定?"
        bindQ = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        bindQ!!.isTouchable = true
        bindQ!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        bindQ!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            startActivity(Intent(this, SetQuestion::class.java).putExtra("type", "0"))
            bindQ!!.dismiss()
        }
        cancel.setOnClickListener {
            bindQ!!.dismiss()
        }
    }
}
