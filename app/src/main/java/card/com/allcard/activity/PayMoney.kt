package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import card.com.allcard.R
import card.com.allcard.bean.OtherMoneyBean
import card.com.allcard.bean.YjBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_pay_money.*

class PayMoney : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_pay_money
    var m = 0
    var cardNo = ""
    var name1 = ""
    override fun initView() {
        utils.changeStatusBlack(false, window)
        address.text = "一卡通余额"
        close.setOnClickListener { finish() }
        cardNo = intent.getStringExtra("cardNo")
        name1 = intent.getStringExtra("name")
        val num1 = intent.getStringExtra("num")
        forWho.text = "为 $name1 一键充值"
        name.text = name1
        num.text = num1
        if (TextUtils.isEmpty(cardNo)) {
            initJt()
        } else {
            initFjm()
        }
        rl_cz.setOnClickListener {
            if (TextUtils.isEmpty(cardNo)) {
                val bundle = Bundle()
                bundle.putString("cardNo", "")
                bundle.putString("nickName", name1)
                bundle.putString("flag", "2")
                utils.startActivityBy(MoneyIn::class.java, bundle)
            } else {
                val bundle = Bundle()
                bundle.putString("cardNo", cardNo)
                bundle.putString("nickName", name1)
                bundle.putString("flag", "1")
                utils.startActivityBy(MoneyIn::class.java, bundle)
            }
        }
        rl_mx.setOnClickListener {
            if (TextUtils.isEmpty(cardNo)) {
                val bundle = Bundle()
                bundle.putString("cardNo", "")
                bundle.putString("nickName", name1)
                bundle.putString("is_other", "1")
                utils.startActivityBy(MoneyInfo::class.java, bundle)
            } else {
                val bundle = Bundle()
                bundle.putString("cardNo", cardNo)
                bundle.putString("nickName", name1)
                bundle.putString("is_other", "2")
                utils.startActivityBy(MoneyInfo::class.java, bundle)
            }
        }
        rl_kyj.setOnClickListener {
            if (TextUtils.isEmpty(cardNo)) {
                val bundle = Bundle()
                bundle.putString("cardNo", "")
                bundle.putString("nickName",name1)
                bundle.putString("flag", "2")
                utils.startActivityBy(MoneyOfCash::class.java, bundle)
            } else {
                val bundle = Bundle()
                bundle.putString("cardNo",cardNo)
                bundle.putString("nickName","")
                bundle.putString("flag", "1")
                utils.startActivityBy(MoneyOfCash::class.java, bundle)
            }
        }
    }

    private fun initJt() {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.searchYuEOther(userId, name1, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<OtherMoneyBean>() {})
                if (bean.result == "0") {
                    money.text = bean.amt.toDouble().toString()
                    yj_money.text = "${bean.yj.toDouble()}元"
                } else {
                    utils.showToast("修改失败，请重试")
                }
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    private fun initFjm() {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.searchYuE(userId, cardNo, "1", object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<YjBean>() {})
                if (bean.result == "0") {
                    money.text = bean.ye_amt.toDouble().toString()
                    yj_money.text = "${bean.yj_amt.toDouble()}元"
                } else {
                    utils.showToast("修改失败，请重试")
                }
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }
    // wait_pay.setOnClickListener { startActivity<MoneyInfo>() }
//     xz.setOnClickListener { startActivity<EdOfMoney>() }


}
