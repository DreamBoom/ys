package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.utils.ActivityUtils
import card.com.allcard.utils.LocationUtil
import com.amap.api.location.CoordinateConverter
import com.amap.api.location.DPoint
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.LocationSource
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.PolylineOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_DEFAULT
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.title.*
import java.util.*

class Map : AppCompatActivity() , LocationSource, RouteSearch.OnRouteSearchListener{

    var utils = ActivityUtils(this)
    private var locationUtil: LocationUtil? = null
    private var mListener: LocationSource.OnLocationChangedListener? = null//定位监听器
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        map.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.hide)
        initView()
        close.setOnClickListener { finish() }
    }
    var phone0 = ""
    var aMap: AMap?=null
    var la = 0.0
    var lg = 0.0
    var lat0 = 0.0
    var lgt0 = 0.0
    @SuppressLint("SetTextI18n")
    fun initView() {
        aMap = map.map
        val markerOption = MarkerOptions()
        val name0 = intent.getStringExtra("name")
        phone0 = intent.getStringExtra("phone")
        var address0 = intent.getStringExtra("address")
        la = intent.getStringExtra("lat").toDouble()
        lg = intent.getStringExtra("Lng").toDouble()
        if(TextUtils.isEmpty(address0)){
            address0 = "暂无地址信息"
        }
        address.text = name0
        hospital_name.text = name0
        val latLng = LatLng(la, lg)
        markerOption.position(latLng)
        val marker = aMap!!.addMarker(markerOption)
        aMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(la, lg), 18f))
        val uiSettings = aMap!!.uiSettings
      //  uiSettings.setLogoBottomMargin(-50)//隐藏logo
