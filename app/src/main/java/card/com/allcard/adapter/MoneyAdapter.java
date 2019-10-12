package card.com.allcard.adapter;

import android.app.Activity;
import android.view.View;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.MoneyBean;
import card.com.allcard.utils.ActivityUtils;
import card.com.allcard.view.MyListView;

public class MoneyAdapter extends CommonAdapter<MoneyBean.DetailListBeanX> {
    private Activity mContext;
    private final ActivityUtils utils;
    public ClickListener Click;
    void setClickListener(ClickListener onClickListener) {
        this.Click = onClickListener;
    }
    public interface ClickListener {
        void onClickListener();
    }
    public MoneyAdapter(Activity act, List<MoneyBean.DetailListBeanX> data, int layoutId) {
        super(act, data, layoutId);
        this.mContext = act;
        utils = new ActivityUtils(act);
    }
    @Override
    public void convert(ViewHolder holder, MoneyBean.DetailListBeanX datas) {
        holder.getView(R.id.yue).setOnClickListener(v -> {
            Click.onClickListener();
        });
        holder.setText(R.id.yue,datas.getMonth());
        String s1  = utils.save2(Double.valueOf(datas.getOutAmt()) * 0.01);
        String s2  = utils.save2(Double.valueOf(datas.getInAmt()) * 0.01);
        holder.setText(R.id.zc,"支出: ¥"+s1);
        holder.setText(R.id.sr,"收入: ¥"+s2);
        if(datas.getDetailList().size()>0){
            holder.getView(R.id.noData).setVisibility(View.GONE);
            holder.getView(R.id.list).setVisibility(View.VISIBLE);
            MyListView list = holder.getView(R.id.list);
            MoneyTwoAdapter adapter = new MoneyTwoAdapter(mContext,datas.getDetailList(),R.layout.money_item);
            list.setAdapter(adapter);
        }else {
            holder.getView(R.id.noData).setVisibility(View.VISIBLE);
            holder.getView(R.id.list).setVisibility(View.GONE);
        }
    }
}
