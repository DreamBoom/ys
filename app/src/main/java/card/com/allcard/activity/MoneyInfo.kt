package card.com.allcard.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.adapter.MoneyAdapter
import card.com.allcard.bean.MoneyBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import kotlinx.android.synthetic.main.activity_money_info.*
import java.text.SimpleDateFormat
import java.util.*

class MoneyInfo : BaseActivity(), OnDateSetListener,MoneyAdapter.ClickListener {
    override fun layoutId(): Int = R.layout.activity_money_info
    @SuppressLint("SimpleDateFormat")
    private var sf = SimpleDateFormat("yyyy-MM")
    private var timeDialog: TimePickerDialog? = null
    var dat = ""
    var cardNo = ""
    var nickName = ""
    var is_other = ""
    private  var type = 1
    private  var trCode = "all"
    var adapter: MoneyAdapter? = null
    private val dataList = ArrayList<MoneyBean.DetailListBeanX>()

    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address.text = "余额明细"
        close.setOnClickListener { finish() }
        right_menu.visibility = View.VISIBLE
        right_menu.text = "筛选"
        right_menu.setOnClickListener { showPopup(type) }
        refresh.setEnableOverScrollDrag(false)
        refresh.setOnRefreshListener {
            refresh.finishRefresh()
            getList()
        }
        cardNo = intent.getStringExtra("cardNo")
        nickName = intent.getStringExtra("nickName")
        is_other = intent.getStringExtra("is_other")
        adapter = MoneyAdapter(this@MoneyInfo, dataList, R.layout.money_item_one)
        adapter!!.Click = this
        listView.adapter = adapter
        //data.setOnClickListener { checkDate() }
        //时间弹窗
        val tenYears = 10L * 365 * 1000 * 60 * 60 * 24L
        timeDialog = TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis() - tenYears)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(ContextCompat.getColor(this, R.color.blue))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(ContextCompat.getColor(this, R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(ContextCompat.getColor(this, R.color.blue))
                .setWheelItemTextSize(17)
                .setType(Type.YEAR_MONTH)
                .build()
        refresh.autoRefresh()
    }

    override fun onClickListener() {
        timeDialog!!.show(supportFragmentManager, "year_month")
    }

    override fun onDateSet(p0: TimePickerDialog?, millseconds: Long) {
        val d = Date(millseconds)
         dat = sf.format(d)
        refresh.autoRefresh()
    }

    fun getList() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.yuEDetail(userId, dat, "1", "500",
                cardNo, trCode,is_other,nickName,
                object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<MoneyBean>() {})
                        if (bean.result == "0") {
                            dataList.clear()
                            dataList.addAll(bean.detailList)
                            adapter!!.notifyDataSetChanged()
                        }
                    }
                })
    }

    var popupWindow: PopupWindow? = null
    private fun showPopup(i: Int) {
        val v = utils.getView(this, R.layout.pop_choose)
        popupWindow = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow!!.contentView = v
        popupWindow!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow!!.isClippingEnabled = false
        popupWindow!!.showAsDropDown(bar)
        val t1 = v.findViewById<TextView>(R.id.t1)
        val t2 = v.findViewById<TextView>(R.id.t2)
        val t3 = v.findViewById<TextView>(R.id.t3)
        val t4 = v.findViewById<TextView>(R.id.t4)
        val t5 = v.findViewById<TextView>(R.id.t5)
        val t6 = v.findViewById<TextView>(R.id.t6)
        when (i) {
            1 -> {
                t1.setTextColor(ContextCompat.getColor(this, R.color.blue))
                t1.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_blu)
            }
            2 -> {
                t2.setTextColor(ContextCompat.getColor(this, R.color.blue))
                t2.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_blu)
            }
            3 -> {
                t3.setTextColor(ContextCompat.getColor(this, R.color.blue))
                t3.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_blu)
            }
            4 -> {
                t4.setTextColor(ContextCompat.getColor(this, R.color.blue))
                t4.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_blu)
            }
            5 -> {
                t5.setTextColor(ContextCompat.getColor(this, R.color.blue))
                t5.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_blu)
            }
            6 -> {
                t6.setTextColor(ContextCompat.getColor(this, R.color.blue))
                t6.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_blu)
            }
        }
        v.findViewById<ImageView>(R.id.c_pop).setOnClickListener {
            popupWindow!!.dismiss()
        }
        v.findViewById<TextView>(R.id.q_pop).setOnClickListener {
            popupWindow!!.dismiss()
        }
        t1.setOnClickListener {
            type = 1
            trCode = "all"
            refresh.autoRefresh()
            popupWindow!!.dismiss()
        }
        t2.setOnClickListener {
            type = 2
            trCode = "cz"
            refresh.autoRefresh()
            popupWindow!!.dismiss()
        }
        t3.setOnClickListener {
            type = 3
            trCode = "2830"
            refresh.autoRefresh()
            popupWindow!!.dismiss()
        }
        t4.setOnClickListener {
            type = 4
            trCode = "1610"
            refresh.autoRefresh()
            popupWindow!!.dismiss()
        }
        t5.setOnClickListener {
            type = 5
            trCode = "2835"
            refresh.autoRefresh()
            popupWindow!!.dismiss()
        }
        t6.setOnClickListener {
            type = 6
            trCode = "2834"
            refresh.autoRefresh()
            popupWindow!!.dismiss()
        }
    }
}
