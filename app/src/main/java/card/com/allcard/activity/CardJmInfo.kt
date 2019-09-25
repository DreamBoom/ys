package card.com.allcard.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.adapter.CardAdapter
import card.com.allcard.bean.GetNum
import card.com.allcard.bean.JmBeam
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_card_jm_info.*

class CardJmInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_jm_info
    private var serviceGuide = mutableListOf<JmBeam.CardsListBean>()
    private var adapt: CardAdapter? = null
    private var gsPop: PopupWindow? = null
    private var jgPop: PopupWindow? = null
    private var cardNo = ""
    private var pos = 1
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        list.setFlatFlow(true)//平面滚动
        adapt = CardAdapter(this, R.layout.card_item1, serviceGuide)
        address.text = "记名市民卡   1/" + serviceGuide.size

        close.setOnClickListener { finish() }
        list.adapter = adapt
        list.setOnItemSelectedListener { position ->
            serviceGuide[position].cardNo
            cardNo = serviceGuide[position].cardNo
            address.text = "记名市民卡   " + (position + 1).toString() + "/" + list.layoutManager!!.itemCount
            pos = position + 1
            time.text = serviceGuide[position].trDate
            when (serviceGuide[position].cardState) {
                "0" -> state.text = "未启用"
                "1" -> {
                    state.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    state.text = "正常"
                    gs.text = "卡挂失"
                }
                "2","8" -> {
                    state.setTextColor(ContextCompat.getColor(this, R.color.red))
                    state.text = "挂失"
                    gs.text = "卡解挂"
                }
                "7" -> state.text = "作废"
                "9" -> state.text = "注销"
            }
        }

        gs.setOnClickListener {
            when {
                state.text == "正常" -> gs()
                state.text == "挂失" -> jg()
                else -> utils.showToast("当前状态不可操作")
            }
        }
        initData()
        gsPop()
        jgPop()
    }

    private fun initData() {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.smCardList(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                serviceGuide.clear()
                val bean = JSONObject.parseObject(data, object : TypeReference<JmBeam>() {})
                if ("0" == bean.result) {
                    name.text = mk.decodeString(Tool.REAL_NAME, "")
                    val certNo = mk.decodeString(Tool.USER_NUM, "")
                    val s = certNo.substring(0, 3) + "********" + certNo.substring(certNo.length - 4, certNo.length)
                    num.text = s
                    type.text = "记名市民卡"
                    qd.text = "柜面"
                    serviceGuide.addAll(bean.cardsList)
                    address.text = "记名市民卡   $pos/${serviceGuide.size}"
                    time.text = serviceGuide[pos].trDate
                    cardNo = serviceGuide[pos].cardNo
                    when (serviceGuide[pos].cardState) {
                        "0" -> state.text = "未启用"
                        "1" -> {
                            state.setTextColor(ContextCompat.getColor(this@CardJmInfo, R.color.blue))
                            state.text = "正常"
                            gs.text = "卡挂失"
                        }
                        "2","8" -> {
                            state.setTextColor(ContextCompat.getColor(this@CardJmInfo, R.color.red))
                            state.text = "挂失"
                            gs.text = "卡解挂"
                        }
                        "7" -> state.text = "作废"
                        "9" -> state.text = "注销"
                    }
                    adapt!!.notifyDataSetChanged()

                } else {
                    utils.showToast(bean.message)
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

    private fun gs() {
        gsPop!!.showAtLocation(address, Gravity.NO_GRAVITY, 0, 0)
    }

    private fun jg() {
        jgPop!!.showAtLocation(address, Gravity.NO_GRAVITY, 0, 0)
    }

    //挂失弹窗
    @SuppressLint("InflateParams")
    private fun gsPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "确定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "确定挂失当前记名市民卡?"
        gsPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        gsPop!!.isTouchable = true
        gsPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        gsPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            gsk()
            gsPop!!.dismiss()
        }
        cancel.setOnClickListener {
            gsPop!!.dismiss()
        }
    }


    //解挂弹窗
    @SuppressLint("InflateParams")
    private fun jgPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "确定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "确定解挂当前记名市民卡?"
        jgPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        jgPop!!.isTouchable = true
        jgPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        jgPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            jgk()
            jgPop!!.dismiss()
        }
        cancel.setOnClickListener {
            jgPop!!.dismiss()
        }
    }

    private fun gsk() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.gs(userId, cardNo,"1",object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if(bean.result == "0"){
                    utils.showToast("挂失成功")
                    initData()
                }else{
                    utils.showToast("挂失失败")
                }
            }
            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("网络失败，请稍后重试")
            }
        })
    }

    private fun jgk() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.jg(userId, cardNo,object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if(bean.result == "0"){
                    utils.showToast("解挂成功")
                    initData()
                }else{
                    utils.showToast("解挂失败")
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("网络失败，请稍后重试")
            }
        })
    }
}
