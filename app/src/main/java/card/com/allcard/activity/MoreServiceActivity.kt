package card.com.allcard.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.widget.RadioGroup
import card.com.allcard.R
import card.com.allcard.adapter.DepthPageTransformer
import card.com.allcard.adapter.TabPagerAdapter
import card.com.allcard.bean.ServiceTypeBean
import card.com.allcard.fragment.Service1
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.tools.Tool.RESULTCODE_SERVICE
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_more_service.*

class MoreServiceActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {
    override fun layoutId(): Int = R.layout.activity_more_service
    private var fragments: MutableList<Fragment> = mutableListOf()
    private val adapt = TabPagerAdapter(supportFragmentManager, fragments)
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }

        HttpRequestPort.instance.manageType(object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<ServiceTypeBean>() {})
                if (bean.result == "0") {
                    tab.setUnboundedRipple(true)//点击的动画
                    tab.isTabIndicatorFullWidth = true//下划线指示器的宽度不要填充完
                    fragments.add(Service1())
                    tab.addTab(tab.newTab().setText("全部"))
                    for (i in 0 until bean.list.size) {
                        fragments.add(Service1())
                        tab.addTab(tab.newTab().setText(bean.list[i].para_key))
                    }
                    tab.tabMode = TabLayout.MODE_SCROLLABLE
                    tab.tabGravity = TabLayout.GRAVITY_FILL
                    tab.getTabAt(0)!!.select()//默认选中
                    tab.addOnTabSelectedListener(this@MoreServiceActivity)
                    viewpager.setPageTransformer(true, DepthPageTransformer())
                    viewpager.adapter = adapt
                    adapt.notifyDataSetChanged()
                    viewpager.addOnPageChangeListener(this@MoreServiceActivity)                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("请求失败")
            }
        })

        all.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("tab", tab.selectedTabPosition)
            utils.toStartActivityForResult(AllService::class.java, RESULTCODE_SERVICE, bundle)
        }
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewpager.currentItem = tab!!.position
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {
        if (state == 2) {
         tab.getTabAt(viewpager.currentItem)!!.select()
            mk.encode(Tool.tab,"${viewpager.currentItem}")
            if(viewpager.currentItem == 0){
                mk.encode(Tool.tab,"全部")
            }else{
                mk.encode(Tool.tab,"${viewpager.currentItem}")
            }
        }
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        utils.hideSoftKeyboard()
    }

    override fun onPageSelected(p0: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Tool.RESULTCODE_SUCCESS) {
            return
        }
        when (requestCode) {
            RESULTCODE_SERVICE -> {
                val i = data!!.extras!!.getInt("tab", 0)
                tab.getTabAt(i)!!.select()
            }
        }
    }
}
