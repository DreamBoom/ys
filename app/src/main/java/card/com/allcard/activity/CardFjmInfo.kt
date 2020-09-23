package card.com.allcard.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.adapter.CardFjmAdapter
import card.com.allcard.bean.FjmBean
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_card_fjm_info.*
import kotlinx.android.synthetic.main.fjm.*

class CardFjmInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_fjm_info
    private var serviceGuide = mutableListOf<FjmBean.CardListBean>()
    private var adapt: CardFjmAdapter? = null
    var certNo = ""
    var name1 = ""
    var cardNo = ""
    var phone0 = ""
    private var pos = 0
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        list.setFlatFlow(true)//平面滚动
        MyApplication.instance.addActivity(this)
        adapt = CardFjmAdapter(this, R.layout.card_item1, serviceGuide)
        address.text = "非记名市民卡   0/" + serviceGuide.size
        close.setOnClickListener { finish() }
        list.adapter = adapt
        list.setOnItemSelectedListener { position ->
            pos = position
            address.text = "非记名市民卡   " + (position + 1).toString() + "/" + list.layoutManager!!.itemCount
            time.text = serviceGuide[position].updateTime
            name1 = serviceGuide[position].clientName
            qd.text = serviceGuide[position].cardStateName
            name.text = name1
            cardNo = serviceGuide[position].cardNo
            val phoneNum = serviceGuide[position].certNo
            val s = phoneNum.substring(0, 3) + "****" + phoneNum.substring(phoneNum.length - 4, phoneNum.length)
            phone.text = s
            phone0 = phoneNum
            when (serviceGuide[position].cardStatus) {
                "0" -> {
                    state.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    state.text = "正常"
                }
                else -> {
                    state.setTextColor(ContextCompat.getColor(this, R.color.red))
                    state.text = "注销"
                }
            }
        }

        yue.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cardNo", cardNo)
            bundle.putString("name", name1)
            bundle.putString("num", certNo)
            utils.startActivityBy(PayMoney::class.java, bundle)
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
            bundle.putString("phone", phone0)
            bundle.putString("name", name1)
            utils.startActivityBy(ChangeCardPass::class.java, bundle)
        }
        jl.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", HttpRequestPort.H5_BASE_URL+
                    "weixin/hospital_v2/medicalHospitalRecordUn.jsp?cardNo=$cardNo&fixparam=android&version=v101")
            utils.startActivityBy(WebOther::class.java, bundle)
        }

        right_menu.setOnClickListener { startActivity<BindCardTwo>() }
        delete.setOnClickListener { exitPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0) }
        exitPop()
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
                    rl_zw.visibility = View.GONE
                    clue.visibility = View.VISIBLE
                    val certNo = serviceGuide[pos].certNo
                    val s = certNo.substring(0, 3) + "****" + certNo.substring(certNo.length - 4, certNo.length)
                    phone.text = s
                    phone0 = certNo
                    qd.text = serviceGuide[pos].cardStateName
                    address.text = "非记名市民卡   ${pos+1}/" + serviceGuide.size
                    time.text = serviceGuide[pos].updateTime
                    name.text = serviceGuide[pos].clientName
                    name1 = serviceGuide[pos].clientName
                    cardNo = serviceGuide[pos].cardNo
                    when (serviceGuide[pos].cardStatus) {
                        "0" -> {
                            state.setTextColor(ContextCompat.getColor(this@CardFjmInfo, R.color.blue))
                            state.text = "正常"
                        }
                        else -> {
                            state.setTextColor(ContextCompat.getColor(this@CardFjmInfo, R.color.red))
                            state.text = "注销"
                        }
                    }
                    adapt!!.notifyDataSetChanged()
                } else {
                    address.text = "非记名市民卡   0/0"
                    clue.visibility = View.GONE
                    rl_zw.visibility = View.VISIBLE
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

    private var exitPop: PopupWindow? = null
    @SuppressLint("InflateParams")
    private fun exitPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "确定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "删除本张市民卡?"
        exitPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        exitPop!!.isTouchable = true
        exitPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        exitPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            delete()
            exitPop!!.dismiss()
        }
        cancel.setOnClickListener {
            exitPop!!.dismiss()
        }
    }

    private fun delete() {
        val userId = mk.decodeString(Tool.USER_ID, "")
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
