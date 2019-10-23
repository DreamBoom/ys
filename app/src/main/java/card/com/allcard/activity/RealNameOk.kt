package card.com.allcard.activity

import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_real_name_ok.*

class RealNameOk : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_real_name_ok

    override fun initView() {
        MyApplication.instance.addActivity(this)
        bt_real_ok.setOnClickListener {
            startActivity<RealName>()
            finish()
        }
    }
}
