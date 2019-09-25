package card.com.allcard.activity

import android.annotation.SuppressLint
import android.widget.ImageView
import card.com.allcard.R
import card.com.allcard.bean.YjBean
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_money_of_card.*
import org.xutils.image.ImageOptions
import org.xutils.x

class MoneyOfCard : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_money_of_card
    var m = 0
    @SuppressLint("SetTextI18n")
    override fun initView() {
        utils.changeStatusBlack(false, window)
        close.setOnClickListener { finish() }
        pay.setOnClickListener { startActivity<MoneyIn>()  }
        getMoney.setOnClickListener {  }
        xz.setOnClickListener { startActivity<EdOfMoney>() }
        mx.setOnClickListener { startActivity<MoneyInfo>()  }
        kyj.setOnClickListener { startActivity<MoneyOfCash>() }
        val realName = mk.decodeString(Tool.REAL_NAME, "")
        name.text = realName
        val m = mk.decodeString(Tool.oneMoney, "0")
        money.text = utils.save2(m.toDouble())
        val num0 = mk.decodeString(Tool.USER_NUM, "")
        num.text = num0.substring(0, 3) + "********" +
                num0.substring(num0.length - 4, num0.length)
        val img = mk.decodeString(Tool.HEADER, "")
        val options = ImageOptions.Builder()
                .setSize(300, 300)
                .setCrop(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.ykt_user)
                .setFailureDrawableId(R.drawable.ykt_user)
                .setUseMemCache(true)
                .setIgnoreGif(false)
                .setCircular(true).build()
        x.image().bind(header, img, options)
        //查询卡押金
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.searchYj(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<YjBean>() {})
                val status = bean.result
                if (status == "0") {
                    yj.text = "  ${bean.amt}元"
                    money.text = utils.save2(bean.amt.toDouble())
                }
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    private fun initData() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.searchYuE(userId, object : BaseHttpCallBack(this) {
            @SuppressLint("SetTextI18n")
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<YjBean>() {})
                val status = bean.result
                if (status == "0") {
                    money.text = bean.amt
                    money.text = utils.save2(bean.amt.toDouble())
                }
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initData()
    }
}
