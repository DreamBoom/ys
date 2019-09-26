package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import org.xutils.x;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.PayTypeBean;

public class PayTypeAdapter extends CommonAdapter<PayTypeBean.ListBean> {
    private Activity act;

    public PayTypeAdapter(Activity act, List<PayTypeBean.ListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, PayTypeBean.ListBean data) {
        holder.setText(R.id.type,data.getPara_name());
        ImageView view = holder.getView(R.id.icon);
        ImageView view1 = holder.getView(R.id.im_p);
        x.image().bind(view,data.getImg());
        if(data.getIs_enable().equals("1")){
            view1.setVisibility(View.VISIBLE);
        }else {
            view1.setVisibility(View.GONE);
        }
    }

}

