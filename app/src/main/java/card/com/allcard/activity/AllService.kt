package card.com.allcard.activity

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import card.com.allcard.R
import card.com.allcard.tools.Tool
import com.gw.swipeback.SwipeBackLayout
import kotlinx.android.synthetic.main.activity_all_service.*

class AllService : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_all_service

    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        val mSwipeBackLayout = findViewById<View>(R.id.swipeBackLayout) as SwipeBackLayout
        mSwipeBackLayout.directionMode = SwipeBackLayout.FROM_BOTTOM
        mSwipeBackLayout.maskAlpha = 125
        mSwipeBackLayout.swipeBackFactor = 0.5f
        mSwipeBackLayout.setSwipeBackListener(object : SwipeBackLayout.OnSwipeBackListener {
            override fun onViewPositionChanged(mView: View, swipeBackFraction: Float, SWIPE_BACK_FACTOR: Float) {

            }

            override fun onViewSwipeFinished(mView: View, isEnd: Boolean) {
                if(isEnd){
                    finish()
                }
            }
        })

        val bundle = intent.extras
        val i = bundle.getInt("tab")
        when (i) {
            0 -> tab1.setTextColor(ContextCompat.getColor(this, R.color.blue))
            1 -> tab2.setTextColor(ContextCompat.getColor(this, R.color.blue))
            2 -> tab3.setTextColor(ContextCompat.getColor(this, R.color.blue))
            3 -> tab4.setTextColor(ContextCompat.getColor(this, R.color.blue))
            4 -> tab5.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }
        close.setOnClickListener {
            finish()
        }
        tab1.setOnClickListener {
            val intent = Intent()
            intent.putExtra("tab", 0)
            setResult(Tool.RESULTCODE_SUCCESS, intent)
            finish()
        }
        tab2.setOnClickListener {
            val intent = Intent()
            intent.putExtra("tab", 1)
            setResult(Tool.RESULTCODE_SUCCESS, intent)
            finish()
        }
        tab3.setOnClickListener {
            val intent = Intent()
            intent.putExtra("tab", 2)
            setResult(Tool.RESULTCODE_SUCCESS, intent)
            finish()
        }
        tab4.setOnClickListener {
            val intent = Intent()
            intent.putExtra("tab", 3)
            setResult(Tool.RESULTCODE_SUCCESS, intent)
            finish()
        }
        tab5.setOnClickListener {
            val intent = Intent()
            intent.putExtra("tab", 4)
            setResult(Tool.RESULTCODE_SUCCESS, intent)
            finish()
        }
    }

    override fun finish() {
        super.finish()
        //overridePendingTransition(R.anim.activity_close, R.anim.activity_close)
    }
}
