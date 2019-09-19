package card.com.allcard.activity

import android.text.TextUtils
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_card_pro.*
import kotlinx.android.synthetic.main.title.*

class CardPro : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_pro

    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "制卡进度"
        close.setOnClickListener { finish() }
        img_ok.setOnClickListener {
            val name = et_name!!.text.toString().trim()
            val num = et_num!!.text.toString().trim()
            when {
                TextUtils.isEmpty(name) -> utils.showToast("请输入真实姓名")
                TextUtils.isEmpty(num) -> utils.showToast("请输入身份证号")
                else -> search()
            }
        }
    }

    private fun search() {
        startActivity<CardProgress>()
    }
}
