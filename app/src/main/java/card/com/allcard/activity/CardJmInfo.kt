package card.com.allcard.activity

import android.annotation.SuppressLint
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.CardAdapter
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_card_jm_info.*

class CardJmInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_jm_info
    private var serviceGuide = mutableListOf<String>()
    private var adapt: CardAdapter? = null
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        list.setFlatFlow(true)//平面滚动
        adapt = CardAdapter(this,R.layout.card_item1,serviceGuide)
        serviceGuide.add("张三")
        serviceGuide.add("李素")
        serviceGuide.add("王五")
        serviceGuide.add("发送到")
        serviceGuide.add("高德")
        address.text = "非记名市民卡   1/"+ serviceGuide.size
        close.setOnClickListener { finish() }
        list.adapter = adapt
        list.setOnItemSelectedListener {
            position -> address.text = "非记名市民卡   "+(position + 1).toString() + "/" + list.layoutManager!!.itemCount
            name.text = serviceGuide[position]
            if(position>2){
                l1.visibility =View.VISIBLE
                gs.visibility =View.GONE
            }else{
                l1.visibility =View.GONE
                gs.visibility =View.VISIBLE
            }
        }
        mm.setOnClickListener {   startActivity<ChangeCardPass>() }
        right_menu.setOnClickListener { startActivity<BindCardTwo>() }
    }
}
