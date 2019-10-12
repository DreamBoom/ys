package card.com.allcard.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import card.com.allcard.R
import card.com.allcard.adapter.ImgAdapter
import card.com.allcard.adapter.NewListAdapter
import card.com.allcard.bean.MainImgBean
import card.com.allcard.bean.ServiceListBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.chad.library.adapter.base.BaseQuickAdapter
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_tab_one.*
import org.xutils.image.ImageOptions
import org.xutils.x

class TabOne : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    override fun layoutId(): Int = R.layout.activity_tab_one
    private var detail = arrayListOf<MainImgBean.SummarydetailBean>()
    private var serviceGuide = mutableListOf<ServiceListBean.ListBean>()
    private var adapter: NewListAdapter? = null
    private var imgAdapter: ImgAdapter? = null
    private var options: ImageOptions? = null
    var userId = ""
    override fun initView() {
        adapter = NewListAdapter(this, R.layout.news_list_item, serviceGuide)
        utils.changeStatusBlack(false, window)
        swipeLayout.setOnRefreshListener(this)
        swipeLayout.setColorSchemeResources(R.color.blue)
        pull_view.layoutManager = LinearLayoutManager(this@TabOne)
        //动画效果
        adapter!!.openLoadAnimation(BaseQuickAdapter.EMPTY_VIEW)
        pull_view.adapter = adapter
        initData()
        service_more.setOnClickListener { startActivity<MoreServiceActivity>() }
    }

    override fun onRefresh() {
        Handler().postDelayed({ initData() }, 1000)
    }

    private fun initData() {
        //swipeLayout.isRefreshing = true
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.mainImg(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                val bean = JSONObject.parseObject(data, object : TypeReference<MainImgBean>() {})
                //设置轮播图
                if (detail.size == 0) {
                    x.image().bind(kar, bean.image[0], options)
                }
                //设置图标
                val summarydetail = bean.summarydetail
                detail.clear()
                detail.addAll(summarydetail)
                //设置图片数组
                imgAdapter = ImgAdapter(this@TabOne, detail, R.layout.img_item)
                im_info!!.adapter = imgAdapter
                imgAdapter!!.notifyDataSetChanged()
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                //网络加载失败，设置默认图片
                kar.setImageDrawable(ContextCompat.getDrawable(this@TabOne,R.drawable.banner))
            }

            override fun onFinished() {
                super.onFinished()
                runDelayed(1000) {
                    if (swipeLayout!!.isRefreshing) {
                        swipeLayout!!.isRefreshing = false
                    }
                }
                initData2()
            }
        })
    }

    private fun initData2() {
        HttpRequestPort.instance.serviceList1("", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<ServiceListBean>() {})
                //设置服务指南
                if (bean.result == "0") {
                    setData(bean.list)
                } else {
                    adapter!!.addHeaderView(utils.getView(this@TabOne, R.layout.view_no_web))
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                adapter!!.addHeaderView(utils.getView(this@TabOne, R.layout.view_no_web))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        userId = mk.decodeString(Tool.USER_ID, "")
        initData()
    }

    private fun setData(data: List<ServiceListBean.ListBean>) {
        if (adapter!!.headerLayout != null) {
            adapter!!.removeAllHeaderView()
        }
        adapter!!.setNewData(data)
        when {
            data.size < 1 -> adapter!!.addHeaderView(utils.getView(this, R.layout.no_data))
        }
    }
}
