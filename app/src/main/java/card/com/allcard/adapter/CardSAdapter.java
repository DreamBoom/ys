package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import card.com.allcard.R;

public class CardSAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Activity act;

    public CardSAdapter(Activity act, int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
        this.act =act;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder holder, String data) {
        holder.setText(R.id.kh,"卡号: "+data);
    }
}
