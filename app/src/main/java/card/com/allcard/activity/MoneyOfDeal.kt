package card.com.allcard.activity

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import card.com.allcard.R
import kotlinx.android.synthetic.main.activity_money_of_deal.*

class MoneyOfDeal : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_money_of_deal
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        val type = intent.getStringExtra("type")
        val code = intent.getStringExtra("img")
        val t1 = intent.getStringExtra("tv1")
        val t2 = intent.getStringExtra("tv2")
        val t4 = intent.getStringExtra("tv4")
        val t5 = intent.getStringExtra("tv5")
        val t7 = intent.getStringExtra("tv7")
        val t8 = intent.getStringExtra("tv8")
        tv1.text = t1
        tv4.text = t4
        tv5.text = t5
        tv7.text = t7
        tv8.text = t8
        tv10.text = t1
        when (code) {
            "2830" -> {
                im.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.img_yemx_xf))
                tv3.text = "消费成功"
                tv2.text = "- $t2"
            }
            "2834" -> {
                im.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.img_yemx_zhtx))
                tv3.text = "退款成功"
                tv2.text = "+ $t2"
            }
            "2835",
            "1610" -> {
                im.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.img_yemx_tx))
                tv3.text = "提现成功"
                tv2.text = "- $t2"
            }
            else -> {
                im.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.img_yemx_cz))
                tv3.text = "充值成功"
                tv2.text = "+ $t2"
            }
        }
        when(type){
            "1" ->  tv9.text = "柜面"
            "3" ->  tv9.text = "自助机"
            "5" ->  tv9.text = "APP"
        }
    }
}
