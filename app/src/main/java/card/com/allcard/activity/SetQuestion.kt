package card.com.allcard.activity

import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.adapter.QuestionAdapter
import card.com.allcard.bean.GetNum
import card.com.allcard.bean.QuestionBean
import card.com.allcard.bean.UpQuestionBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_set_question.*
import kotlinx.android.synthetic.main.title.*

class SetQuestion : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_set_question
    val list = mutableListOf<QuestionBean.BaseListBean>()
    var id0 = ""
    var id1 = ""
    var id2 = ""
    var qu0 = ""
    var qu1 = ""
    var qu2 = ""
    override fun initView() {
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        MyApplication.instance.addActivity(this)
        close.setOnClickListener { finish() }
        val type = intent.getStringExtra("type")
        when(type){
            "0" ->  address.text = "密保添加"
            "1" ->  address.text = "密保重置"
        }
        next.setOnClickListener {
            when(type){
                "0" -> setQuestion()
                "1" -> upQuestion()
            }
        }
        q1.setOnClickListener {
            utils.hideSoftKeyboard()
            if(popup != null){
                popup!!.dismiss()
                popup = null
            }
            showLoginPop(list, tv_q1,0)
            popup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
        }
        q2.setOnClickListener {
            utils.hideSoftKeyboard()
            if(popup != null){
                popup!!.dismiss()
                popup = null
            }
            showLoginPop(list, tv_q2,1)
            popup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
        }
        q3.setOnClickListener {
            utils.hideSoftKeyboard()
            if(popup != null){
                popup!!.dismiss()
                popup = null
            }
            showLoginPop(list, tv_q3,2)
            popup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
        }
        getList()
    }

    private var popup: PopupWindow? = null
    private fun showLoginPop(lis: MutableList<QuestionBean.BaseListBean>, v: TextView,i:Int) {
        val view = LayoutInflater.from(this).inflate(R.layout.q_dialog, null)
        popup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        popup!!.isTouchable = true
        popup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        popup!!.setBackgroundDrawable(dw)
        val list = view.findViewById<ListView>(R.id.list)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            if( popup != null ){
                popup!!.dismiss()
                popup = null
            }
        }
        list.adapter = QuestionAdapter(this, lis, R.layout.q_item)
        list.setOnItemClickListener { _, _, position, _ ->
            when(lis[position].id){
                id0 ->
                    if(i != 0){
                        utils.showToast("不能选择重复问题")
                    }
                id1 ->
                    if(i != 1){
                    utils.showToast("不能选择重复问题")
                }
                id2 ->
                    if(i != 2){
                    utils.showToast("不能选择重复问题")
                }
                else -> when(i){
                    0 ->{
                        d1.setText("".toCharArray(), 0, "".length)
                        id0 = lis[position].id
                        qu0 =lis[position].para_key
                        v.text = lis[position].para_key
                    }
                    1 ->{
                        d2.setText("".toCharArray(), 0, "".length)
                        id1 = lis[position].id
                        qu1 =lis[position].para_key
                        v.text = lis[position].para_key
                    }
                    2 ->{
                        d3.setText("".toCharArray(), 0, "".length)
                        id2 = lis[position].id
                        qu2 =lis[position].para_key
                        v.text = lis[position].para_key
                    }
                }
            }
            if(popup != null){
                popup!!.dismiss()
                popup = null
            }
        }
    }

    private fun getList() {
        utils.getProgress(this)
        HttpRequestPort.instance.questionList(object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<QuestionBean>() {})
                        list.addAll(bean.baseList)
                        tv_q1.text = list[0].para_key
                        id0 = list[0].id
                        qu0 =list[0].para_key
                        tv_q2.text = list[1].para_key
                        id1 = list[1].id
                        qu1 =list[1].para_key
                        tv_q3.text = list[2].para_key
                        id2 = list[2].id
                        qu2 =list[2].para_key
                    }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
                })
    }

    private fun setQuestion(){
        val a0 = d1.text.toString().trim()
        val a1 = d2.text.toString().trim()
        val a2 = d3.text.toString().trim()
        when{
            TextUtils.isEmpty(a0) ->  utils.showToast("请完善问题一的答案")
            TextUtils.isEmpty(a1) ->  utils.showToast("请完善问题二的答案")
            TextUtils.isEmpty(a2) ->  utils.showToast("请完善问题三的答案")
            else -> {
                val list = arrayListOf<UpQuestionBean>()
                val up0 = UpQuestionBean()
                up0.id = id0
                up0.question = qu0
                up0.answer = a0
                val up1 = UpQuestionBean()
                up1.id = id1
                up1.question = qu1
                up1.answer = a1
                val up2 = UpQuestionBean()
                up2.id = id2
                up2.question = qu2
                up2.answer = a2
                list.add(up0)
                list.add(up1)
                list.add(up2)
                val toJSONString = JSON.toJSONString(list)
                val userId = mk.decodeString(Tool.USER_ID, "")
                HttpRequestPort.instance.setQuestion(userId,toJSONString,object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                        if(bean.result =="0"){
                            utils.showToast("添加成功")
                            mk.encode(Tool.BINDQ, "1")
                            finish()
                        }else{
                            utils.showToast(bean.message)
                        }
                    }
                })
            }
        }
    }
    private fun upQuestion(){
        val a0 = d1.text.toString().trim()
        val a1 = d2.text.toString().trim()
        val a2 = d3.text.toString().trim()
        when{
            TextUtils.isEmpty(a0) ->  utils.showToast("请完善问题一的答案")
            TextUtils.isEmpty(a1) ->  utils.showToast("请完善问题二的答案")
            TextUtils.isEmpty(a2) ->  utils.showToast("请完善问题三的答案")
            else -> {
                val list = arrayListOf<UpQuestionBean>()
                val up0 = UpQuestionBean()
                up0.id = id0
                up0.question = qu0
                up0.answer = a0
                val up1 = UpQuestionBean()
                up1.id = id1
                up1.question = qu1
                up1.answer = a1
                val up2 = UpQuestionBean()
                up2.id = id2
                up2.question = qu2
                up2.answer = a2
                list.add(up0)
                list.add(up1)
                list.add(up2)
                val toJSONString = JSON.toJSONString(list)
                val userId = mk.decodeString(Tool.USER_ID, "")
                HttpRequestPort.instance.upQuestion(userId,toJSONString,object : BaseHttpCallBack(this) {
                    override fun success(data: String) {
                        super.success(data)
                        val bean = JSONObject.parseObject(data, object : TypeReference<GetNum>() {})
                        if(bean.result =="0"){
                            utils.showToast("修改成功")
                            mk.encode(Tool.BINDQ,"1")
                            finish()
                        }
                    }
                })
            }
        }
    }
}
