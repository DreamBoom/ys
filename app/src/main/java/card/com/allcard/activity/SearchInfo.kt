package card.com.allcard.activity

import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import kotlinx.android.synthetic.main.activity_search_info.*
import kotlinx.android.synthetic.main.activity_search_info.bar
import kotlinx.android.synthetic.main.activity_search_result.*

class SearchInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_search_info
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        val type = intent.getIntExtra("type",1)
        val money1 = intent.getStringExtra("money")
        val yue1 = intent.getStringExtra("yue")
        val time1 = intent.getStringExtra("time")
        val id1 = intent.getStringExtra("id")
        val cardId1 = intent.getStringExtra("cardId")
        val deviceId1 = intent.getStringExtra("deviceId")
        close.setOnClickListener { finish() }
        address.text = "交易详情"
        if(type == 1){
            t1.text = "账户充值(元)"
        }else{
            t1.text = "账户消费(元)"
        }
        money.text = money1
        yue.text = yue1
        time.text = time1
        id.text = id1
        cardId.text = cardId1
        deviceId.text = deviceId1
    }
}