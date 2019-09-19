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
import card.com.allcard.fragment.*
import card.com.allcard.tools.Tool
import card.com.allcard.tools.Tool.RESULTCODE_SERVICE
import kotlinx.android.synthetic.main.activity_more_service.*

class MoreServiceActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener,TabLayout.OnTabSelectedListener {
    override fun layoutId(): Int = R.layout.activity_more_service
    private var fragments: MutableList<Fragment> = mutableListOf(
            Service1(), Service2(), Service3(), Service4(), Service5())
    private val adapt = TabPagerAdapter(supportFragmentManager, fragments)
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        tab.setUnboundedRipple(true)//点击的动画
        tab.isTabIndicatorFullWidth = true//下划线指示器的宽度不要填充完
        tab.addTab(tab.newTab().setText("全部"))
        tab.addTab(tab.newTab().setText("社保卡办理流程"))
        tab.addTab(tab.newTab().setText("使用说明"))
        tab.addTab(tab.newTab().setText("政策法规"))
        tab.addTab(tab.newTab().setText("热点问答           "))
        tab.tabMode = TabLayout.MODE_SCROLLABLE
        tab.tabGravity = TabLayout.GRAVITY_FILL
        tab.getTabAt(0)!!.select()//默认选中
        tab.addOnTabSelectedListener(this)
        viewpager.setPageTransformer(true, DepthPageTransformer())
        viewpager.adapter = adapt
        viewpager.addOnPageChangeListener(this)
        all.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("tab",tab.selectedTabPosition)
            utils.toStartActivityForResult(AllService::class.java,RESULTCODE_SERVICE,bundle)
            overridePendingTransition(R.anim.activity_open,R.anim.activity_close)
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
            when (viewpager.currentItem) {
                0 ->  tab.getTabAt(0)!!.select()
                1 ->  tab.getTabAt(1)!!.select()
                2 ->  tab.getTabAt(2)!!.select()
                3 ->  tab.getTabAt(3)!!.select()
                4 ->  tab.getTabAt(4)!!.select()
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
                when(i){
                    0 -> {
                        tab.getTabAt(0)!!.select()
                    }
                    1 -> {
                        tab.getTabAt(1)!!.select()
                    }
                    2 -> {
                        tab.getTabAt(2)!!.select()
                    }
                    3 -> {
                        tab.getTabAt(3)!!.select()
                    }
                    4 -> {
                        tab.getTabAt(4)!!.select()
                    }
                }
            }
        }
    }
}
