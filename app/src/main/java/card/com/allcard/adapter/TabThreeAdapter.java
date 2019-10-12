package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.HospitalInfo;
import card.com.allcard.bean.HospitalList;

public class TabThreeAdapter extends BaseQuickAdapter<HospitalList.HospitalBean, BaseViewHolder> {
    private Activity act;

    public TabThreeAdapter(Activity act, int layoutResId, @Nullable List<HospitalList.HospitalBean> data) {
        super(layoutResId, data);
        this.act =act;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder holder, HospitalList.HospitalBean data) {
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
            intent.putExtra("hosId",data.getId());
            intent.putExtra("name",data.getHosp_name());
            intent.putExtra("phone",data.getTelephone());
            intent.putExtra("address",data.getAddress());
            intent.putExtra("lat",split[1]);
            intent.putExtra("Lng",split[0]);
            act.startActivity(intent);
        });
    }
}
