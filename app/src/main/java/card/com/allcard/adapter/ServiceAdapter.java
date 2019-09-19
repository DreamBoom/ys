package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.xutils.x;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.WebOther;
import card.com.allcard.bean.ServiceListBean;
import card.com.allcard.net.HttpRequestPort;
import card.com.allcard.utils.ReadImgToBinary;

public class ServiceAdapter extends CommonAdapter<ServiceListBean.ListBean> {
    private Activity mContext;
    public ServiceAdapter(Activity act, List<ServiceListBean.ListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.mContext = act;
    }
    @Override
    public void convert(ViewHolder holder,ServiceListBean.ListBean datas) {
        holder.setText(R.id.tv_news_title,datas.getTitle());
        holder.setText(R.id.tv_news_data,datas.getIn_date().substring(0,10));
        ImageView img = holder.getView(R.id.im_news);
        x.image().bind(img,datas.getSerimg(), ReadImgToBinary.INSTANCE.getImageOptions());
        RelativeLayout news = holder.getView(R.id.news);
        news.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(mContext, WebOther.class);
            intent.putExtra("url", HttpRequestPort.H5_BASE_URL+"WeiLoginController/wxservicede1.do?id="+datas.getId());
            mContext.startActivity(intent);
        });
    }

}
