package card.com.allcard.activity

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import card.com.allcard.R
import card.com.allcard.adapter.CashAdapter
import card.com.allcard.bean.YjListBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_money_of_cash.*
import kotlinx.android.synthetic.main.title.*
import java.util.*

class MoneyOfCash : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_money_of_cash
    private val dataList = ArrayList<YjListBean.CardsListBean>()
    var adapt: CashAdapter? = null
    var cardNo = ""
    var nickName = ""
    var flag = ""
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address.text = "卡押金"
        cardNo = intent.getStringExtra("cardNo")
        nickName = intent.getStringExtra("nickName")
        flag = intent.getStringExtra("flag")
        close.setOnClickListener { finish() }
        refresh.setEnableOverScrollDrag(false)
        refresh.setOnRefreshListener {
            refresh.finishRefresh()
            getList()
        }
        adapt = CashAdapter(this@MoneyOfCash, R.layout.yj_item, dataList)
        listView.layoutManager = LinearLayoutManager(this@MoneyOfCash)
        //动画效果
        adapt!!.openLoadAnimation(BaseQuickAdapter.EMPTY_VIEW)
        listView.adapter = adapt
        refresh.autoRefresh()
    }

    private fun getList() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.cardDeposit(userId, cardNo, flag, nickName, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<YjListBean>() {})
                val status = bean.result
                dataList.clear()
                if (status == "0") {
                    setData(bean.cardsList)
                } else {
                    setData(null)
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                setData(null)
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    private fun setData(data: List<YjListBean.CardsListBean>?) {
        if (adapt!!.headerLayout != null) {
            adapt!!.removeAllHeaderView()
        }
        adapt!!.setNewData(data)
        when {
            data == null -> adapt!!.addHeaderView(utils.getView(this, R.layout.view_no_web))
            data.size < 1 -> adapt!!.addHeaderView(utils.getView(this, R.layout.no_data))
        }
    }
}
