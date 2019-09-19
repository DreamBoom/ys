package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.Map;
import card.com.allcard.bean.DrugstoreList;

public class DrugstoreAdapter extends CommonAdapter<DrugstoreList.PharmacyBean> {
    private Activity act;
    public DrugstoreAdapter(Activity act, List<DrugstoreList.PharmacyBean> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
    }
    @Override
    public void convert(ViewHolder holder, DrugstoreList.PharmacyBean data) {
        if(TextUtils.isEmpty(data.getAddress())){
            holder.setText(R.id.hospital_adr," 暂无地址信息");
        }else {
            holder.setText(R.id.hospital_adr," "+data.getAddress());
        }
        holder.setText(R.id.hospital_name," "+data.getPhar_name());
        RelativeLayout item = holder.getView(R.id.item);
        item.setOnClickListener(v -> {
            String[] split = data.getPass_strength().split(",");
            Intent intent = new Intent();
            intent.setClass(act, Map.class);
            intent.putExtra("name",data.getPhar_name());
            intent.putExtra("phone",data.getTelephone());
            intent.putExtra("address",data.getAddress());
            intent.putExtra("lat",split[1]);
            intent.putExtra("Lng",split[0]);
            act.startActivity(intent);
        });
    }

}
