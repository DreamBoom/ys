package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.MoneyOfDeal;

public class MoneyAdapter extends CommonAdapter<String> {
    private Activity mContext;
    public MoneyAdapter(Activity act, List<String> data, int layoutId) {
        super(act, data, layoutId);
        this.mContext = act;
    }
    @Override
    public void convert(ViewHolder holder,String datas) {
        holder.setText(R.id.tv_type,datas);
//        holder.setText(R.id.date,datas);
//        holder.setText(R.id.money,datas);
        holder.getView(R.id.item).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(mContext, MoneyOfDeal.class);
            mContext.startActivity(intent);
        });
    }

}
