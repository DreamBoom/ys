package card.com.allcard.activity

import card.com.allcard.R
import card.com.allcard.adapter.DeviceItemAdapter
import card.com.allcard.bean.DeviceListBean
import card.com.allcard.getActivity.MyApplication
import kotlinx.android.synthetic.main.activity_device.*

class Device : BaseActivity() {
    var adapter:DeviceItemAdapter? = null
    override fun layoutId(): Int = R.layout.activity_device

    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        MyApplication.instance.addActivity(this)
        address.text = "终端登录日志"
        close.setOnClickListener { finish() }
        val list = intent.getSerializableExtra("list")
                as (List<DeviceListBean.DeviceList>)
        adapter = DeviceItemAdapter(this, list, R.layout.device_item)
        listView.adapter = adapter
    }
}





