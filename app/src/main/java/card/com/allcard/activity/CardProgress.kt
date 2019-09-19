package card.com.allcard.activity

import card.com.allcard.R
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
    }
}