//        uiSettings.isRotateGesturesEnabled = false   //禁止地图旋转手势
//        uiSettings.isTiltGesturesEnabled = false     //禁止倾斜手势
//        marker.title = name0//设置标题
//        marker.snippet = "$phone0,$address0"//设置内容
//        mapGd.setInfoWindowAdapter(WindowAdapter(this))
//        mapGd.setOnInfoWindowClickListener(WindowAdapter(this))
//        mapGd.setOnMarkerClickListener(WindowAdapter(this))
     //   marker.showInfoWindow()
    //    marker.isClickable =true

        //设置定位监听
        aMap!!.setLocationSource(this)
        //设置缩放级别
       // aMap!!.moveCamera(CameraUpdateFactory.zoomTo(18f))
        //显示定位层并可触发，默认false
        aMap!!.isMyLocationEnabled = true
        locationUtil!!.setLocationCallBack { code, str, lat, lgt, aMapLocation ->
            //根据获取的经纬度，将地图移动到定位位置
            //aMap!!.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(lat, lgt)))
            mListener!!.onLocationChanged(aMapLocation)
            //添加定位图标
            //定位坐标
            lat0 = lat
            lgt0 = lgt
            val mPoint = DPoint(lat, lgt)
            val mPoint1 = DPoint(la, lg)
            //获取两点之间距离
            CoordinateConverter.calculateLineDistance(mPoint, mPoint1)
            val distance = CoordinateConverter.calculateLineDistance(mPoint, mPoint1) / 1000
            hospital_adr.text = "$address0\n距您:${distance.toInt()} KM"
            // tv.setText("距离:" + distance.toInt() + "KM")
            dw.setOnClickListener {
                aMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(la, lg), 18f))
                //   marker.showInfoWindow()
            }
            aMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(la, lg), 18f))
            call.setOnClickListener { popup!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0) }
            see.setOnClickListener {
                see.isClickable = false
                see() }
            showPop()
        }
    }

    override fun activate(onLocationChangedListener: LocationSource.OnLocationChangedListener) {
        mListener = onLocationChangedListener
        locationUtil = LocationUtil()
        locationUtil!!.startLocate(this@Map)
    }

    private fun see(){
            //路线规划
        val start = LatLonPoint(lat0, lgt0)
        val end = LatLonPoint(la, lg)
            val routeSearch = RouteSearch(this@Map)
            routeSearch.setRouteSearchListener(this)
            //构建开始点与终止点的经纬度数据
            val fromAndTo = RouteSearch.FromAndTo(start, end)
            // fromAndTo包含路径规划的起点和终点，drivingMode表示驾车模式
            // 第三个参数表示途经点（最多支持16个），  第四个参数表示避让区域（最多支持32个），第五个参数表示避让道路
            val query = RouteSearch.DriveRouteQuery(
                    fromAndTo, DRIVING_SINGLE_DEFAULT, null, null, "")
            routeSearch.calculateDriveRouteAsyn(query)
            //添加标记
            aMap!!.addMarker(locationUtil!!.getstart("", lat0, lgt0))
           // aMap!!.addMarker(locationUtil!!.getend("", la, lg))
    }

    private var popup: PopupWindow? = null
    private fun showPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "拨打"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "确定拨打电话?"
        popup = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        popup!!.isTouchable = true
        popup!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        popup!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            call(phone0)
        }
        cancel.setOnClickListener {
            popup!!.dismiss()
        }
    }

    /**
     * 调用拨号界面
     * @param phone 客服电话号码
     */
    private fun call(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        map.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun deactivate() {
        mListener = null
    }

    override fun onBusRouteSearched(busRouteResult: BusRouteResult, i: Int) {

        //  aMap.clear();
        //几种公交路线
        val busPathList = busRouteResult.paths
        //选择第一条
        val busSteps = busPathList[0].steps

        for (bs in busSteps) {
            //获取该条路线某段公交路程步行的点
            val routeBusWalkItem = bs.walk
            if (routeBusWalkItem != null) {
                val wsList = routeBusWalkItem.steps
                val walkPoint = ArrayList<LatLng>()

                for (ws in wsList) {
                    val points = ws.polyline
                    for (lp in points) {
                        walkPoint.add(LatLng(lp.latitude, lp.longitude))
                    }
                }
                //添加步行点
                aMap!!.addPolyline(PolylineOptions()
                        .addAll(walkPoint)
                        .width(8f)
                        .geodesic(true)
                        .color(Color.argb(100, 21, 79, 181)))
            }

            //获取该条路线某段公交路路程的点
            val rbli = bs.busLines
            val busPoint = ArrayList<LatLng>()


            for (one in rbli) {

                val points = one.polyline

                for (lp in points) {
                    busPoint.add(LatLng(lp.latitude, lp.longitude))
                }
            }
            //添加公交路线点
            aMap!!.addPolyline(PolylineOptions()
                    .addAll(busPoint)
                    .width(8f)
                    .geodesic(true)
                    .color(Color.argb(100, 21, 79, 181)))
        }
    }

    override fun onDriveRouteSearched(driveRouteResult: DriveRouteResult, i: Int) {
        if (driveRouteResult.paths == null) {
            return
        }
        val pathList = driveRouteResult.paths
        val driverPath = ArrayList<LatLng>()
        for (dp in pathList) {
            val stepList = dp.steps
            for (ds in stepList) {
                val points = ds.polyline
                for (llp in points) {
                    driverPath.add(LatLng(llp.latitude, llp.longitude))
                }
            }
        }

        //  aMap.clear();
        aMap!!.addPolyline(PolylineOptions()
                .addAll(driverPath)
                .width(8f)
                .geodesic(true)
                .color(Color.argb(100, 21, 79, 181)))

    }

    override fun onWalkRouteSearched(walkRouteResult: WalkRouteResult, i: Int) {

        val pathList = walkRouteResult.paths
        val walkPaths = ArrayList<LatLng>()

        for (dp in pathList) {

            val stepList = dp.steps
            for (ds in stepList) {


                val points = ds.polyline
                for (llp in points) {
                    walkPaths.add(LatLng(llp.latitude, llp.longitude))
                }
            }
        }

        // aMap.clear();
        aMap!!.addPolyline(PolylineOptions()
                .addAll(walkPaths)
                .width(8f)
                .geodesic(true)
                .color(Color.argb(100, 21, 79, 181)))

    }

    override fun onRideRouteSearched(rideRouteResult: RideRouteResult, i: Int) {
        val pathList = rideRouteResult.paths
        val walkPaths = ArrayList<LatLng>()
        for (dp in pathList) {
            val stepList = dp.steps
            for (ds in stepList) {
                val points = ds.polyline
                for (llp in points) {
                    walkPaths.add(LatLng(llp.latitude, llp.longitude))
                }
            }
        }

        //aMap.clear();
        aMap!!.addPolyline(PolylineOptions()
                .addAll(walkPaths)
                .width(8f)
                .geodesic(true)
                .color(Color.argb(100, 21, 79, 181)))

    }
}
