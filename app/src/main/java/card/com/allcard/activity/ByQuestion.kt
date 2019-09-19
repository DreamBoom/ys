package card.com.allcard.activity

import android.text.TextUtils
import card.com.allcard.R
import card.com.allcard.bean.AnswerBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_by_question.*
import kotlinx.android.synthetic.main.title.*

class ByQuestion : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_by_question
    var d1 = ""
    var d2 = ""
    var d3 = ""
    override fun initView() {
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        MyApplication.instance.addActivity(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        address.text = "密保问题验证"
        next.setOnClickListener { check() }
        getAns()
        forgetQuestion.setOnClickListener {
            startActivity<ChangeQuestion>()
            finish()
        }
    }

    private fun check() {
        val tvA1 = a1.text.toString().trim()
        val tvA2 = a2.text.toString().trim()
        val tvA3 = a3.text.toString().trim()
        when {
            TextUtils.isEmpty(tvA1) -> utils.showToast("请输入问题一的答案")
            TextUtils.isEmpty(tvA2) -> utils.showToast("请输入问题二的答案")
            TextUtils.isEmpty(tvA3) -> utils.showToast("请输入问题三的答案")
            else -> {
                when {
                    tvA1 != d1 -> utils.showToast("答案输入错误")
                    tvA2 != d2 -> utils.showToast("答案输入错误")
                    tvA3 != d3 -> utils.showToast("答案输入错误")
                    else -> {
                        startActivity<ByPhone2>()
                        finish()
                    }
                }
            }
        }
    }

    private fun getAns() {
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.selectAnswer(userId, object : BaseHttpCallBack(this) {
            override fun onSuccess(s: String) {
                super.onSuccess(s)
                val bean = JSONObject.parseObject(s, object : TypeReference<AnswerBean>() {})
                if (bean.status == "0") {
                    q1.text = bean.answerlist[0].remark
                    q2.text = bean.answerlist[1].remark
                    q3.text = bean.answerlist[2].remark
                    d1 = bean.answerlist[0].answer
                    d2 = bean.answerlist[1].answer
                    d3 = bean.answerlist[2].answer
                }
            }
        })
    }
}
