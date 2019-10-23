package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.WebNew;
import card.com.allcard.bean.ServiceListBean;
import card.com.allcard.net.HttpRequestPort;

public class ServiceAdapter extends CommonAdapter<ServiceListBean.ListBean> {
    private Activity mContext;
    private final ImageOptions options;

    public ServiceAdapter(Activity act, List<ServiceListBean.ListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.mContext = act;
        options = new ImageOptions.Builder()
                .setSize(300, 300)
                .setCrop(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.img_sy_fezn)
                .setFailureDrawableId(R.drawable.img_sy_fezn)
                .setUseMemCache(true)
                .setIgnoreGif(false)
                .setCircular(false).build();
    }
    @Override
    public void convert(ViewHolder holder,ServiceListBean.ListBean datas) {
        holder.setText(R.id.tv_news_title,datas.getTitle());
        holder.setText(R.id.tv_news_data,datas.getIn_date());
        ImageView img = holder.getView(R.id.im_news);
        x.image().bind(img, datas.getSerimg(),options);
        RelativeLayout news = holder.getView(R.id.news);
        news.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(mContext, WebNew.class);
            intent.putExtra("url", HttpRequestPort.H5_BASE_URL+"WeiLoginController/wxservicede1.do?id="+datas.getId()+"&fixparam=android");
            mContext.startActivity(intent);
        });
    }

}
