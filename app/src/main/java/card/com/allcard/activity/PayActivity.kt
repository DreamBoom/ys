package card.com.allcard.activity

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.PayAdapter
import card.com.allcard.getActivity.MyApplication
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import kotlinx.android.synthetic.main.activity_pay.*
import kotlinx.android.synthetic.main.title.*


class PayActivity : BaseActivity(), PayAdapter.ClickListener {
    override fun layoutId(): Int = R.layout.activity_pay
    private var userId: String? = null
    @SuppressLint("SetJavaScriptEnabled")
    var dataList = arrayListOf<String>()
    var payAdapter : PayAdapter?=null
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        MyApplication.instance.addActivity(this)
        address.text = "家庭管理"
        close.setOnClickListener { finish() }
        dataList.add("老爸")
        dataList.add("老妈")
        dataList.add("朋友")
        payAdapter = PayAdapter(this, dataList, R.layout.pay_item)
        right_menu.visibility = View.VISIBLE
        right_menu.text = "+新增"
        right_menu.setOnClickListener {
            dataList.add("姓名")
            payAdapter!!.notifyDataSetChanged()
        }
        //设置左滑菜单
        val creator = SwipeMenuCreator { menu ->
            // create "delete" item
            val deleteItem = SwipeMenuItem(
                    applicationContext)
            deleteItem.background = ContextCompat.getDrawable(this, R.drawable.bg_red)
            deleteItem.width = 170
            deleteItem.title = "删除"
            deleteItem.titleColor = ContextCompat.getColor(this, R.color.red)
            deleteItem.titleSize = 15
            menu.addMenuItem(deleteItem)
        }
        list.setMenuCreator(creator)
        payAdapter!!.setClickListener(this)
        list.adapter = payAdapter
        refresh.setEnableOverScrollDrag(false)
        refresh.setOnRefreshListener { refresh ->
            refresh.finishRefresh()
            //重新刷新页面

        }
    }

    override fun onClickListener(position: Int, name: String?) {
        dataList[position] = name!!
        payAdapter!!.notifyDataSetChanged()
    }
}
