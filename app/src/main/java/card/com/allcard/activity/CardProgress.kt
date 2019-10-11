package card.com.allcard.activity

import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.bean.CardProBean
import card.com.allcard.getActivity.MyApplication
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
        val bean = intent.getSerializableExtra("bean") as CardProBean
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
                        t2.text = bean.bankTime
                    }
                    if (!TextUtils.isEmpty(bean.zkTime)) {
                        r3.visibility = View.VISIBLE
                        t3.text = bean.zkTime
                    }
                    if (!TextUtils.isEmpty(bean.zkwcTime)) {
                        r4.visibility = View.VISIBLE
                        t4.text = bean.zkwcTime
                    }
                    if (!TextUtils.isEmpty(bean.cityTime)) {
                        r5.visibility = View.VISIBLE
                        t5.text = bean.cityTime
                        ads.text = "您的卡已到达[${bean.city}]卡管理中心！"
                    }
                    if (!TextUtils.isEmpty(bean.getTime)) {
                        r6.visibility = View.VISIBLE
                        t6.text = bean.getTime
                    }
                }

    }
}
