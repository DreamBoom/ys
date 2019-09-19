package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xutils.x;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.TabTwoBean;

public class TabTopAdapter extends BaseQuickAdapter<TabTwoBean.ListBean.SummarydetailBean, BaseViewHolder> {
    private Activity act;

    public TabTopAdapter(Activity act, int layoutResId, @Nullable List<TabTwoBean.ListBean.SummarydetailBean> data) {
        super(layoutResId, data);
        this.act =act;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder holder, TabTwoBean.ListBean.SummarydetailBean data) {
        ImageView im = holder.getView(R.id.img);
        x.image().bind(im, data.getSumd_img());
    }
}
