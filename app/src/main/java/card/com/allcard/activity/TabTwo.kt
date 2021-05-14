package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.GridTopAdapter
import card.com.allcard.adapter.TabTopAdapter
import card.com.allcard.adapter.TabTwoAdapter
import card.com.allcard.bean.ResultBean
import card.com.allcard.bean.TabTwoBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.LogUtils
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_tab_two.*
import kotlinx.android.synthetic.main.title.*
import java.util.*

class TabTwo : BaseActivity(), GridTopAdapter.GridClickListener, TabTwoAdapter.TwoClickListener {
    override fun layoutId(): Int = R.layout.activity_tab_two
    //全部图标
    private var adapt: TabTwoAdapter? = null
    //顶部图标
    private var topAdapter: TabTopAdapter? = null
    //顶部编辑图标
    private var gridAdapter: GridTopAdapter? = null

    //编辑状态
    companion object {
        @SuppressLint("StaticFieldLeak")
        var edit = 0
        @SuppressLint("StaticFieldLeak")
        val TopList = ArrayList<TabTwoBean.ListBean.SummarydetailBean>()
    }

    private val dataList = ArrayList<TabTwoBean.ListBean.IconAllBean>()
    //上传图标数组
    private val iconList = ArrayList<String>()

    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address.text = "服务"
        close.visibility = View.GONE
        topAdapter = TabTopAdapter(this, R.layout.im_item, TopList)
        gridAdapter = GridTopAdapter(this, TopList, R.layout.gridview_item)
        gridAdapter!!.gridClickListener = this
        adapt = TabTwoAdapter(this, dataList, R.layout.tab_two_item)
        adapt!!.Click = this
        val layoutManager = LinearLayoutManager(this@TabTwo)
        topList.layoutManager = layoutManager
        layoutManager.orientation = OrientationHelper.HORIZONTAL
        topList.adapter = topAdapter
        all_list.adapter = adapt
        top_grid.adapter = gridAdapter
        tv_edit.setOnClickListener {
            if (!noUserId()) {
                grid_title.visibility = View.GONE
                ll_top_apps.visibility = View.GONE
                rl_top.visibility = View.VISIBLE
                edit = 1
            }
        }
        done.setOnClickListener {
            rl_top.visibility = View.GONE
            grid_title.visibility = View.VISIBLE
            ll_top_apps.visibility = View.VISIBLE
            edit = 0
            putUp()
        }
        refresh.setOnRefreshListener { refresh ->
            initData()
            refresh.finishRefresh()
        }
        restart.setOnClickListener {
            initData()
        }
        refresh.autoRefresh()
    }

    private fun putUp() {
        iconList.clear()
        for (i in 0 until TopList.size) {
            iconList.add(TopList[i].id)
        }
        val icon = iconList.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "").trim()
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.iconSave(userId, icon, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<ResultBean>() {})
//                if (bean.status != "0") {
//                    //utils.showToast(bean.message)
//                }
            }
        })
    }

    private fun initData() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.tabTwo(userId, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                LogUtils.i(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<TabTwoBean>() {})
                if (bean.result == "0") {
                    if (rl_zw.visibility == View.VISIBLE) {
                        rl_zw.visibility = View.GONE
                    }
                    dataList.clear()
                    TopList.clear()
                    dataList.addAll(bean.list.iconAll)
                    TopList.addAll(bean.list.summarydetail)
                    adapt!!.notifyDataSetChanged()
                    topAdapter!!.notifyDataSetChanged()
                    gridAdapter!!.notifyDataSetChanged()
                } else {
                    if (rl_zw.visibility == View.GONE) {
                        rl_zw.visibility = View.VISIBLE
                    }
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                if (rl_zw.visibility == View.GONE) {
                    rl_zw.visibility = View.VISIBLE
                }
                utils.showToast("加载失败，请稍后重试")
            }
        })
    }

    override fun onClickListener(position: Int) {
        TopList.removeAt(position)
        adapt!!.notifyDataSetChanged()
        gridAdapter!!.notifyDataSetInvalidated()
        topAdapter!!.notifyDataSetChanged()

    }

    override fun onClickListener() {
        adapt!!.notifyDataSetChanged()
        gridAdapter!!.notifyDataSetInvalidated()
        topAdapter!!.notifyDataSetChanged()
    }

    private fun noUserId(): Boolean {
        val userId = mk.decodeString(Tool.USER_ID, "")
        if (TextUtils.isEmpty(userId)) {
            startActivity(Intent(this, LoginActivity::class.java).putExtra("from", 1))
            return true
        }
        return false
    }
}
