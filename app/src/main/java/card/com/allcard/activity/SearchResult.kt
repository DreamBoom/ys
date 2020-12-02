package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.SearchAdapter
import card.com.allcard.bean.SearchBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.utils.AES
import card.com.allcard.utils.LogUtils
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.title.*

class SearchResult : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_search_result
    private var dataList = java.util.ArrayList<SearchBean.DataBean>()
    private var noData: View? = null
    private var noWeb: View? = null
    var adapter: SearchAdapter? = null
    var page = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        val id = intent.getStringExtra("id")
        val type = intent.getIntExtra("type", 0)
        val start = intent.getStringExtra("start")
        val end = intent.getStringExtra("end")
        if(type==1){
            address.text = "充值明细"
        }else{
            address.text = "消费明细"
        }
        close.setOnClickListener { finish() }
        noData = utils.getView(R.layout.no_data)
        noWeb = utils.getView(R.layout.view_no_web)
        adapter = SearchAdapter(this@SearchResult, dataList, R.layout.money_item, type)
        listView.adapter = adapter
        refresh.setOnRefreshListener {
            page = 1
            getList(id, "" + type, start, end)
        }
        refresh.setOnLoadMoreListener { getList(id, "" + type, start, end) }
        refresh.autoRefresh()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getList(id: String, type: String, start: String, end: String) {
        val encrypt = AES.Encrypt(id, "qaz14789wsxedcrf")
        HttpRequestPort.instance.search(encrypt, "" + type,
                "$start 00:00:00", "$end 23:59:59", "" + page, "10",
                object : BaseHttpCallBack(this) {
                    @SuppressLint("SetTextI18n")
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<SearchBean>() {})
                        if (bean.code == "10000") {
                            no_web.visibility = View.GONE
                            if (bean.data.size > 0) {
                                no_data.visibility = View.GONE
                                if (page == 1) {
                                    dataList.clear()
                                }
                                dataList.addAll(bean.data)
                                adapter!!.notifyDataSetChanged()
                                page++
                            }
                        }else{
                            if(page==1){
                                no_data.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onError(throwable: Throwable, b: Boolean) {
                        super.onError(throwable, b)
                        LogUtils.i(throwable.toString())
                        no_data.visibility = View.GONE
                        no_web.visibility = View.VISIBLE
                    }

                    override fun onFinished() {
                        super.onFinished()
                        if (refresh.isRefreshing) {
                            refresh.finishRefresh()
                        } else {
                            refresh.finishLoadMore()
                        }
                    }
                })
    }
}