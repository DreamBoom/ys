package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.HospitalInfo;
import card.com.allcard.bean.HospitalList;

public class HospitalAdapter extends CommonAdapter<HospitalList.HospitalBean> {
    private Activity act;
    public HospitalAdapter(Activity act, List<HospitalList.HospitalBean> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
    }

    @Override
    public void convert(ViewHolder holder, HospitalList.HospitalBean data) {
        if(TextUtils.isEmpty(data.getAddress())){
            holder.setText(R.id.hospital_adr," 暂无地址信息");
        }else {
            holder.setText(R.id.hospital_adr, " " + data.getAddress());
        }
        holder.setText(R.id.hospital_name," "+data.getHosp_name());
        RelativeLayout item = holder.getView(R.id.item);
        String[] split = data.getPass_strength().split(",");
        item.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(act, HospitalInfo.class);
            intent.putExtra("name",data.getHosp_name());
            intent.putExtra("phone",data.getTelephone());
            intent.putExtra("address",data.getAddress());
            intent.putExtra("lat",split[1]);
            intent.putExtra("Lng",split[0]);
            act.startActivity(intent);
        });
    }
}
