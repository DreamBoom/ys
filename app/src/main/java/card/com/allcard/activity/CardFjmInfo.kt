package card.com.allcard.activity

import android.annotation.SuppressLint
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.CardAdapter
import card.com.allcard.bean.JmBeam
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_card_fjm_info.*

class CardFjmInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_fjm_info
    private var serviceGuide = mutableListOf<JmBeam.CardsListBean>()
    private var adapt: CardAdapter? = null
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        list.setFlatFlow(true)//平面滚动
        adapt = CardAdapter(this,R.layout.card_item1,serviceGuide)
        address.text = "非记名市民卡   1/"+ serviceGuide.size
        close.setOnClickListener { finish() }
        list.adapter = adapt
        list.setOnItemSelectedListener {
            position -> address.text = "非记名市民卡   "+(position + 1).toString() + "/" + list.layoutManager!!.itemCount
            name.text = serviceGuide[position].trDate
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
