package card.com.allcard.activity

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.adapter.ImgAdapter
import card.com.allcard.adapter.NewListAdapter
import card.com.allcard.bean.MainImgBean
import card.com.allcard.bean.ServiceListBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MyNetUtils
import card.com.allcard.view.MyGridView
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_tab_one.*
import org.xutils.image.ImageOptions
import org.xutils.x

class TabOne : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_tab_one
    private var detail = arrayListOf<MainImgBean.SummarydetailBean>()
    private var serviceGuide = mutableListOf<ServiceListBean.ListBean>()
    private var adapter: NewListAdapter? = null
    private var im_info: MyGridView? = null
    private var serviceMore: TextView? = null
    private var imgAdapter: ImgAdapter? = null
    private var kar: ImageView? = null
    private var options: ImageOptions? = null
    var userId = ""
    override fun initView() {
        adapter = NewListAdapter(this,R.layout.news_list_item,serviceGuide)
        utils.changeStatusBlack(false, window)
        lv_list.adapter = adapter
        lv_list.layoutManager = LinearLayoutManager(this@TabOne)
        refresh.setOnRefreshListener { refresh ->
            refresh.finishRefresh()
            //重新刷新页面
            if (MyNetUtils.isNetworkConnected(this@TabOne)) {
                initData()
            } else {
                utils.showToast("请检查是否未连接网络,或连接的网络未登录")
            }
        }
        adapter!!.addHeaderView(addHeadView())
       // refresh.setEnableOverScrollDrag(true)
        options =  ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.banner)
                .setFailureDrawableId(R.drawable.banner)
                .setUseMemCache(true)
                .setIgnoreGif(false)
                .setCircular(false).build()
    }

    private fun addHeadView(): View {
       val mHeader = LayoutInflater.from(this).inflate(R.layout.main_head, RelativeLayout(this))
        im_info = mHeader.findViewById(R.id.im_info)
        kar = mHeader.findViewById(R.id.kar)
        serviceMore = mHeader.findViewById(R.id.service_more)
        serviceMore!!.setOnClickListener { startActivity<MoreServiceActivity>() }
        return mHeader
    }

    private fun initData(){
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.mainImg(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                val bean = JSONObject.parseObject(data, object : TypeReference<MainImgBean>() {})
                //设置轮播图
                if(detail.size==0){
                    x.image().bind(kar,bean.image[0], options)
                }
                //设置图标
                val summarydetail = bean.summarydetail
                detail.clear()
                detail.addAll(summarydetail)
                //设置图片数组
                imgAdapter = ImgAdapter(this@TabOne, detail, R.layout.img_item)
                im_info!!.adapter = imgAdapter
                imgAdapter!!.notifyDataSetChanged()
                initData2()
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                //网络加载失败，设置默认图片
                x.image().bind(kar,"", options)
                //serviceMore!!.visibility = View.GONE
            }
        })
    }

    private fun initData2(){
        HttpRequestPort.instance.serviceList1("", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<ServiceListBean>() {})
                //设置服务指南
                val service = bean.list
                serviceGuide.clear()
                serviceGuide.addAll(service)
                adapter!!.notifyDataSetChanged()
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)

            }

            override fun onFinished() {
                super.onFinished()
            }
        })
    }


    override fun onResume() {
        super.onResume()
        userId = mk.decodeString(Tool.USER_ID, "")
        runDelayed(200){
            initData()
        }
    }
}
