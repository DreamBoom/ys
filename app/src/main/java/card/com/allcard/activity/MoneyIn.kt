package card.com.allcard.activity

import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import card.com.allcard.R
import kotlinx.android.synthetic.main.activity_money_in.*
import kotlinx.android.synthetic.main.title.*

class MoneyIn : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_money_in
    private var type = 1
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        address.text = "支付"
        bt_cz.isEnabled = false
        bt_cz.setOnClickListener {  }
        im_pay.setImageDrawable(ContextCompat.getDrawable(this@MoneyIn,R.drawable.img_yl))
        et_money!!.addTextChangedListener(PhoneWatcher(et_money))
        payType.setOnClickListener { showPopup(type) }
    }

    var popupWindow: PopupWindow? = null

    private fun showPopup(i: Int) {
        val v = utils.getView(this, R.layout.pop_pay)
        popupWindow = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow!!.contentView = v
        popupWindow!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow!!.isClippingEnabled = false
        popupWindow!!.showAsDropDown(bar)
        val pay1 = v.findViewById<LinearLayout>(R.id.pay1)
        val pay2 = v.findViewById<LinearLayout>(R.id.pay2)
        val pay3 = v.findViewById<LinearLayout>(R.id.pay3)
        val pay4 = v.findViewById<LinearLayout>(R.id.pay4)
        val imP1 = v.findViewById<ImageView>(R.id.im_p1)
        val imP2 = v.findViewById<ImageView>(R.id.im_p2)
        val imP3 = v.findViewById<ImageView>(R.id.im_p3)
        val imP4 = v.findViewById<ImageView>(R.id.im_p4)
        when(i){
            1 -> imP1.visibility = View.VISIBLE
            2 -> imP2.visibility = View.VISIBLE
            3 -> imP3.visibility = View.VISIBLE
            4 -> imP4.visibility = View.VISIBLE
        }
        pay1.setOnClickListener {
            type = 1
            im_pay.setImageDrawable(ContextCompat.getDrawable(this@MoneyIn,R.drawable.img_yl))
            pay_name.text = "银联支付"
            popupWindow!!.dismiss()

        }
        pay2.setOnClickListener {
            type = 2
            im_pay.setImageDrawable(ContextCompat.getDrawable(this@MoneyIn,R.drawable.img_kef))
            pay_name.text = "农行快e付"
            popupWindow!!.dismiss()

        }
        pay3.setOnClickListener {
            type = 3
            im_pay.setImageDrawable(ContextCompat.getDrawable(this@MoneyIn,R.drawable.img_wx))
            pay_name.text = "微信支付"
            popupWindow!!.dismiss()

        }
        pay4.setOnClickListener {
            type = 4
            im_pay.setImageDrawable(ContextCompat.getDrawable(this@MoneyIn,R.drawable.img_zfb))
            pay_name.text = "支付宝支付"
            popupWindow!!.dismiss()

        }
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
