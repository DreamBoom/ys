package card.com.allcard.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.AllService;
import card.com.allcard.bean.ServiceTypeBean;
import card.com.allcard.utils.ActivityUtils;

public class MoreAdapter extends CommonAdapter<ServiceTypeBean.ListBean> {
    private Activity act;
    public AllClickListener allClick;
    private final ActivityUtils utils;

    void setallClickListener(AllClickListener onClickListener) {
        this.allClick = onClickListener;
    }

    public interface AllClickListener {
        void onClickListener( int i);
    }

    public MoreAdapter(Activity act, List<ServiceTypeBean.ListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
        utils = new ActivityUtils(act);
    }

    @Override
    public void convert(ViewHolder holder, ServiceTypeBean.ListBean data,int position) {
        holder.setText(R.id.tab, data.getPara_key());
        holder.getView(R.id.tab).setOnClickListener(v -> allClick.onClickListener(position));
        if(position == AllService.Companion.getI()){
            holder.setTextColor(R.id.tab, ContextCompat.getColor(act,R.color.blue));
        }

    }
}
