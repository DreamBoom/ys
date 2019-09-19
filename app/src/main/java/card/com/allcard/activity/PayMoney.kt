package card.com.allcard.activity

import android.support.v4.content.ContextCompat
import card.com.allcard.R
import card.com.allcard.tools.Tool
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_pay_money.*
import kotlinx.android.synthetic.main.title.*

class PayMoney : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_pay_money
    var m = 0
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address.text = "一卡通余额"
        close.setOnClickListener { finish() }
        // wait_pay.setOnClickListener { startActivity<MoneyInfo>() }
        xz.setOnClickListener { startActivity<EdOfMoney>() }
        mx.setOnClickListener { startActivity<MoneyInfo>()  }
        kyj.setOnClickListener { startActivity<MoneyOfCash>() }

        moneyShow.setOnClickListener {
            if (m == 0) {
                m = 1
                moneyShow.setImageDrawable(ContextCompat.getDrawable(this@PayMoney,R.drawable.icon_eye_close))
                money.text = "****"
            } else {
                m = 0
                moneyShow.setImageDrawable(ContextCompat.getDrawable(this@PayMoney,R.drawable.icon_eye_open))
                val m = mk.decodeString(Tool.oneMoney, "0")
                money.text = utils.save2(m.toDouble())
            }
        }
    }
}
