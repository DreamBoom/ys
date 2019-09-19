package card.com.allcard.activity

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import card.com.allcard.R
import card.com.allcard.adapter.GuidePagerAdapter
import com.pawegio.kandroid.i
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_guide.*
import java.util.*

/**
 * @author Created by Dream
 * 引导页
 */
@Suppress("UNUSED_EXPRESSION")
class GuideActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_guide

    override fun initView() {
        val pagerAdapter = GuidePagerAdapter({ gotoMian() }, this@GuideActivity, guideDrawable)
        vp_guide!!.adapter = pagerAdapter
        vp_guide.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> mPoint.setImageDrawable(ContextCompat.getDrawable(this@GuideActivity,R.drawable.img_lbd1))
                    1 -> mPoint.setImageDrawable(ContextCompat.getDrawable(this@GuideActivity,R.drawable.img_lbd2))
                    2 -> mPoint.setImageDrawable(ContextCompat.getDrawable(this@GuideActivity,R.drawable.img_lbd3))
                }
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                i{""}
            }

            override fun onPageScrollStateChanged(p0: Int) {
                i{""}
            }

        })
    }

    private var isBut = true
    private val guideDrawable: ArrayList<Drawable>
        get() {
            val guideList = ArrayList<Drawable>()
            guideList.add(ContextCompat.getDrawable(this,R.drawable.one)!!)
            guideList.add(ContextCompat.getDrawable(this,R.drawable.two)!!)
            guideList.add(ContextCompat.getDrawable(this,R.drawable.three)!!)
            return guideList
        }

    private fun gotoMian() {
        if (isBut) {
            //用于拦截重复点击的问题
            startActivity<MainActivity>()
            isBut = false
            finish()
        }
    }
}
