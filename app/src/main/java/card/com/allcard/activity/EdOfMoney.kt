package card.com.allcard.activity

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import card.com.allcard.R
import card.com.allcard.bean.EduBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.KeyboardStateObserver
import card.com.allcard.utils.MoneyInputFilter
import card.com.allcard.utils.MoneyInputFilter2
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_ed_of_money.*
import kotlinx.android.synthetic.main.title.*

class EdOfMoney : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_ed_of_money
    var d1 =""
    var d2 =""
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        MyApplication.instance.addActivity(this)
        address.text = "资金额度设置"
        bt_change.isEnabled = false
        bt_change.setOnClickListener {
            et1.isFocusable = false
            et1.isFocusableInTouchMode = false
            et1.setTextColor(ContextCompat.getColor(this@EdOfMoney,R.color.grey91))
            et2.isFocusable = false
            et2.isFocusableInTouchMode = false
            et1.setTextColor(ContextCompat.getColor(this@EdOfMoney,R.color.grey91))
            if(TextUtils.isEmpty(et1.text.toString().trim())||TextUtils.isEmpty(et2.text.toString().trim())){
                utils.showToast("请输入修改金额")
                return@setOnClickListener
            }
            val t1 = (et1.text.toString().trim().toDouble()*100).toString()
            val t2 = (et2.text.toString().trim().toDouble()*100).toString()
            upMoney(t1,t2)
        }
        et1.addTextChangedListener(moneyWatcher(et1))
        et2.addTextChangedListener(moneyWatcher(et2))
        initData()
        et1.isFocusable = false
        et1.isFocusableInTouchMode = false
        et2.isFocusable = false
        et2.isFocusableInTouchMode = false
        ed1.setOnClickListener {
            et1.isFocusableInTouchMode = true
            et1.isFocusable = true
            et1.setTextColor(ContextCompat.getColor(this,R.color.black))
            et1.setText("".toCharArray(), 0, "".length)
            et1.requestFocus()
            et1.setSelection(et1.text.length)
            bt_change.isEnabled = true
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT)
            if(TextUtils.isEmpty(et2.text)){
                et2.setText(d2.toCharArray(), 0, d2.length)
                et2.setTextColor(ContextCompat.getColor(this,R.color.grey91))
            }
        }
        ed2.setOnClickListener {
            et2.isFocusableInTouchMode = true
            et2.isFocusable = true
            et2.setTextColor(ContextCompat.getColor(this,R.color.black))
            et2.setText("".toCharArray(), 0, "".length)
            et2.requestFocus()
            et2.setSelection(et2.text.length)
            bt_change.isEnabled = true
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et2, InputMethodManager.SHOW_IMPLICIT)
            if(TextUtils.isEmpty(et1.text)){
                et1.setText(d1.toCharArray(), 0, d1.length)
                et1.setTextColor(ContextCompat.getColor(this,R.color.grey91))
            }
        }

        KeyboardStateObserver.getKeyboardStateObserver(this).
                setKeyboardVisibilityListener(object :KeyboardStateObserver.OnKeyboardVisibilityListener{
                    override fun onKeyboardShow() {

                    }
                    override fun onKeyboardHide() {
                        et1.isFocusable = false
                        et1.isFocusableInTouchMode = false
                        et2.isFocusable = false
                        et2.isFocusableInTouchMode = false
                        if(TextUtils.isEmpty(et1.text)){
                            et1.setText(d2.toCharArray(), 0, d2.length)
                            et1.setTextColor(ContextCompat.getColor(this@EdOfMoney,R.color.grey91))
                            bt_change.isEnabled = false
                        }
                        if(TextUtils.isEmpty(et2.text)){
                            et2.setText(d2.toCharArray(), 0, d2.length)
                            et2.setTextColor(ContextCompat.getColor(this@EdOfMoney,R.color.grey91))
                            bt_change.isEnabled = false
                        }
                    }
                })
    }

    private fun initData() {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.cardParamList(userId, "0","","",object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<EduBean>() {})
                val status = bean.result
                if (status == "0") {
                    val mo1 = bean.cardsList[1].single_consumption_amount
                    val mo2 = bean.cardsList[1].account_balance_ceiling
                    d1 = utils.save2(mo1.toDouble() * 0.01)
                    d2 = utils.save2(mo2.toDouble() * 0.01)
                    et1.setText(d1.toCharArray(), 0,d1.length)
                    et2.setText(d2.toCharArray(), 0, d2.length)
                    val m1 = bean.cardsList[0].single_consumption_amount
                    val m2 = bean.cardsList[0].account_balance_ceiling
                    val dl1 = utils.save2(m1.toDouble() * 0.01)
                    val dl2 = utils.save2(m2.toDouble() * 0.01)
                    MoneyInputFilter.MAX_VALUE = (m1.toDouble()+1) * 0.01
                    xt_m1.text = "您可根据需求调整额度,可调区间为0~$dl1 元"
                    MoneyInputFilter2.MAX_VALUE = (m2.toDouble()+1) * 0.01
                    xt_m2.text = "您可根据需求调整额度,可调区间为0~$dl2 元"
                    val filters = arrayOf<InputFilter>(MoneyInputFilter(this@EdOfMoney))
                    et1.filters = filters
                    val filters2 = arrayOf<InputFilter>(MoneyInputFilter2(this@EdOfMoney))
                    et2.filters = filters2
                } else {
                    utils.showToast("额度加载失败，返回后重进加载")
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("额度加载失败，返回后重进加载")
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    private fun upMoney(m1:String,m2:String){
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.setCardParam(userId,m1,m2, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<EduBean>() {})
                val status = bean.result
                if (status == "0") {
                    et2.setTextColor(ContextCompat.getColor(this@EdOfMoney,R.color.grey91))
                    et2.setTextColor(ContextCompat.getColor(this@EdOfMoney,R.color.grey91))
                    bt_change.isEnabled = false
                    utils.showToast("修改成功")
                }else{
                    utils.showToast("修改失败，请稍后重试")
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("网络失败，请稍后重试")
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    internal inner class moneyWatcher(var editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }
        override fun afterTextChanged(s: Editable) {
            val text = s.toString()
            if (text.isNotEmpty() && text.substring(0, 1) == "0") {
                editText.setText(text.substring(1))
            }
        }
    }
}
