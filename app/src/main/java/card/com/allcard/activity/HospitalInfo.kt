package card.com.allcard.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import com.zzhoujay.richtext.RichText
import com.zzhoujay.richtext.callback.OnUrlClickListener
import kotlinx.android.synthetic.main.activity_hospital_info.*
import kotlinx.android.synthetic.main.title.*

class HospitalInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_hospital_info
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        val hosId = intent.getStringExtra("hosId")
        val name0 = intent.getStringExtra("name")
        var phone0 = intent.getStringExtra("phone")
        var address0 = intent.getStringExtra("address")
        var tran = intent.getStringExtra("tran")
        val lat = intent.getStringExtra("lat")
        val lng = intent.getStringExtra("Lng")
        val hosWeb = intent.getStringExtra("hosWeb")
        val hosInfo = intent.getStringExtra("hosInfo")
        when {
            TextUtils.isEmpty(tran) -> tran = "暂无信息"
            TextUtils.isEmpty(phone0) -> phone0 = "暂无信息"
            TextUtils.isEmpty(address0) -> address0 = "暂无地址信息"
        }
        address.text = name0
        tel.text = "医院电话: $phone0"
        address1.text = "医院地址: $address0"
        zn.text = "交通指南: $tran"
        hos_web.text = hosWeb
        mapName.text = address0
        val userId = mk.decodeString(Tool.USER_ID, "")
        val q = mk.decodeString(Tool.IS_AUTH, "")
        jf.setOnClickListener {
            if (q == "1") run {
                utils.showToast("请先进行实名认证")
            } else {
                if (!TextUtils.isEmpty(userId)) {
                    val bundle = Bundle()
                    bundle.putString("url", HttpRequestPort.H5_BASE_URL
                            + "weixin/accountPayment/medicalHospitalRecord.jsp")
                    utils.startActivityBy(WebOther::class.java, bundle)
                } else {
                    startActivity(Intent(this, LoginActivity::class.java).putExtra("from", 1))
                }
            }
        }
        pb.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", hosId)
            bundle.putString("url", HttpRequestPort.H5_BASE_URL
                    + "weixin/hospital_v2/departmentNavSelect.jsp")
            utils.startActivityBy(WebHos::class.java, bundle)
        }
        dh.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", hosId)
            bundle.putString("url", HttpRequestPort.H5_BASE_URL
                    + "weixin/hospital_v2/departmentNav.jsp")
            utils.startActivityBy(WebHos::class.java, bundle)
        }
        jl.setOnClickListener {
            if (q == "1") run {
                utils.showToast("请先进行实名认证")
            } else {
                if (!TextUtils.isEmpty(userId)) {
                    val bundle = Bundle()
                    bundle.putString("id", hosId)
                    bundle.putString("url", HttpRequestPort.H5_BASE_URL
                            + "weixin/hospital_v2/medicalRecordTab.jsp")
                    utils.startActivityBy(WebHos::class.java, bundle)
                } else {
                    startActivity(Intent(this, LoginActivity::class.java).putExtra("from", 1))
                }
            }
        }
        hos_web.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val contentUrl = Uri.parse(hosWeb)
            intent.data = contentUrl
            startActivity(intent)
        }
        map.setOnClickListener {
            if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)) {
                utils.showToast("暂无地址信息")
            } else {
                val intent = Intent(this, Map::class.java)
                intent.putExtra("name", name0)
                intent.putExtra("phone", phone0)
                intent.putExtra("address", address0)
                intent.putExtra("lat", lat)
                intent.putExtra("Lng", lng)
                startActivity(intent)
            }
        }

        if (!TextUtils.isEmpty(hosInfo)) {
            RichText.initCacheDir(this)
            RichText.debugMode = true
            RichText.from(hosInfo)
                    .urlClick(OnUrlClickListener { url ->
                        if (url.startsWith("code://")) {
                            Toast.makeText(this@HospitalInfo, url.replaceFirst("code://".toRegex(), ""), Toast.LENGTH_SHORT).show()
                            return@OnUrlClickListener true
                        }
                        false
                    })
                    .into(info)
        }

//
//        RichText.from(img)
//                .imageClick { imageUrls, position ->
//                    val calendar = Calendar.getInstance()
//                    val m = calendar.get(Calendar.MINUTE)
//                    val s = calendar.get(Calendar.SECOND)
//                    Toast.makeText(this@HospitalInfo, "M:$m,S:$s", Toast.LENGTH_SHORT).show()
//                }.into(info2)
    }
}
