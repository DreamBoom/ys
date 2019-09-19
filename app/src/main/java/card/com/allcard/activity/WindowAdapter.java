//package card.com.allcard.activity;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.model.Marker;
//
//import card.com.allcard.R;
//
//public class WindowAdapter implements AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
//        AMap.OnInfoWindowClickListener{
//
//    private Context context;
//    private static final String TAG = "WindowAdapter";
//
//    public WindowAdapter(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public View getInfoWindow(Marker marker) {
//        //关联布局
//        View view = LayoutInflater.from(context).inflate(R.layout.info_window, null);
//        TextView name = view.findViewById(R.id.name);
//        TextView phone = view.findViewById(R.id.phone);
//        TextView address = view.findViewById(R.id.address);
//        name.setText(marker.getTitle());
//        address.setText(marker.getSnippet().split(",")[1]);
//        phone.setText(marker.getSnippet().split(",")[0]);
//        return view;
//    }
//
//    //如果用自定义的布局，不用管这个方法,返回null即可
//    @Override
//    public View getInfoContents(Marker marker) {
//        return null;
//    }
//
//    // marker 对象被点击时回调的接口
//    // 返回 true 则表示接口已响应事件，否则返回false
//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        if(!marker.isInfoWindowShown()){
//            marker.showInfoWindow();
//        }
//        return false;
//    }
//
//    //绑定信息窗点击事件
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        if(marker.isInfoWindowShown()){
//            marker.hideInfoWindow();
//        }
//    }
//}

