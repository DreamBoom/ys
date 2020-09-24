package card.com.allcard.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.SearchBean;
import card.com.allcard.utils.ActivityUtils;

/**
 * @author wmx
 */
public class SearchAdapter extends CommonAdapter<SearchBean.DataBean> {
    private Activity mContext;
    private final ActivityUtils utils;
    private int type;
    public SearchAdapter(Activity act, List<SearchBean.DataBean> data, int layoutId,int type) {
        super(act, data, layoutId);
        this.mContext = act;
        this.type = type;
        utils = new ActivityUtils(act);
    }
    @Override
    public void convert(ViewHolder holder, SearchBean.DataBean datas) {
        holder.setText(R.id.date,datas.getConsumeTime());
        String s  = ""+datas.getAmount();
        if(type == 1){
            holder.setText(R.id.tv_type,"账户充值");
            holder.setImageResource(R.id.type, R.drawable.img_yemx_cz);
            holder.setText(R.id.money,"+ "+s);
            holder.setTextColor(R.id.money, ContextCompat.getColor(mContext,R.color.blue));
        }else {
            holder.setText(R.id.tv_type,"账户消费");
            holder.setImageResource(R.id.type, R.drawable.img_yemx_xf);
            holder.setText(R.id.money,"- "+s);
        }
    }
}
