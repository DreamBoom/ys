package card.com.allcard.activity

import card.com.allcard.R
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_card_one.*

class CardOne : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_one

    override fun initView() {
        utils.changeStatusBlack(false, window)
        close.setOnClickListener { finish() }
        jm.setOnClickListener { startActivity<CardInfo>() }
        fjm.setOnClickListener { startActivity<CardJmInfo>() }
    }
}
