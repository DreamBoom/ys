package card.com.allcard.activity

import android.os.Bundle
import android.text.TextUtils
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_change_question.*
import kotlinx.android.synthetic.main.title.*


class ChangeQuestion : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_change_question
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        MyApplication.instance.addActivity(this)
        utils.changeStatusBlack(true, window)
        address.text = "身份验证"
        val type = intent.getStringExtra("type")
        close.setOnClickListener { finish() }
        bt_next.setOnClickListener {
            val name = et_name.text.toString().trim()
            val num = et_num.text.toString().trim()
            val realName = mk.decodeString(Tool.REAL_NAME,"")
            val userNum = mk.decodeString(Tool.USER_NUM,"")
            val num1 = num.toUpperCase()
            if(type == "0") {
                when {
                    TextUtils.isEmpty(name) -> utils.showToast("请输入姓名")
                    name != realName -> utils.showToast("请输入真实姓名")
                    TextUtils.isEmpty(num) -> utils.showToast("请输入身份证号")
                    num1 != userNum -> utils.showToast("请输入正确身份证号")
                    else -> {
                        startActivity<ChangeQuestion2>()
                        finish()
                    }
                }
            }else{
                when {
                    TextUtils.isEmpty(name) -> utils.showToast("请输入姓名")
                    TextUtils.isEmpty(num) -> utils.showToast("请输入身份证号")
                    else -> {
                        val bundle = Bundle()
                        bundle.putString("name",name)
                        bundle.putString("num",num)
                        utils.startActivityBy(CardProgress::class.java,bundle)
                        finish()
                    }
                }
            }
        }
    }

}
