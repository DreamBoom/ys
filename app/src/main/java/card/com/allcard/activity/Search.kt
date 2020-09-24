package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.pawegio.kandroid.runDelayed
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.title.*
import java.text.SimpleDateFormat
import java.util.*

class Search : BaseActivity(), OnDateSetListener{
    override fun layoutId(): Int = R.layout.activity_search
    var timeType = 0
    var searchType = 0
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        address.text = "餐厅"
        initTime()
        et_time1.setOnClickListener {
            timeType = 0
            timeDialog!!.show(supportFragmentManager, "year_month_day")
        }
        et_time2.setOnClickListener {
            timeType = 1
            timeDialog!!.show(supportFragmentManager, "year_month_day")
        }
        et_type.setOnClickListener {
            utils.hideSoftKeyboard()
            runDelayed(200){
                showPopup()
            }
            }
        img_ok.setOnClickListener {
            val id = et_id.text.toString()
            if(TextUtils.isEmpty(id)){
                utils.showToast("请输入学生ID")
                return@setOnClickListener
            }
            if(searchType ==0){
                utils.showToast("请选择查询类型")
                return@setOnClickListener
            }
            if(et_time1.text =="请选择开始时间"){
                utils.showToast("请选择开始时间")
                return@setOnClickListener
            }
            if(et_time2.text =="请选择结束时间"){
                utils.showToast("请选择结束时间")
                return@setOnClickListener
            }
            val intent = Intent()
            intent.setClass(this,SearchResult::class.java)
            intent.putExtra("id",id)
            intent.putExtra("type",searchType)
            intent.putExtra("start",et_time1.text)
            intent.putExtra("end",et_time2.text)
            startActivity(intent)
        }
    }

    private var timeDialog: TimePickerDialog? = null
    @SuppressLint("SimpleDateFormat")
    private var sf = SimpleDateFormat("yyyy-MM-dd")
    fun initTime(){
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
                .setType(Type.YEAR_MONTH_DAY)
                .build()
    }

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        val d = Date(millseconds)
        if(timeType == 0){
            et_time1.setTextColor(ContextCompat.getColor(this,R.color.black3))
            et_time1.text = sf.format(d)
        }else{
            et_time2.setTextColor(ContextCompat.getColor(this,R.color.black3))
            et_time2.text = sf.format(d)
        }
    }

    var popupWindow: PopupWindow? = null
    private fun showPopup() {
        val v = utils.getView(this, R.layout.pop_type)
        popupWindow = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow!!.contentView = v
        popupWindow!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow!!.isClippingEnabled = true
        popupWindow!!.showAsDropDown(bar)
        val closePop = v.findViewById<ImageView>(R.id.pop_close)
        val moneyIn = v.findViewById<TextView>(R.id.money_in)
        val moneyOut = v.findViewById<TextView>(R.id.money_out)
        closePop.setOnClickListener { popupWindow!!.dismiss() }
        moneyIn.setOnClickListener {
            et_type.text = "充值"
            et_type.setTextColor(ContextCompat.getColor(this,R.color.black3))
            searchType=1
            popupWindow!!.dismiss()
        }
        moneyOut.setOnClickListener {
            et_type.text = "消费"
            et_type.setTextColor(ContextCompat.getColor(this,R.color.black3))
            searchType=2
            popupWindow!!.dismiss()
        }
    }
}