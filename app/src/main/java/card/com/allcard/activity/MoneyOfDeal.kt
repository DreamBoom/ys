package card.com.allcard.activity

import card.com.allcard.R
import kotlinx.android.synthetic.main.activity_money_of_deal.*

class MoneyOfDeal : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_money_of_deal
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        close.setOnClickListener { finish() }
    }
}
