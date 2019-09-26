package card.com.allcard.adapter;

import android.content.Context;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tencent.mmkv.MMKV;

import java.io.IOException;
import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.BaseActivity;
import card.com.allcard.activity.ChooseArea;
import card.com.allcard.bean.AreaBean;
import card.com.allcard.bean.SearchAreaBean;
import card.com.allcard.net.BaseHttpCallBack;
import card.com.allcard.net.HttpRequestPort;
import card.com.allcard.tools.Tool;

public class ShengAdapter extends CommonAdapter<AreaBean.ArealistBean> {
    private ChooseArea fragment;
    private Context context;
    public String shengId;
    private MMKV mk = BaseActivity.Companion.getMk();
    public ShengAdapter(Context context, ChooseArea fragment, List<AreaBean.ArealistBean> datas, int layoutId) {
        super(context, datas, layoutId);
        this.fragment=fragment;
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final AreaBean.ArealistBean bean, final int position) throws IOException {
        super.convert(holder, bean, position);
        holder.setText(R.id.tv_pop,bean.getArea_name());
        TextView tvPop = holder.getView(R.id.tv_pop);
        tvPop.setOnClickListener(view -> {
            shengId = bean.getArea_id();
            //1024不代表任何意义，就是任性的符号
            String str = "1024";
            if(shengId.equals(str)){
                mk.encode(Tool.INSTANCE.getChooseArea(),"全部");
                mk.encode(Tool.INSTANCE.getArea_ID(),"");
                fragment.hide4("全部");
            }else {
                mk.encode(Tool.INSTANCE.getChooseArea(),bean.getArea_name());
                mk.encode(Tool.INSTANCE.getArea_ID(),bean.getArea_id());
                fragment.hide3(bean.getArea_name());
                searchArea(bean);
            }
        });
    }
    private void searchArea(AreaBean.ArealistBean areaBean) {
        HttpRequestPort.Companion.getInstance().searchArea(areaBean.getArea_id(),"3",
                new BaseHttpCallBack((ChooseArea)context){
                    @Override
                    public void success(String data) {
                        super.success(data);
                        SearchAreaBean bean = JSONObject.parseObject(data, new TypeReference<SearchAreaBean>() {});
                        int number = bean.getMessage().get(0).getNumber();
                        if(number == 0){
                            fragment.hide4(areaBean.getArea_name());
                        }else {
                            fragment.hide1(areaBean.getArea_name());
                        }
                    }

                    @Override
                    public void onFinished() {
                        super.onFinished();
                    }
                });
    }
}

