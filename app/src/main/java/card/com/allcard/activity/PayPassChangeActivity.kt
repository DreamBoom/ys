package card.com.allcard.activity

import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.View
import card.com.allcard.R
import card.com.allcard.tools.Tool
import com.xnumberkeyboard.android.KeyboardType
import com.xnumberkeyboard.android.OnNumberKeyboardListener
import com.xnumberkeyboard.android.XNumberKeyboardView
import kotlinx.android.synthetic.main.activity_pay_pass_change.*
import kotlinx.android.synthetic.main.title.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class PayPassChangeActivity : BaseActivity(), OnNumberKeyboardListener {
    override fun layoutId(): Int = R.layout.activity_pay_pass_change
    private var i = 0
    var payPass = ""
    var setPass1 = ""
    var setPass2 = ""
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        close.setOnClickListener { finish() }
        keyboard.setOnNumberKeyboardListener(this)
        keyboard.setKeyboardType(KeyboardType.number)
        keyboard.setSpecialKeyBackground(ColorDrawable(resources.getColor(R.color.colorKeyBlank)))
        keyboard.shuffleKeyboard()
        payPass = mk.decodeString(Tool.payPass, "")
        utils.showToast(payPass)
        if (TextUtils.isEmpty(payPass)) {
            address.text = "设置交易密码"
            tv_t1.text = "请设置6位交易密码"
            tv_t2.visibility = View.VISIBLE
        } else {
            address.text = "修改交易密码"
            tv_t1.text = "请输入旧密码"
            tv_t2.visibility = View.INVISIBLE
        }
    }

    override fun onNumberKey(keyCode: Int, insert: String?) {
        // 右下角按键的点击事件，删除一位输入的文字
        if (keyCode === XNumberKeyboardView.KEYCODE_BOTTOM_RIGHT) {
            when (i) {
                1 -> {
                    i -= 1
                    if (setPass2.isNotEmpty()) {
                        setPass2 = ""
                    } else {
                        setPass1 = ""
                    }
                    et1.setText("".toCharArray(), 0, "".length)
                }
                2 -> {
                    i -= 1
                    if (setPass2.isNotEmpty()) {
                        setPass2 = setPass2.substring(0, setPass2.length - 1)
                    } else {
                        setPass1 = setPass1.substring(0, setPass1.length - 1)
                    }
                    et2.setText("".toCharArray(), 0, "".length)
                }
                3 -> {
                    i -= 1
                    if (setPass2.isNotEmpty()) {
                        setPass2 = setPass2.substring(0, setPass2.length - 1)
                    } else {
                        setPass1 = setPass1.substring(0, setPass1.length - 1)
                    }
                    et3.setText("".toCharArray(), 0, "".length)
                }
                4 -> {
                    i -= 1
                    if (setPass2.isNotEmpty()) {
                        setPass2 = setPass2.substring(0, setPass2.length - 1)
                    } else {
                        setPass1 = setPass1.substring(0, setPass1.length - 1)
                    }
                    et4.setText("".toCharArray(), 0, "".length)
                }
                5 -> {
                    i -= 1
                    if (setPass2.isNotEmpty()) {
                        setPass2 = setPass2.substring(0, setPass2.length - 1)
                    } else {
                        setPass1 = setPass1.substring(0, setPass1.length - 1)
                    }
                    et5.setText("".toCharArray(), 0, "".length)
                }
                6 -> {
                    i -= 1
                    if (setPass2.isNotEmpty()) {
                        setPass2 = setPass2.substring(0, setPass2.length - 1)
                    } else {
                        setPass1 = setPass1.substring(0, setPass1.length - 1)
                    }
                    et6.setText("".toCharArray(), 0, "".length)
                }
                else -> utils.showToast("请重新输入")
            }
        } else {
            if (i < 6) {
                if (setPass1.length < 6) {
                    setPass1 += insert.toString()
                } else {
                    setPass2 += insert.toString()
                }
                i++
            } else {
                utils.showToast("最多输入6位")
            }
            when (i) {
                1 -> et1.append(insert)
                2 -> et2.append(insert)
                3 -> et3.append(insert)
                4 -> et4.append(insert)
                5 -> et5.append(insert)
                6 -> {
                    et6.append(insert)
                    i = 0
                    if (TextUtils.isEmpty(payPass)) {
                        //设置交易密码
                        if (setPass2.length > 5) {
                            if (setPass1 == setPass2) {
                                mk.encode(Tool.payPass, setPass2)
                                utils.showToast("设置成功")
                                finish()
                            } else {
                                clear()
                                setPass2 = ""
                                utils.showToast("密码输入不一致，请重新输入")
                            }
                        } else {
                            tv_t1.text = "请再次确认6位数字交易密码"
                            clear()
                        }
                    }else{
                        val p = mk.decodeString(Tool.payPass, "")
                        if(setPass1 == p){
                            mk.encode(Tool.payPass, "")
                            setPass1 =""
                            clear()
                            address.text = "设置交易密码"
                            tv_t1.text = "请设置6位交易密码"
                            tv_t2.visibility = View.VISIBLE
                        }else{
                            clear()
                            setPass1 = ""
                            setPass2 = ""
                            utils.showToast("密码输入不一致，请重新输入")
                        }
                    }
                }
            }

        }// 左下角按键和数字按键的点击事件，输入文字
    }

    fun clear() {
        et1.setText("".toCharArray(), 0, "".length)
        et2.setText("".toCharArray(), 0, "".length)
        et3.setText("".toCharArray(), 0, "".length)
        et4.setText("".toCharArray(), 0, "".length)
        et5.setText("".toCharArray(), 0, "".length)
        et6.setText("".toCharArray(), 0, "".length)
    }
}
