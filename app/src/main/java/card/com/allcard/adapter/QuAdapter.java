package card.com.allcard.adapter;

import android.content.Context;
import android.widget.TextView;

import com.tencent.mmkv.MMKV;

import java.io.IOException;
import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.BaseActivity;
import card.com.allcard.activity.ChooseArea;
import card.com.allcard.bean.AreaBean;
import card.com.allcard.tools.Tool;

public class QuAdapter extends CommonAdapter<AreaBean.ArealistBean> {
    private ChooseArea fragment;
    private Context context;
    public String areaId;
    public String name;
    private MMKV mk = BaseActivity.Companion.getMk();
    public QuAdapter(Context context, ChooseArea fragment, List<AreaBean.ArealistBean> datas, int layoutId) {
        super(context, datas, layoutId);
        this.fragment=fragment;
        this.context = context;
    }
    @Override
    public void convert(ViewHolder holder, final AreaBean.ArealistBean bean, final int position) throws IOException {
        super.convert(holder, bean, position);
        holder.setText(R.id.tv_pop,bean.getArea_name());
        TextView tvPop = holder.getView(R.id.tv_pop);
        areaId = bean.getArea_id();
        name = bean.getArea_name();
        tvPop.setOnClickListener(view -> {
            mk.encode(Tool.INSTANCE.getChooseArea(),bean.getArea_name());
            mk.encode(Tool.INSTANCE.getArea_ID(),areaId);
            fragment.hide4(bean.getArea_name());
        });
    }
}