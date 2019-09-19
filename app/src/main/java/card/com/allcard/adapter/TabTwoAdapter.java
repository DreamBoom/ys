package card.com.allcard.adapter;

import android.app.Activity;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.TabTwoBean;
import card.com.allcard.view.MyGridView;

public class TabTwoAdapter extends CommonAdapter<TabTwoBean.ListBean.IconAllBean> implements GridAdapter.AllClickListener{
    private Activity act;
    public TwoClickListener Click;

    void setallClickListener(TwoClickListener onClickListener) {
        this.Click = onClickListener;
    }

    public interface TwoClickListener {
        void onClickListener();
    }

    public TabTwoAdapter(Activity act, List<TabTwoBean.ListBean.IconAllBean> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
    }

    @Override
    public void convert(ViewHolder holder, TabTwoBean.ListBean.IconAllBean data) {
        holder.setText(R.id.tab_name,data.getName());
        if(data.getSummarydetailList() != null){
            GridAdapter gridAdapter = new GridAdapter(act, data.getSummarydetailList(), R.layout.gridview_item);
            gridAdapter.allClick = this;
            MyGridView grid = holder.getView(R.id.grid);
            grid.setAdapter(gridAdapter);
            gridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickListener() {
        Click.onClickListener();
    }
}
