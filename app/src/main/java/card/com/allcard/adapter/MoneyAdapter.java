package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.MoneyOfDeal;
import card.com.allcard.bean.MoneyBean;

public class MoneyAdapter extends CommonAdapter<MoneyBean.CardsListBean> {
    private Activity mContext;
    public MoneyAdapter(Activity act, List<MoneyBean.CardsListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.mContext = act;
    }
    @Override
    public void convert(ViewHolder holder, MoneyBean.CardsListBean datas) {
        holder.setText(R.id.tv_type,datas.getTrCodeName());
        holder.setText(R.id.date,datas.getTrDate());
        switch (datas.getTrCode()){
            case "2830":
                holder.setImageResource(R.id.type, R.drawable.img_yemx_xf);
                holder.setText(R.id.money,"- "+datas.getAmt());
                holder.setTextColor(R.id.money,R.color.black3);
                break;
            case "2834":
                holder.setImageResource(R.id.type, R.drawable.img_yemx_zhtx);
                holder.setText(R.id.money,"- "+datas.getAmt());
                holder.setTextColor(R.id.money,R.color.black3);
                break;
            case "2835":
            case "1610":
                holder.setImageResource(R.id.type, R.drawable.img_yemx_tx);
                holder.setText(R.id.money,"- "+datas.getAmt());
                holder.setTextColor(R.id.money,R.color.black3);
                break;
            default:
                holder.setImageResource(R.id.type, R.drawable.img_yemx_cz);
                holder.setText(R.id.money,"+ "+datas.getAmt());
                break;
        }
        holder.getView(R.id.item).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(mContext, MoneyOfDeal.class);
            mContext.startActivity(intent);
        });
    }

}
