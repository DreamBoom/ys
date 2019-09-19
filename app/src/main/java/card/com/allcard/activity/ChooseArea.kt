package card.com.allcard.activity

import android.support.design.widget.TabLayout
import android.view.View
import card.com.allcard.R
import card.com.allcard.adapter.QuAdapter
import card.com.allcard.adapter.ShengAdapter
import card.com.allcard.adapter.ShiAdapter
import card.com.allcard.bean.AreaBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_choose_area.*
import kotlinx.android.synthetic.main.title.*

class ChooseArea : BaseActivity(), TabLayout.OnTabSelectedListener {
    override fun layoutId(): Int = R.layout.activity_choose_area

    var shengList = mutableListOf<AreaBean.ArealistBean>()
    var shiList = mutableListOf<AreaBean.ArealistBean>()
    var quList = mutableListOf<AreaBean.ArealistBean>()
    var shengAdapter: ShengAdapter? = null
    var shiAdapter: ShiAdapter? = null
    var quAdapter: QuAdapter? = null
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address.text = "选择省"
        sheng.visibility = View.VISIBLE
        shi.visibility = View.GONE
        qu.visibility = View.GONE
        shengAdapter = ShengAdapter(this, this, shengList, R.layout.pop_item)
        shiAdapter = ShiAdapter(this, this, shiList, R.layout.pop_item)
        quAdapter = QuAdapter(this, this, quList, R.layout.pop_item)
        sheng.adapter = shengAdapter
        shi.adapter = shiAdapter
        qu.adapter = quAdapter
        close.setOnClickListener {
            Tool.CHOOSE = 1
            finish()
        }
        getSheng()
        tab.setUnboundedRipple(true)//点击的动画
        tab.isTabIndicatorFullWidth = true//下划线指示器的宽度不要填充完
        tab.addTab(tab.newTab().setText("请选择"))
        tab.tabMode = TabLayout.MODE_FIXED
        tab.tabGravity = TabLayout.GRAVITY_FILL
        tab.getTabAt(0)!!.select()//默认选中
        tab.addOnTabSelectedListener(this)
        delete.setOnClickListener {
            mk.encode(Tool.chooseArea, "全部")
            mk.encode(Tool.Area_ID, "")
            when (tab.tabCount) {
                2 -> {
                    tab.getTabAt(0)!!.text = "请选择"
                    tab.removeTab(tab.getTabAt(1))
                    tab.getTabAt(0)!!.select()
                    shi.visibility = View.GONE
                    sheng.visibility = View.VISIBLE
                    getSheng()
                }
                3 -> {
                    tab.getTabAt(0)!!.text = "请选择"
                    tab.removeTab(tab.getTabAt(2))
                    tab.removeTab(tab.getTabAt(1))
                    tab.getTabAt(0)!!.select()
                    shi.visibility = View.GONE
                    qu.visibility = View.GONE
                    sheng.visibility = View.VISIBLE
                    getSheng()
                }
            }
        }
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(p0: TabLayout.Tab?) {

    }

    fun hide1(name: String) {
        address.text = "选择市"
        sheng.visibility = View.GONE
        shi.visibility = View.VISIBLE
        qu.visibility = View.GONE
        getShi(name)

    }

    fun hide2(name: String) {
        address.text = "选择区"
        sheng.visibility = View.GONE
        shi.visibility = View.GONE
        qu.visibility = View.VISIBLE
        getQu(name)
    }

    fun hide3() {
        Tool.CHOOSE = 1
        finish()
    }

    private fun getSheng() {
        utils.getProgress(this)
        HttpRequestPort.instance.getArea("1111111111", "2", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val areaBean = JSONObject.parseObject(data, object : TypeReference<AreaBean>() {})
                val arealist = areaBean.arealist
                val arealistBean = AreaBean.ArealistBean()
                arealistBean.area_name = "全部"
                arealistBean.area_id = "1024"
                shengList.add(arealistBean)
                shengList.addAll(arealist)
                shengAdapter!!.notifyDataSetChanged()
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    private fun getShi(name: String) {
        utils.getProgress(this)
        HttpRequestPort.instance.getArea(shengAdapter!!.shengId, "3", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val areaBean = JSONObject.parseObject(data, object : TypeReference<AreaBean>() {})
                val arealist = areaBean.arealist
                shiList.addAll(arealist)
                shiAdapter!!.notifyDataSetChanged()
                tab.getTabAt(0)!!.text = "  $name  "
                tab.addTab(tab.newTab().setText("请选择"))
                tab.getTabAt(1)!!.select()
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    private fun getQu(name: String) {
        utils.getProgress(this)
        HttpRequestPort.instance.getArea(shiAdapter!!.shiId, "4", object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val areaBean = JSONObject.parseObject(data, object : TypeReference<AreaBean>() {

                })
                val arealist = areaBean.arealist
                quList.addAll(arealist)
                quAdapter!!.notifyDataSetChanged()
                tab.getTabAt(1)!!.text = "  $name  "
                tab.addTab(tab.newTab().setText("请选择"))
                tab.getTabAt(2)!!.select()
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }
}
