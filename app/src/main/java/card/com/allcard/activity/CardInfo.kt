package card.com.allcard.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.adapter.CardSAdapter
import card.com.allcard.bean.CardBean
import card.com.allcard.bean.CardStatusBean
import card.com.allcard.bean.GetNum
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_card_info.*
import kotlinx.android.synthetic.main.title.*


class CardInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_info
    private var serviceGuide = mutableListOf<String>()
    private var adapt: CardSAdapter? = null
    private var gsPop: PopupWindow? = null
    private var jgPop: PopupWindow? = null
    private var cardNo = ""
    private var nameNo = ""
    private var certNo = ""
    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        list.setFlatFlow(true)//平面滚动
        adapt = CardSAdapter(this, R.layout.card_item, serviceGuide)
        address.text = "社保卡服务"
        close.setOnClickListener { finish() }
        MyApplication.instance.addActivity(this)
        list.adapter = adapt
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
        HttpRequestPort.instance.CardInfo(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                serviceGuide.clear()
                val bean = JSONObject.parseObject(data, object : TypeReference<CardBean>() {})
                if (bean.result == "0") {
                    cardNo = bean.aaZ500
                    nameNo = bean.aaC003
                    certNo = bean.certNo
                    serviceGuide.add(bean.aaZ500)
                    name.text = bean.aaC003
                    num.text = bean.certNo.substring(0, 6) + "********" +
                            bean.certNo.substring(bean.certNo.length - 4, bean.certNo.length)
                    adapt!!.notifyDataSetChanged()
                    cardStatus(bean.aaC003,bean.aaZ500,bean.certNo)
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

    private fun cardStatus(name:String,cardNo:String,certNo:String) {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.getCardStatus(userId,name,cardNo,certNo, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                val bean = JSONObject.parseObject(data, object : TypeReference<CardStatusBean>() {})
                if(bean.result == "0"){
                    if(bean.cardState == "正常"){
                        state.text = "正常"
                        state.setTextColor(ContextCompat.getColor(this@CardInfo,R.color.blue))
                        gs.text = "卡挂失"
                        im_gs.visibility=View.GONE
                    }else{
                        state.text = "挂失"
                        state.setTextColor(ContextCompat.getColor(this@CardInfo,R.color.red))
                        gs.text = "卡解挂"
                        im_gs.visibility=View.VISIBLE
                    }
                }else{
                    state.text = "未知"
                    gs.visibility = View.GONE
                    im_gs.visibility=View.GONE
                }
            }
            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("卡状态请求失败，请稍后重试")
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
        HttpRequestPort.instance.gs(userId, cardNo, "1", object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    utils.showToast("挂失成功")
                    cardStatus(nameNo,cardNo,certNo)
                } else {
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
        HttpRequestPort.instance.jg(userId, cardNo, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    utils.showToast("解挂成功")
                    cardStatus(nameNo,cardNo,certNo)
                } else {
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
