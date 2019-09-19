package card.com.allcard.activity

import android.annotation.SuppressLint
import card.com.allcard.R
import card.com.allcard.adapter.YjAdapter
import card.com.allcard.bean.YjListBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_money_of_cash.*
import kotlinx.android.synthetic.main.title.*
import java.util.*

class MoneyOfCash : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_money_of_cash
    private val dataList = ArrayList<YjListBean.CardsListBean>()
    var adapt: YjAdapter? = null
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address.text = "卡押金"
        close.setOnClickListener { finish() }
        refresh.setEnableOverScrollDrag(false)
        refresh.setOnRefreshListener {
            refresh.finishRefresh()
            getList()
        }
        adapt = YjAdapter(this@MoneyOfCash, dataList, R.layout.yj_item)
        listView.adapter = adapt
        refresh.autoRefresh()
    }

    private fun getList() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.cardDeposit(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<YjListBean>() {})
                val status = bean.result
                if (status == "0") {
                    dataList.addAll(bean.cardsList)
                }
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
        adapt!!.notifyDataSetChanged()
    }
}
