package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.YjListBean;
import card.com.allcard.utils.ActivityUtils;

public class CashAdapter extends BaseQuickAdapter<YjListBean.CardsListBean, BaseViewHolder> {
    private Activity act;
    private final ActivityUtils utils;
    public CashAdapter(Activity act, int layoutResId, @Nullable List<YjListBean.CardsListBean> data) {
        super(layoutResId, data);
        this.act =act;
        utils = new ActivityUtils(act);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder holder, YjListBean.CardsListBean datas) {
        String s = datas.getCardNo();
        String num = s.substring(0, 4) + "****" + datas.getCardNo().substring(s.length()-4,s.length());
        holder.setText(R.id.tv_num,num);
        holder.setText(R.id.tv_time,"办卡时间："+datas.getTrDate());
        holder.setText(R.id.tv_yj,"押金"+utils.save2(Double.valueOf(datas.getAmt())*0.01)+"元");
    }
}
