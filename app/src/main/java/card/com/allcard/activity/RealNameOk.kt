package card.com.allcard.activity

import card.com.allcard.R
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_real_name_ok.*

class RealNameOk : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_real_name_ok

    override fun initView() {
        bt_real_ok.setOnClickListener {
            startActivity<RealName>()
            finish()
        }
    }
}
