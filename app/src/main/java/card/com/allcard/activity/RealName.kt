package card.com.allcard.activity

import android.text.TextUtils
import android.widget.ImageView
import card.com.allcard.R
import card.com.allcard.bean.RealNameBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import kotlinx.android.synthetic.main.activity_real_name.*
import org.xutils.image.ImageOptions
import org.xutils.x

class RealName : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_real_name
    override fun initView() {
        MyApplication.instance.addActivity(this)
        close.setOnClickListener { finish() }
        val header = mk.decodeString(Tool.HEADER,"")
        val options =  ImageOptions.Builder()
                .setSize(300, 300)
                .setCrop(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.user)
                .setFailureDrawableId(R.drawable.user)
                .setUseMemCache(true)
                .setIgnoreGif(false)
                .setCircular(true).build()
        if(!TextUtils.isEmpty(header)){
            x.image().bind(im_icon, header,options)
        }else{
            x.image().bind(im_icon, Tool.HEADER_IMG,options)
        }
        initData()
    }

    private fun initData() {
        utils.getProgress(this)
        val userId = mk.decodeString(Tool.USER_ID, "")
        HttpRequestPort.instance.smzInfo(userId, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val bean = JSONObject.parseObject(data, object : TypeReference<RealNameBean>() {})
                if(bean.result == "0"){
                    name.text = bean.clientName
                    val cert = bean.certNo.substring(0, 6) + "********" +
                            bean.certNo.substring(bean.certNo.length-4,bean.certNo.length)
                    num.text = cert
                    tv_real_name.text = bean.clientName
                  when(bean.sex){
                      "1" ->  tv_sex.text = "男"
                      "2" -> tv_sex.text = "女"
                  }
                    tv_data.text = bean.birthday
                    if(!TextUtils.isEmpty(bean.resideAddr)){
                        tv_address.text = bean.resideAddr
                    }
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("信息拉取失败，返回重试")
            }

            override fun onFinished() {
                super.onFinished()
                utils.hindProgress()
            }
        })
    }
}
