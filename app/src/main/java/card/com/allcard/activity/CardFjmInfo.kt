package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.CardFjmAdapter
import card.com.allcard.bean.FjmBean
import card.com.allcard.bean.GetNum
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.LogUtils
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_card_fjm_info.*

class CardFjmInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_fjm_info
    private var serviceGuide = mutableListOf<FjmBean.CardListBean>()
    private var adapt: CardFjmAdapter? = null
    var certNo = ""
    var name1 = ""
    var cardNo = ""
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        list.setFlatFlow(true)//平面滚动
        adapt = CardFjmAdapter(this, R.layout.card_item1, serviceGuide)
        address.text = "非记名市民卡   0/" + serviceGuide.size
        close.setOnClickListener { finish() }
        list.adapter = adapt
        list.setOnItemSelectedListener { position ->
            address.text = "非记名市民卡   " + (position + 1).toString() + "/" + list.layoutManager!!.itemCount
            time.text = serviceGuide[position].createTime
            name1 = serviceGuide[position].clientName
            name.text = name1
            cardNo = serviceGuide[position].cardNo
            val s = certNo.substring(0, 3) + "****" + certNo.substring(certNo.length - 4, certNo.length)
            phone.text = s
            when (serviceGuide[position].cardStatus) {
                "0" -> state.text = "未启用"
                "1" -> {
                    state.setTextColor(ContextCompat.getColor(this@CardFjmInfo, R.color.blue))
                    state.text = "正常"
                }
                "2", "8" -> {
                    state.setTextColor(ContextCompat.getColor(this, R.color.red))
                    state.text = "挂失"
                }
                "7" -> state.text = "作废"
                "9" -> state.text = "注销"
            }
        }
        mx.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cardNo", cardNo)
            bundle.putString("nickName", name1)
            bundle.putString("is_other", "2")
            utils.startActivityBy(MoneyInfo::class.java, bundle)
        }
        mm.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cardNo", certNo)
            bundle.putString("name", name1)
            utils.startActivityBy(ChangeCardPass::class.java, bundle)
        }
        right_menu.setOnClickListener { startActivity<BindCardTwo>() }
        delete.setOnClickListener { delete() }
    }

    private fun initData() {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.unsmCardList(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                serviceGuide.clear()
                val bean = JSONObject.parseObject(data, object : TypeReference<FjmBean>() {})
                if ("0" == bean.result) {
                    serviceGuide.addAll(bean.cardList)
                    rl_zwwl.visibility = View.GONE
                    delete.visibility = View.VISIBLE
                    name.text = mk.decodeString(Tool.REAL_NAME, "")
                    val certNo = serviceGuide[0].certNo
                    val s = certNo.substring(0, 3) + "****" + certNo.substring(certNo.length - 4, certNo.length)
                    phone.text = s
                    qd.text = "柜面"
                    address.text = "非记名市民卡   1/" + serviceGuide.size
                    time.text = serviceGuide[0].createTime
                    name.text = serviceGuide[0].clientName
                    cardNo = serviceGuide[0].cardNo
                    when (serviceGuide[0].cardStatus) {
                        "0" -> state.text = "未启用"
                        "1" -> {
                            state.setTextColor(ContextCompat.getColor(this@CardFjmInfo, R.color.blue))
                            state.text = "正常"
                        }
                        "2", "8" -> {
                            state.setTextColor(ContextCompat.getColor(this@CardFjmInfo, R.color.red))
                            state.text = "挂失"
                        }
                        "7" -> state.text = "作废"
                        "9" -> state.text = "注销"
                    }
                    adapt!!.notifyDataSetChanged()
                } else {
                    address.text = "非记名市民卡   0/0"
                    delete.visibility = View.GONE
                    rl_zwwl.visibility = View.VISIBLE
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("请求失败，请稍后重试")
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    private fun delete() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        LogUtils.i("===>", cardNo)
        HttpRequestPort.instance.deleteCard(userId, cardNo, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    initData()
                } else {
                    utils.showToast(bean.message)
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("请求失败，请稍后重试")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initData()
    }
}
