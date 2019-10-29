package card.com.allcard.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import card.com.allcard.R
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MD5Utils
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.title.*
/**
 * @author Created by Dream
 * 修改登录密码
 */
class ChangePasswordActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_change_password
    val deviceId = utils.getDeviceId(this)

    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "修改登录密码"
        right_menu!!.visibility = View.GONE
        val ph = mk.decodeString(Tool.PHONE, "")
        val pass = mk.decodeString(Tool.PASSWORD, "")
        val phone1 = ph!!.substring(0, 3) + "****" + ph.substring(7, 11)
        et_phone!!.text = phone1
        //禁止输入空格
        et_password.addTextChangedListener(passWatcher(et_password))
        et_new.addTextChangedListener(passWatcher(et_new))
        et_new1.addTextChangedListener(passWatcher(et_new1))
        close.setOnClickListener { finish() }
        img_ok.setOnClickListener {
            val old = et_password!!.text.toString().trim()
            val new = et_new!!.text.toString().trim()
            val new1 = et_new1!!.text.toString().trim()
            when {
                TextUtils.isEmpty(old) -> utils.showToast("输入旧密码")
                MD5Utils.encrypt(old) != pass -> utils.showToast("旧密码输入错误")
                TextUtils.isEmpty(new) -> utils.showToast("输入新密码")
                (new.length < 6) -> utils.showToast("密码长度为6-12位")
                TextUtils.isEmpty(new1) -> utils.showToast("确认新密码")
                new1 != (new) -> utils.showToast("密码输入不一致")
                else -> changePassword()
            }
        }
    }

    private fun jPush(alias: String) {
        JPushInterface.setAlias(applicationContext, 0, alias)
    }
    private fun changePassword() {
        utils.getProgress(this)
        val ph = mk.decodeString(Tool.PHONE, "")
        val pass = et_password!!.text.toString().trim()
        val encrypt = MD5Utils.encrypt(pass)
        val new1 = et_new1!!.text.toString().trim()
        val encrypt1 = MD5Utils.encrypt(new1)
        HttpRequestPort.instance.upPasswd(ph, encrypt, encrypt1, deviceId,
                object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                        utils.showToast(bean.message)
                        if (bean.result == "0") {
                            mk.clearAll()
                            jPush("")
                            JPushInterface.clearAllNotifications(this@ChangePasswordActivity)
                            MyApplication.instance.removeAllActivity()
                            startActivity<LoginActivity>()
                            finish()
                        }
                    }

                    override fun onFinished() {
                        super.onFinished()
                        utils.hindProgress()
                    }
                })
    }

    internal inner class passWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString().contains(" ")) {
                val str = s.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var str1 = ""
                for (i in str.indices) {
                    str1 += str[i]
                }
                editText.setText(str1)
                editText.setSelection(start)
            }
        }

        override fun afterTextChanged(editable: Editable) {
        }
    }

}
