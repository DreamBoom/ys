package card.com.allcard.activity

import android.annotation.SuppressLint
import card.com.allcard.R
import card.com.allcard.bean.EduBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_ed_of_money.*
import kotlinx.android.synthetic.main.title.*

class EdOfMoney : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_ed_of_money

    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        address.text = "资金额度设置"
        bt_change.isEnabled = false
        bt_change.setOnClickListener { }
        initData()
        et1.isFocusable = false
        et1.isFocusableInTouchMode = false
        et2.isFocusable = false
        et2.isFocusableInTouchMode = false
        et1.setText("100".toCharArray(), 0, "100".length)
        et2.setText("100".toCharArray(), 0, "100".length)
        ed1.setOnClickListener {
            et1.isFocusableInTouchMode = true
            et1.isFocusable = true
            et1.requestFocus()
        }
        ed2.setOnClickListener {
            et2.isFocusableInTouchMode = true
            et2.isFocusable = true
            et2.requestFocus()
        }
    }

    private fun initData() {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.quotaSelect(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<EduBean>() {})
                val status = bean.status
                if (status == "0") {
                    et1.setText(bean.single_consumption_amount.toCharArray(), 0, bean.single_consumption_amount.length)
                    et2.setText(bean.account_balance_ceiling.toCharArray(), 0, bean.account_balance_ceiling.length)
                }
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }
}
