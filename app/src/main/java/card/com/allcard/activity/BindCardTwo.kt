package card.com.allcard.activity

import android.text.TextUtils
import card.com.allcard.R
import card.com.allcard.bean.GetNum
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.RegexUtils
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_bind_card_two.*
import kotlinx.android.synthetic.main.title.*

class BindCardTwo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_bind_card_two

    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address.text = "非记名市民卡绑定"
        close.setOnClickListener { finish() }
        img_ok.setOnClickListener {
            val name = et_name.text.toString().trim()
            val num = et_num.text.toString().trim()
            val card = et_card.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                utils.showToast("请输入办卡姓名")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(num)) {
                utils.showToast("请输入办卡手机号")
                return@setOnClickListener
            }
            if (RegexUtils.verifyUsername(num) != RegexUtils.VERIFY_SUCCESS) {
                utils.showToast("请输入正确的办卡手机号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(card)) {
                utils.showToast("请输入卡号")
                return@setOnClickListener
            }
            bindSbk(name,num,card)
        }
    }

    private fun bindSbk(name: String, num: String, card: String) {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.cardBind(userId, name, num, card, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if(bean.result == "0"){
                    utils.showToast("绑定成功")
                    finish()
                }else{
                    utils.showToast(bean.message)
                }
            }

            override fun failed(desc: String) {
                super.failed(desc)
                utils.showToast("网络异常,绑定失败")
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }
}
