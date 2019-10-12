package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import card.com.allcard.R
import card.com.allcard.adapter.PayTypeAdapter
import card.com.allcard.bean.AccBean
import card.com.allcard.bean.EduBean
import card.com.allcard.bean.GetNum
import card.com.allcard.bean.PayTypeBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.EditInputFilter
import card.com.allcard.utils.LogUtils
import card.com.allcard.view.MyListView
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.example.caller.BankABCCaller
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_money_in.*
import kotlinx.android.synthetic.main.title.*
import org.xutils.x

class MoneyIn : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_money_in
    private var adapter0: PayTypeAdapter? = null
    val dataList = ArrayList<PayTypeBean.ListBean>()
    var cardNo = ""
    var nickName = ""
    var payWay = ""
    var flag = ""
    var actionNo = ""
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        address.text = "支付"
        cardNo = intent.getStringExtra("cardNo")
        nickName = intent.getStringExtra("nickName")
        flag = intent.getStringExtra("flag")
        bt_cz.isEnabled = false
        bt_cz.setOnClickListener {
            if (flag == "2") {
                accOther()
            } else {
                acc()
            }
        }
        im_pay.setImageDrawable(ContextCompat.getDrawable(this@MoneyIn, R.drawable.img_yl))
        et_money!!.addTextChangedListener(PhoneWatcher(et_money))
        val filters = arrayOf<InputFilter>(EditInputFilter())
        et_money.filters = filters
        payType.setOnClickListener { showPopup() }
        adapter0 = PayTypeAdapter(this, dataList, R.layout.pay_item0)
        //获取充值类型
        HttpRequestPort.instance.baseData("paytype", object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<PayTypeBean>() {})
                if (bean.result == "0") {
                    if (bean.list.size > 0) {
                        dataList.addAll(bean.list)
                        dataList[0].is_enable = "1"
                        payWay = dataList[0].para_value
                        adapter0!!.notifyDataSetChanged()
                    }
                }
                //获取充值额度
                initData()
            }
        })
    }

    private fun initData() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.cardParamList(userId, flag, cardNo, nickName, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<EduBean>() {})
                if (bean.result == "0") {
                    val m1 = bean.cardsList[0].account_balance_ceiling.toDouble() * 0.01
                    EditInputFilter.MAX_VALUE = m1 - bean.balance.toDouble() * 0.01
                    ed.text = "账户余额不能超过$m1 元，还可充值${EditInputFilter.MAX_VALUE}元"
                }
            }
        })
    }

    var popupWindow: PopupWindow? = null
    private fun showPopup() {
        val v = utils.getView(this, R.layout.pop_pay)
        popupWindow = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow!!.contentView = v
        popupWindow!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow!!.isClippingEnabled = false
        popupWindow!!.showAsDropDown(bar)
        val list = v.findViewById<MyListView>(R.id.list)
        list.adapter = adapter0
        list.setOnItemClickListener { parent, view, position, id ->
            for (i in 0 until dataList.size) {
                dataList[i].is_enable = "0"
            }
            dataList[position].is_enable = "1"
            payWay = dataList[position].para_value
            x.image().bind(im_pay, dataList[position].img)
            pay_name.text = dataList[position].para_name
            popupWindow!!.dismiss()
        }
    }

    private fun acc() {
        utils.getProgress(this)
        val money = et_money.text.toString().trim()
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.acc(userId, cardNo, "5", payWay, money, flag,
                object : BaseHttpCallBack(this) {
                    @SuppressLint("SetTextI18n")
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<AccBean>() {})
                        if (bean.result == "0") {
                            when (payWay) {
                                "unionpay" -> {
                                    actionNo = bean.actionNo
                                    val bundle = Bundle()
                                    bundle.putString("num", bean.actionNo)
                                    bundle.putString("url", bean.paymentURL)
                                    utils.startActivityBy(WebViewActivity::class.java, bundle)
                                }
                                "epay" -> {
                                    if (BankABCCaller.isBankABCAvaiable(this@MoneyIn)) {
                                        mk.encode(Tool.BY_LOGIN, "1")
                                        actionNo = bean.actionNo
                                        /**调起农行掌银*/
                                        BankABCCaller.startBankABC(this@MoneyIn,
                                                "card.com.allcard",
                                                "card.com.allcard.activity.MoneyIn",
                                                "pay", bean.paymentURL)
                                    } else {//客户手机未安装农行掌银APP的处理逻辑
                                        utils.showToast("未安装农行掌银，或已安装农行掌银版本不支持")
                                    }
                                }
                                "wxpay" -> {
                                    actionNo = bean.actionNo
                                    val api = WXAPIFactory.createWXAPI(this@MoneyIn, "wx1467bf6a8cf0dffd")
                                    val json = org.json.JSONObject(bean.paymentURL)
                                    val req = PayReq()
                                    req.nonceStr = json.getString("noncestr")
                                    req.packageValue = json.getString("package")
                                    req.partnerId = json.getString("partnerid")
                                    req.prepayId = json.getString("prepayid")
                                    req.sign = json.getString("sign")
                                    req.appId = json.getString("sub_appid")
                                    req.timeStamp = json.getString("timestamp")
                                    api.registerApp("wx1467bf6a8cf0dffd")
                                    api.sendReq(req)
                                }
                            }
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

    private fun accOther() {
        utils.getProgress(this)
        val money = et_money.text.toString().trim()
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.accOther(userId, "5", payWay, money, "1", nickName,
                object : BaseHttpCallBack(this) {
                    @SuppressLint("SetTextI18n")
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<AccBean>() {})
                        if (bean.result == "0") {
                            when (payWay) {
                                "unionpay" -> {
                                    actionNo = bean.actionNo
                                    val bundle = Bundle()
                                    bundle.putString("num", bean.actionNo)
                                    bundle.putString("url", bean.paymentURL)
                                    utils.startActivityBy(WebViewActivity::class.java, bundle)
                                }
                                "epay" -> {
                                    if (BankABCCaller.isBankABCAvaiable(this@MoneyIn)) {
                                        mk.encode(Tool.BY_LOGIN, "1")
                                        actionNo = bean.actionNo
                                        /**调起农行掌银*/
                                        BankABCCaller.startBankABC(this@MoneyIn,
                                                "card.com.allcard",
                                                "card.com.allcard.activity.MoneyIn",
                                                "pay", bean.paymentURL)
                                    } else {//客户手机未安装农行掌银APP的处理逻辑
                                        utils.showToast("未安装农行掌银，或已安装农行掌银版本不支持")
                                    }
                                }
                                "wxpay" -> {
                                    actionNo = bean.actionNo
                                    val api = WXAPIFactory.createWXAPI(this@MoneyIn, "wx1467bf6a8cf0dffd")
                                    val json = org.json.JSONObject(bean.paymentURL)
                                    val req = PayReq()
                                    req.nonceStr = json.getString("noncestr")
                                    req.packageValue = json.getString("package")
                                    req.partnerId = json.getString("partnerid")
                                    req.prepayId = json.getString("prepayid")
                                    req.sign = json.getString("sign")
                                    req.appId = json.getString("sub_appid")
                                    req.timeStamp = json.getString("timestamp")
                                    api.registerApp("wx1467bf6a8cf0dffd")
                                    api.sendReq(req)
                                }
                            }
                        } else {
                            utils.showToast(bean.message)
                        }
                    }

                    override fun onFinished() {
                        super.onFinished()
                        utils.hindProgress()
                    }
                })
    }

    internal inner class PhoneWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            val y = 1
            bt_cz.isEnabled = editText.text.length >= y
        }
    }

    private fun isSuccess() {
        utils.getProgress(this)
        HttpRequestPort.instance.isSuccess(actionNo, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    utils.showToast(bean.message)

                    finish()
                } else {
                    utils.showToast("充值失败")
                }
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if(!TextUtils.isEmpty(actionNo)){
            isSuccess()
            actionNo = ""
        }
        //以下为农行回调处理
        val extra = intent.getStringExtra("from_bankabc_param")
        if (extra != null) {
            mk.encode(Tool.BY_LOGIN, "0")
            val code = extra.split("&")[1].split("=")[1]
            if ("0000" == code) {
                LogUtils.i("===>","农行返回：成功")
            } else {
                when (code) {
                    "9999" -> utils.showToast("取消支付")
                    "PA500401" -> utils.showToast("已存在成功支付的订单")
                    else -> {
                        utils.showToast("支付失败")
                    }
                }
            }
        }

    }
}
