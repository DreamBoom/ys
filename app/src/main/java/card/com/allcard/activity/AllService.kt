package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import card.com.allcard.R
import card.com.allcard.adapter.MoreAdapter
import card.com.allcard.bean.ServiceTypeBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_all_service.*
import java.util.*

class AllService : BaseActivity(), MoreAdapter.AllClickListener {
    override fun layoutId(): Int = R.layout.activity_all_service
    private val dataList = ArrayList<ServiceTypeBean.ListBean>()
    companion object {
        @SuppressLint("StaticFieldLeak")
        var i = 0
    }
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        MyApplication.instance.addActivity(this)
        val bundle = intent.extras
         i = bundle!!.getInt("tab",0)
        HttpRequestPort.instance.manageType(object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<ServiceTypeBean>() {})
                if (bean.result == "0") {
                    dataList.clear()
                    val listBean = ServiceTypeBean.ListBean()
                    listBean.para_key = "全部"
                    dataList.add(listBean)
                    dataList.addAll(bean.list)
                    val gridAdapter = MoreAdapter(this@AllService, dataList, R.layout.more_type_item)
                    gridAdapter.allClick = this@AllService
                    grid.adapter = gridAdapter
                    gridAdapter.notifyDataSetChanged()
                }
            }
            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("请求失败")
            }
        })

        close.setOnClickListener {
            finish()
        }
    }

    override fun onClickListener(i: Int) {
        val intent = Intent()
        intent.putExtra("tab", i)
        setResult(Tool.RESULTCODE_SUCCESS, intent)
        finish()
    }
}
