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
import card.com.allcard.utils.LogUtils
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_tab_one.*
import org.xutils.x

class TabOne : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    override fun layoutId(): Int = R.layout.activity_tab_one
    private var detail = arrayListOf<MainImgBean.SummarydetailBean>()
    private var serviceGuide = mutableListOf<ServiceListBean.ListBean>()
    private var adapter: NewListAdapter? = null
    private var imgAdapter: ImgAdapter? = null
    var userId = ""
    override fun initView() {
        adapter = NewListAdapter(this, R.layout.news_list_item, serviceGuide)
        utils.changeStatusBlack(false, window)
        refresh.setEnableOverScrollDrag(false)
        refresh.setOnRefreshListener {
            refresh.finishRefresh()
            initData()
        }
        pull_view.layoutManager = LinearLayoutManager(this@TabOne)
        //动画效果
        // adapter!!.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        pull_view.adapter = adapter
        service_more.setOnClickListener {
            startActivity<MoreServiceActivity>()
        }

//        wx.setOnClickListener {
//            val appId = "wx1467bf6a8cf0dffd" // 填应用AppId
//
//            val api = WXAPIFactory.createWXAPI(this@TabOne, appId)
//            val req = WXLaunchMiniProgram.Req()
//            req.userName = "gh_d43f693ca31f" // 填小程序原始id
//
//            //req.path = path;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//            req.path = "/pages/login/login" ;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST // 可选打开 开发版，体验版和正式版
//            api.sendReq(req)
//        }

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
                LogUtils.i(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<MainImgBean>() {})
                //设置轮播图
                if (detail.size == 0) {
                    x.image().bind(kar, bean.image[0])
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
                LogUtils.i(throwable.toString())
                //网络加载失败，设置默认图片
                kar.setImageDrawable(ContextCompat.getDrawable(this@TabOne, R.drawable.banner))
            }

            override fun onFinished() {
                super.onFinished()
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
                    setData(null)
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                setData(null)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        userId = mk.decodeString(Tool.USER_ID, "")
        initData()
    }


    private fun setData(data: List<ServiceListBean.ListBean>?) {
        if (adapter!!.headerLayout != null) {
            adapter!!.removeAllHeaderView()
        }
        adapter!!.setNewData(data)
        when {
            data == null -> adapter!!.addHeaderView(utils.getView(this, R.layout.tab_no_web))
            data.size < 1 -> adapter!!.addHeaderView(utils.getView(this, R.layout.no_data))
        }
    }
}
