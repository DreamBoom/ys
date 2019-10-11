package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.WebNew;
import card.com.allcard.bean.ServiceListBean;
import card.com.allcard.net.HttpRequestPort;

public class ServiceAdapter extends CommonAdapter<ServiceListBean.ListBean> {
    private Activity mContext;
    public ServiceAdapter(Activity act, List<ServiceListBean.ListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.mContext = act;
    }
    @Override
    public void convert(ViewHolder holder,ServiceListBean.ListBean datas) {
        holder.setText(R.id.tv_news_title,datas.getTitle());
        holder.setText(R.id.tv_news_data,datas.getIn_date());
        ImageView img = holder.getView(R.id.im_news);
        if(TextUtils.isEmpty(datas.getSerimg())){
            img.setBackgroundResource(R.drawable.img_sy_fezn);
        }else {
            //x.image().bind(img, datas.getSerimg());
            img.setBackgroundResource(R.drawable.img_sy_fezn);
        }
        RelativeLayout news = holder.getView(R.id.news);
        news.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(mContext, WebNew.class);
            intent.putExtra("url", HttpRequestPort.H5_BASE_URL+"WeiLoginController/wxservicede1.do?id="+datas.getId()+"&fixparam=android");
            mContext.startActivity(intent);
        });
    }

}
