package card.com.allcard.activity

import android.annotation.SuppressLint
import card.com.allcard.R
import card.com.allcard.bean.JmBeam
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_card_one.*

class CardOne : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_card_one

    override fun initView() {
        utils.changeStatusBlack(false, window)
        close.setOnClickListener { finish() }
        jm.setOnClickListener { jm()  }
        fjm.setOnClickListener { startActivity<CardFjmInfo>() }
    }

        private fun jm() {
            utils.getProgress(this)
            val userId = mk.decodeString(Tool.USER_ID, "")
            HttpRequestPort.instance.smCardList(userId, object : BaseHttpCallBack(this) {
                @SuppressLint("SetTextI18n")
                override fun success(data: String) {
                    super.success(data)
                    val bean = JSONObject.parseObject(data, object : TypeReference<JmBeam>() {})
                    if ("0" == bean.result) {
                        if(bean.cardsList.size>0){
                            startActivity<CardJmInfo>()
                        }else{
                            utils.showToast("您的账户当前未绑定记名市民卡")
                        }
                    } else {
                        utils.showToast(bean.message)
                    }
                }

                override fun onError(throwable: Throwable, b: Boolean) {
                    super.onError(throwable, b)
                    utils.showToast("请求失败，请稍后重试")
                }

                override fun onFinished() {
                    super.onFinished()
                    utils.hindProgress()
                }
            })
        }

}
