package card.com.allcard.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.HospitalAdapter
import card.com.allcard.bean.HospitalList
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.tools.Tool.REQUETCODE_SEARCH
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_tab_three.*
import java.util.*

class TabThree : BaseActivity() {
    private var adapter: HospitalAdapter? = null
    private var noData: View? = null
    private var noWeb: View? = null
    var searchName = ""
    private val dataList = ArrayList<HospitalList.HospitalBean>()
    override fun layoutId(): Int = R.layout.activity_tab_three
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        area.setOnClickListener { utils.startActivityForResult(ChooseArea::class.java, REQUETCODE_SEARCH) }
        noData = utils.getView(R.layout.no_data)
        noWeb = utils.getView(R.layout.view_no_web)
        refresh.setEnableOverScrollDrag(false)
        refresh.setOnRefreshListener {
            refresh.finishRefresh()
            getList()
        }
        adapter = HospitalAdapter(this, dataList, R.layout.scrow_view_item)
        listView!!.adapter = adapter
        refresh.autoRefresh()
        search.setOnClickListener {
            searchName = et_search.text.toString().trim()
            refresh.autoRefresh()
            utils.hideSoftKeyboard()
        }
    }

    fun getList() {
        val areaId = mk.decodeString(Tool.Area_ID, "")
        HttpRequestPort.instance.hospitalList("1",
                "100", areaId, searchName, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val hospitalList = JSONObject.parseObject(data, object : TypeReference<HospitalList>() {})
                val pharmacy = hospitalList.hospital
                dataList.clear()
                if (pharmacy.size > 0) {
                    if (listView.headerViewsCount > 0) {
                        listView.removeHeaderView(noData)
                    }
                    dataList.addAll(pharmacy)
                    adapter!!.notifyDataSetChanged()
                } else {
                    if (listView.headerViewsCount == 0) {
                        listView.addHeaderView(noData)
                    }
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                dataList.clear()
                adapter!!.notifyDataSetChanged()
                if (listView.headerViewsCount > 0) {
                    listView.removeAllViews()
                }
                listView.addHeaderView(noWeb)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Tool.RESULTCODE_SUCCESS) {
            return
        }
        when (requestCode) {
            REQUETCODE_SEARCH -> {
                val name = data!!.getStringExtra("name")
                if (TextUtils.isEmpty(name)) {
                    area.text = "全部"
                } else {
                    area.text = name
                }
                getList()
            }
        }
    }
}
