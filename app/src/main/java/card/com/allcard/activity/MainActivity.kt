package card.com.allcard.activity

import android.annotation.SuppressLint
import android.app.TabActivity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TabHost
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.DataCleanTools
import card.com.allcard.tools.Tool
import card.com.allcard.utils.ActivityUtils
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.buttom_bar.*

/**
 * @author Created by Dream
 * Email：499276615@qq.com
 * Tab页
 */

class MainActivity : TabActivity() {
    var mk: MMKV? = null
    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: MainActivity? = null
        @SuppressLint("StaticFieldLeak")
        var tab: TabHost? = null
    }

    private var firstTime: Long = 0
    private var utils = ActivityUtils(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyApplication.instance.addActivity(this)
        mk = BaseActivity.mk
        instance = this
        tab = tabhost
        initView()
    }

    private fun initView() {
        //  BadgeUtil.setBadgeCount(this,1,R.drawable.icon_back)
        tab!!.addTab(tab!!.newTabSpec("one").setIndicator("首页").setContent(Intent(this, TabOne::class.java)))
        tab!!.addTab(tab!!.newTabSpec("two").setIndicator("服务").setContent(Intent(this, TabTwo::class.java)))
        tab!!.addTab(tab!!.newTabSpec("three").setIndicator("网点").setContent(Intent(this, TabThree::class.java)))
        tab!!.addTab(tab!!.newTabSpec("four").setIndicator("我的").setContent(Intent(this, TabFourActivity::class.java)))
        tab!!.setOnTabChangedListener { s ->
            mk!!.encode(Tool.BY_LOGIN, "0")
            setIconBackGround()
            when (s) {
                Tool.one -> {
                    im_one!!.setBackgroundResource(R.drawable.icon_db_01_s)
                    tv_one!!.setTextColor(resources.getColor(R.color.blueTop))
                }
                Tool.two -> {
                    im_two!!.setBackgroundResource(R.drawable.icon_db_02_s)
                    tv_two!!.setTextColor(resources.getColor(R.color.blueTop))
                }
                Tool.three -> {
                    im_three!!.setBackgroundResource(R.drawable.icon_db_03_s)
                    tv_three!!.setTextColor(resources.getColor(R.color.blueTop))
                }
                Tool.four -> {
                    im_four!!.setBackgroundResource(R.drawable.icon_db_04_s)
                    tv_four!!.setTextColor(resources.getColor(R.color.blueTop))
                }
            }
        }

        rb_home.setOnClickListener {
            tabhost!!.setCurrentTabByTag("one")
        }
        rb_service.setOnClickListener {
            tabhost!!.setCurrentTabByTag("two")
        }
        rb_site.setOnClickListener {
            tabhost!!.setCurrentTabByTag("three")
        }
        rb_user.setOnClickListener {
            tabhost!!.setCurrentTabByTag("four")
        }
    }

    private fun setIconBackGround() {
        im_one!!.setBackgroundResource(R.drawable.icon_db_01)
        tv_one!!.setTextColor(resources.getColor(R.color.text_gray))
        im_two!!.setBackgroundResource(R.drawable.icon_db_02)
        tv_two!!.setTextColor(resources.getColor(R.color.text_gray))
        im_three!!.setBackgroundResource(R.drawable.icon_db_03)
        tv_three!!.setTextColor(resources.getColor(R.color.text_gray))
        im_four!!.setBackgroundResource(R.drawable.icon_db_04)
        tv_four!!.setTextColor(resources.getColor(R.color.text_gray))
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_BACK) {
            val secondTime = System.currentTimeMillis()
            if (secondTime - firstTime > 2000) {
                utils.showToast("再按一次退出程序")
                firstTime = secondTime
                return false
            } else {
                MyApplication.instance.removeAllActivity()
                DataCleanTools.cleanApplicationData(this)
                System.exit(0)
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
