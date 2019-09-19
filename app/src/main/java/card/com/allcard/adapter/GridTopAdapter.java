package card.com.allcard.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import org.xutils.x;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.TabTwoBean;

public class GridTopAdapter extends CommonAdapter<TabTwoBean.ListBean.SummarydetailBean> {
    private Activity act;
    public GridClickListener gridClickListener;
    public GridTopAdapter(Activity act, List<TabTwoBean.ListBean.SummarydetailBean> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
    }
    void setgridClickListener(GridClickListener onClickListener) {
        this.gridClickListener = onClickListener;
    }

    public interface GridClickListener {
        void onClickListener(int position);
    }
    @Override
    public void convert(ViewHolder holder, TabTwoBean.ListBean.SummarydetailBean data,int position) {
        holder.setText(R.id.text,data.getSumd_name());
        ImageView img = holder.getView(R.id.img);
        ImageView add = holder.getView(R.id.add);
        add.setBackground(ContextCompat.getDrawable(act,R.drawable.btn_fw_sc));
        add.setVisibility(View.VISIBLE);
        add.setOnClickListener(v -> {
            gridClickListener.onClickListener(position);
        });
        x.image().bind(img, data.getSumd_img());
    }

}
