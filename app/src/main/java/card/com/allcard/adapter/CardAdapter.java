package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.JmBeam;

public class CardAdapter extends BaseQuickAdapter<JmBeam.CardsListBean, BaseViewHolder> {
    private Activity act;

    public CardAdapter(Activity act, int layoutResId, @Nullable List<JmBeam.CardsListBean> data) {
        super(layoutResId, data);
        this.act =act;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder holder, JmBeam.CardsListBean datas) {
        String cert = datas.getCardNo().substring(0, 3) + "********" +
                datas.getCardNo().substring(datas.getCardNo().length()-4);
        holder.setText(R.id.kh,"卡号: "+cert);
        if(datas.getCardState().equals("2")||datas.getCardState().equals("8")){
            holder.getView(R.id.im_gs).setVisibility(View.VISIBLE);
        }
    }
}
