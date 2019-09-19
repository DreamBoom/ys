package card.com.allcard.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.adapter.CardAdapter
import kotlinx.android.synthetic.main.activity_card_info.*
import kotlinx.android.synthetic.main.title.*

class CardInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_info
    private var serviceGuide = mutableListOf<String>()
    private var adapt: CardAdapter? = null
    private var exitPop: PopupWindow? = null
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        list.setFlatFlow(true)//平面滚动
        adapt = CardAdapter(this,R.layout.card_item,serviceGuide)
        serviceGuide.add("张三")
        serviceGuide.add("李素")
        serviceGuide.add("王五")
        serviceGuide.add("发送到")
        serviceGuide.add("高德")
        address.text = "社保卡服务   1/"+ serviceGuide.size
        close.setOnClickListener { finish() }
        list.adapter = adapt
        list.setOnItemSelectedListener {
            position -> address.text = "社保卡服务   "+(position + 1).toString() + "/" + list.layoutManager!!.itemCount
            name.text = serviceGuide[position]
        }
        exitPop()
        gs.setOnClickListener { exitPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0) }
    }

    //退出弹窗
    private fun exitPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "是否对该卡进行卡挂失?"
        exitPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        exitPop!!.isTouchable = true
        exitPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        exitPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            exitPop!!.dismiss()
        }
        cancel.setOnClickListener {
            exitPop!!.dismiss()
        }
    }

}
