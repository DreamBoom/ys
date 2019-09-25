package card.com.allcard.adapter;

import android.app.Activity;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.MoneyBean;
import card.com.allcard.utils.ActivityUtils;
import card.com.allcard.view.MyListView;

public class MoneyAdapter extends CommonAdapter<MoneyBean.DetailListBeanX> {
    private Activity mContext;
    private final ActivityUtils utils;
    public MoneyAdapter(Activity act, List<MoneyBean.DetailListBeanX> data, int layoutId) {
        super(act, data, layoutId);
        this.mContext = act;
        utils = new ActivityUtils(act);
    }
    @Override
    public void convert(ViewHolder holder, MoneyBean.DetailListBeanX datas) {
        holder.setText(R.id.yue,datas.getMonth());
        String s1  = utils.save2(Double.valueOf(datas.getOutAmt()) * 0.01);
        String s2  = utils.save2(Double.valueOf(datas.getInAmt()) * 0.01);
        holder.setText(R.id.zc,"支出:"+s1);
        holder.setText(R.id.sr,"收入:"+s2);
        MyListView list = holder.getView(R.id.list);
        MoneyTwoAdapter adapter = new MoneyTwoAdapter(mContext,datas.getDetailList(),R.layout.money_item);
        list.setAdapter(adapter);
    }
}
