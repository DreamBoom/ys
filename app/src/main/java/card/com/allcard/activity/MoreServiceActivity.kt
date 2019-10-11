package card.com.allcard.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.View
import android.widget.RadioGroup
import card.com.allcard.R
import card.com.allcard.adapter.ServiceAdapter
import card.com.allcard.bean.ServiceListBean
import card.com.allcard.bean.ServiceTypeBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.tools.Tool.RESULTCODE_SERVICE
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_more_service.*
import kotlinx.android.synthetic.main.title.*
import java.util.*

class MoreServiceActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener,
         TabLayout.OnTabSelectedListener {
    override fun layoutId(): Int = R.layout.activity_more_service
    private var adapter: ServiceAdapter? = null
    private var noData: View? = null
    private var noWeb: View? = null
    private val dataList = ArrayList<ServiceListBean.ListBean>()
    var tabNum = ""
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        address.text = "服务指南"
        noData = utils.getView(R.layout.no_data)
        noWeb = utils.getView(R.layout.view_no_web)
        HttpRequestPort.instance.manageType(object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<ServiceTypeBean>() {})
                if (bean.result == "0") {
                    tab.setUnboundedRipple(true)//点击的动画
                    tab.isTabIndicatorFullWidth = true//下划线指示器的宽度不要填充完
                    tab.addTab(tab.newTab().setText("全部"))
                    for (i in 0 until bean.list.size) {
                        tab.addTab(tab.newTab().setText(bean.list[i].para_key))
                    }
                    tab.tabMode = TabLayout.MODE_SCROLLABLE
                    tab.tabGravity = TabLayout.GRAVITY_FILL
                    tab.getTabAt(0)!!.select()
                    //默认选中
                    tab.addOnTabSelectedListener(this@MoreServiceActivity)
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("请求失败")
            }
        })

        all.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("tab", tab.selectedTabPosition)
            utils.toStartActivityForResult(AllService::class.java, RESULTCODE_SERVICE, bundle)
        }
        refresh.setEnableOverScrollDrag(false)
        refresh.setOnRefreshListener { refreshLayout -> getList() }
        adapter = ServiceAdapter(this, dataList, R.layout.news_list_item)
        listView.adapter = adapter
        refresh.autoRefresh()
    }

    fun getList(){
        HttpRequestPort.instance.serviceList(tabNum, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<ServiceListBean>() {})
                dataList.clear()
                if (bean.list.size > 0) {
                    if (listView!!.headerViewsCount > 0) {
                        listView!!.removeHeaderView(noData)
                    }
                    dataList.addAll(bean.list)
                    adapter!!.notifyDataSetChanged()
                } else {
                    if (listView!!.headerViewsCount == 0) {
                        listView!!.addHeaderView(noData)
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

            override fun onFinished() {
                super.onFinished()
                refresh!!.finishRefresh()
            }
        })
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab0: TabLayout.Tab?) {
        when {
            tab0!!.position == 0 -> {
                tabNum = ""
                refresh.autoRefresh()
            }
            else -> {
                tabNum = ""+tab0.position
                refresh.autoRefresh()
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Tool.RESULTCODE_SUCCESS) {
            return
        }
        when (requestCode) {
            RESULTCODE_SERVICE -> {
                val i = data!!.extras!!.getInt("tab", 0)
                tab.getTabAt(i)!!.select()
            }
        }
    }
}
