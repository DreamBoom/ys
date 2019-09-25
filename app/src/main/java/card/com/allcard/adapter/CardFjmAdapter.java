package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.FjmBean;

public class CardFjmAdapter extends BaseQuickAdapter<FjmBean.CardListBean, BaseViewHolder> {
    private Activity act;

    public CardFjmAdapter(Activity act, int layoutResId, @Nullable List<FjmBean.CardListBean> data) {
        super(layoutResId, data);
        this.act =act;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder holder,FjmBean.CardListBean datas) {
        String cert = datas.getCardNo().substring(0, 3) + "********" +
                datas.getCardNo().substring(datas.getCardNo().length()-4);
        holder.setText(R.id.kh,"卡号: "+cert);
        if(datas.getCardStatus().equals("2")){
            holder.getView(R.id.im_gs).setVisibility(View.VISIBLE);
        }

    }
}
