package card.com.allcard.activity

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.PayAdapter
import card.com.allcard.bean.GetNum
import card.com.allcard.bean.PayListBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.KeyBoardListener
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import kotlinx.android.synthetic.main.activity_pay.*
import kotlinx.android.synthetic.main.payadd_item.*
import kotlinx.android.synthetic.main.title.*

class PayActivity : BaseActivity(), PayAdapter.ClickListener {
    override fun layoutId(): Int = R.layout.activity_pay
    @SuppressLint("SetJavaScriptEnabled")
    var dataList = arrayListOf<PayListBean.MemberlinkListBean>()
    var payAdapter: PayAdapter? = null
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        KeyBoardListener.getInstance(this).init()
        MyApplication.instance.addActivity(this)
        address.text = "家庭管理"
        close.setOnClickListener { finish() }
        payAdapter = PayAdapter(this, dataList, R.layout.pay_item)
        right_menu.visibility = View.VISIBLE
        right_menu.text = "+新增"
        right_menu.textSize = 13f
        right_menu.setOnClickListener {
            // list.setSelection(list.bottom)
            list.smoothScrollToPosition(list.bottom)
            // list.smoothScrollBy()
            list.isEnabled = false
            addItem.isFocusable = true
            addItem.isFocusableInTouchMode = true
            addItem.requestFocus()
            addItem.visibility = View.VISIBLE
            no_data.visibility = View.GONE
        }

        add_sure.setOnClickListener {
            utils.hideSoftKeyboard()
            val name = et_add_name.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                utils.showToast("请输入昵称")
            } else {
                list.isEnabled = true
                et_add_name.setText("".toCharArray(), 0, "".length)
                add(name)
            }
        }

        add_cancel.setOnClickListener {
            utils.hideSoftKeyboard()
            list.isEnabled = true
            addItem.visibility = View.GONE
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
        list.setOnMenuItemClickListener { position, menu, index ->
            when (index) {
                0 -> {
                    delete(dataList[position].nickName)
                }
            }
            false
        }
        payAdapter!!.setClickListener(this)
        list.adapter = payAdapter
        refresh.setEnableOverScrollDrag(false)
        refresh.setHeaderMaxDragRate(2.0f)
        refresh.setOnRefreshListener { refresh ->
            refresh.finishRefresh()
            initData()
        }
        initData()
    }

    private fun initData() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.linkList(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                dataList.clear()
                val bean = JSONObject.parseObject(data, object : TypeReference<PayListBean>() {})
                if (bean.result == "0") {
                    if (bean.memberlinkList.size > 0) {
                        no_data.visibility = View.GONE
                        no_web.visibility = View.GONE
                        dataList.addAll(bean.memberlinkList)
                    } else {
                        no_web.visibility = View.GONE
                        no_data.visibility = View.VISIBLE
                    }
                    payAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("请求失败，下拉重试")
                no_data.visibility = View.GONE
                no_web.visibility = View.VISIBLE
            }
        })
    }

    private fun add(newName: String) {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.upName(userId, newName, "", "0", object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    initData()
                    addItem.visibility = View.GONE
                } else {
                    utils.showToast(bean.message)
                }
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    override fun upName() {
        initData()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }


    private fun delete(name: String) {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.deleteName(userId, name, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                if (bean.result == "0") {
                    initData()
                } else {
                    utils.showToast(bean.message)
                }
            }
        })
    }

    override fun onClickListener(position: Int, name: String?) {
        dataList[position].nickName = name!!
        payAdapter!!.notifyDataSetChanged()
    }
}
