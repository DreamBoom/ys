package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import card.com.allcard.R;
import card.com.allcard.bean.DeviceListBean;
import card.com.allcard.bean.QuestionBean;

public class QuestionAdapter extends CommonAdapter<QuestionBean.BaseListBean> {
    private Activity act;

    public QuestionAdapter(Activity act, List<QuestionBean.BaseListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, QuestionBean.BaseListBean data) {
        holder.setText(R.id.tv_q,data.getPara_key());
    }

}

