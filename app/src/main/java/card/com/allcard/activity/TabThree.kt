package card.com.allcard.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.TabThreeAdapter
import card.com.allcard.bean.HospitalList
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.tools.Tool.REQUETCODE_SEARCH
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_tab_three.*
import java.util.*

class TabThree : BaseActivity() {
    private var adapter: TabThreeAdapter? = null
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
        adapter = TabThreeAdapter(this, R.layout.scrow_view_item,dataList)
        listView!!.adapter = adapter
        listView.layoutManager = LinearLayoutManager(this@TabThree)
        //动画效果
        adapter!!.openLoadAnimation(BaseQuickAdapter.EMPTY_VIEW)
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
                val bean = JSONObject.parseObject(data, object : TypeReference<HospitalList>() {})
                dataList.clear()
                setData(bean.hospital)
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                setData(null)
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

    private fun setData(data: List<HospitalList.HospitalBean>?) {
        if ( adapter!!.headerLayout != null) {
            adapter!!.removeAllHeaderView()
        }
        adapter!!.setNewData(data)
        when {
            data == null -> adapter!!.addHeaderView(utils.getView(this, R.layout.view_no_web))
            data.size < 1 -> adapter!!.addHeaderView(utils.getView(this, R.layout.no_data))
        }
    }
}
