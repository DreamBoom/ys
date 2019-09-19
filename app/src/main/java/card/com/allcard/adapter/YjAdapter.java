package card.com.allcard.adapter;

import android.app.Activity;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.YjListBean;

public class YjAdapter extends CommonAdapter<YjListBean.CardsListBean> {
    private Activity mContext;
    public YjAdapter(Activity act, List<YjListBean.CardsListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.mContext = act;
    }
    @Override
    public void convert(ViewHolder holder, YjListBean.CardsListBean datas) {
        String s = datas.getCardNo();
        String num = s.substring(0, 4) + "****" + datas.getCardNo().substring(s.length()-4,s.length());
        holder.setText(R.id.tv_num,num);
        holder.setText(R.id.tv_time,"办卡时间："+datas.getTrDate());
        holder.setText(R.id.tv_yj,"押金"+datas.getAmt()+"元");
    }

}
