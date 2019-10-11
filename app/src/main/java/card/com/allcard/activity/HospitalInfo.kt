package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import card.com.allcard.R
import card.com.allcard.net.HttpRequestPort
import kotlinx.android.synthetic.main.activity_hospital_info.*
import kotlinx.android.synthetic.main.title.*

class HospitalInfo : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_hospital_info

    @SuppressLint("SetTextI18n")
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        close.setOnClickListener { finish() }
        var name0 = intent.getStringExtra("name")
        var phone0 = intent.getStringExtra("phone")
        var address0 = intent.getStringExtra("address")
        val lat = intent.getStringExtra("lat")
        val lng = intent.getStringExtra("Lng")
        address.text = name0
        when {
            TextUtils.isEmpty(name0) -> name0 = "暂无信息"
            TextUtils.isEmpty(phone0) -> phone0 = "暂无信息"
            TextUtils.isEmpty(address0) -> address0 = "暂无地址信息"
        }
        tel.text = "医院电话:$phone0"
        address1.text = "医院地址:$address0"
        mapName.text = address0

        jf.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url",HttpRequestPort.H5_BASE_URL
                    +"weixin/accountPayment/medicalHospitalRecord.jsp")
            utils.startActivityBy(WebOther::class.java,bundle)
        }
        pb.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url",HttpRequestPort.H5_BASE_URL
                    +"weixin/hospital/medicalHospitalRecordyy.jsp")
            utils.startActivityBy(WebOther::class.java,bundle)
        }
        dh.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url",HttpRequestPort.H5_BASE_URL
                    +"weixin/hospital/hospitalNav.jsp")
            utils.startActivityBy(WebOther::class.java,bundle)
        }
        jl.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url",HttpRequestPort.H5_BASE_URL
                    +"weixin/hospital/medicalHospitalRecord.jsp")
            utils.startActivityBy(WebOther::class.java,bundle)
        }
        map.setOnClickListener {
            if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)) {
                utils.showToast("暂无地址信息")
            } else {
                val intent = Intent(this, Map::class.java)
                intent.putExtra("name",name0)
                intent.putExtra("phone",phone0)
                intent.putExtra("address",address0)
                intent.putExtra("lat",lat)
                intent.putExtra("Lng",lng)
                startActivity(intent)
            }
        }
    }
}
