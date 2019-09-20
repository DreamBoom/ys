package card.com.allcard.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.adapter.CardAdapter
import card.com.allcard.bean.JmBeam
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_card_jm_info.*

class CardJmInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_jm_info
    private var serviceGuide = mutableListOf<JmBeam.CardsListBean>()
    private var adapt: CardAdapter? = null
    private var gsPop: PopupWindow? = null
    private var jgPop: PopupWindow? = null
    private var cardNo = ""
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        list.setFlatFlow(true)//平面滚动
        adapt = CardAdapter(this, R.layout.card_item1, serviceGuide)
        address.text = "记名市民卡   0/" + serviceGuide.size
        close.setOnClickListener { finish() }
        list.adapter = adapt
        list.setOnItemSelectedListener { position ->
            cardNo = serviceGuide[position].cardNo
            address.text = "记名市民卡   " + (position + 1).toString() + "/" + list.layoutManager!!.itemCount
            time.text = serviceGuide[position].trDate
            when (serviceGuide[position].cardState) {
                "0" -> state.text = "未启用"
                "1" -> state.text = "正常"
                "2" -> {
                    state.setTextColor(ContextCompat.getColor(this, R.color.red))
                    state.text = "挂失"
                    gs.text = "卡解挂"
                }
                "7" -> state.text = "作废"
                "8" -> state.text = "预挂失"
                "9" -> state.text = "注销"
            }
        }
        right_menu.setOnClickListener { startActivity<BindCardTwo>() }
        gs.setOnClickListener {
            if (TextUtils.isEmpty(state.text)) {
                utils.showToast("当前卡信息不存在")
                return@setOnClickListener
            }
            if (state.text == "正常") {
                gs()
            }

            if (state.text == "挂失") {
                jg()
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
                val bean = JSONObject.parseObject(data, object : TypeReference<JmBeam>() {})
                if ("0" == bean.result) {
                    name.text = mk.decodeString(Tool.REAL_NAME, "")
                    num.text = mk.decodeString(Tool.USER_NUM, "")
                    type.text = "记名市民卡"
                    qd.text = "柜面"
                    serviceGuide.addAll(bean.cardsList)
                    address.text = "记名市民卡   1/" + serviceGuide.size
                    time.text = serviceGuide[0].trDate
                    cardNo = serviceGuide[0].cardNo
                    when (serviceGuide[0].cardState) {
                        "0" -> state.text = "未启用"
                        "1" -> state.text = "正常"
                        "2" -> {
                            state.setTextColor(ContextCompat.getColor(this@CardJmInfo, R.color.red))
                            state.text = "挂失"
                        }
                        "7" -> state.text = "作废"
                        "8" -> state.text = "预挂失"
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
        }
        cancel.setOnClickListener {
            jgPop!!.dismiss()
        }
    }

    fun gsk() {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.gs(userId, cardNo,"1",object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
              //  val bean = JSONObject.parseObject(data, object : TypeReference<JmBeam>() {})
            }
        })
    }

    fun jgk() {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.jg(userId, cardNo,object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                //  val bean = JSONObject.parseObject(data, object : TypeReference<JmBeam>() {})
            }
        })
    }
}
