package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.DeviceListBean;

public class DeviceItemAdapter extends CommonAdapter<DeviceListBean.DeviceList> {
    private Activity act;

    public DeviceItemAdapter(Activity act, List<DeviceListBean.DeviceList> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, DeviceListBean.DeviceList data) {
        TextView name = holder.getView(R.id.device_name);
        name.setText(data.getDeviceName());
        TextView time = holder.getView(R.id.device_time);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time1 = simpleDateFormat.format(data.getDeviceTime().getTime());
        time.setText("最近登录: "+time1);
        if(data.getDeviceType().equals("0")){
            holder.setText(R.id.device_type,"(安全设备)");
            holder.setTextColor(R.id.device_type, ContextCompat.getColor(act,R.color.blue));
        }else {
            holder.setText(R.id.device_type,"(普通设备)");
            holder.setTextColor(R.id.device_type, ContextCompat.getColor(act,R.color.text_gray));
        }

        holder.setText(R.id.device_num,"设备系统: "+data.getDeviceApi());
    }

}

