package card.com.allcard.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import card.com.allcard.R
import card.com.allcard.adapter.PayTypeAdapter
import card.com.allcard.bean.PayTypeBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.EditInputFilter
import card.com.allcard.view.MyListView
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
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
            if (flag == "3") {

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
        HttpRequestPort.instance.baseData("paytype", object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<PayTypeBean>() {})
                if (bean.result == "0") {
                    bean.list[0].is_enable = "1"
                    payWay = dataList[0].para_value
                    dataList.addAll(bean.list)
                    adapter0!!.notifyDataSetChanged()
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

    fun acc() {
        utils.getProgress(this)
        val money = et_money.text.toString().trim()
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.acc(userId, "", "5", payWay, money, flag,
                object : BaseHttpCallBack(this) {
                    @SuppressLint("SetTextI18n")
                    override fun success(data: String) {
                        super.success(data)
//                val bean = JSONObject.parseObject(data, object : TypeReference<PayTypeBean>() {})
//                if (bean.result == "0") {
//                    bean.list[0].is_enable = "1"
//                    dataList.addAll(bean.list)
//                    adapter0!!.notifyDataSetChanged()
//                }
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
}
