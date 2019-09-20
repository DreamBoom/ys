package card.com.allcard.activity

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.bean.CardProBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_card_progress.*
import kotlinx.android.synthetic.main.title.*

class CardProgress : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_progress

    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "制卡进度"
        close.setOnClickListener {
            finish()
        }
        val name1 = intent.getStringExtra("name")
        val num1 = intent.getStringExtra("num")
        utils.getProgress(this)
        HttpRequestPort.instance.CardProgress(name1, num1, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<CardProBean>() {})
                if ("0" == bean.result) {
                    name.text = "姓名: " + bean.name
                    num.text = "身份证号: " + bean.cert_no
                    s_num.text = "社保卡号: " + bean.aaz
                    card_type.text = "卡片类型: " + bean.card_type
                    city.text = "所属城市: " + bean.city
                    bank.text = "所属银行: " + bean.bank
                    if (!TextUtils.isEmpty(bean.applyTime)) {
                        r1.visibility = View.VISIBLE
                        t1.text = bean.applyTime
                    }
                    if (!TextUtils.isEmpty(bean.bankTime)) {
                        r2.visibility = View.VISIBLE
                        t2.text = bean.applyTime
                    }
                    if (!TextUtils.isEmpty(bean.zkTime)) {
                        r3.visibility = View.VISIBLE
                        t3.text = bean.applyTime
                    }
                    if (!TextUtils.isEmpty(bean.zkwcTime)) {
                        r4.visibility = View.VISIBLE
                        t4.text = bean.applyTime
                    }
                    if (!TextUtils.isEmpty(bean.cityTime)) {
                        r5.visibility = View.VISIBLE
                        t5.text = bean.applyTime
                    }
                    if (!TextUtils.isEmpty(bean.getTime)) {
                        r6.visibility = View.VISIBLE
                        t6.text = bean.applyTime
                    }
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("请求失败，请稍后重试")
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }
}
