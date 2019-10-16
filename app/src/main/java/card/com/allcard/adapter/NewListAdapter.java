package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xutils.x;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.WebNew;
import card.com.allcard.bean.ServiceListBean;
import card.com.allcard.net.HttpRequestPort;

public class NewListAdapter extends BaseQuickAdapter<ServiceListBean.ListBean, BaseViewHolder> {
    private Activity act;

    public NewListAdapter(Activity act,int layoutResId, @Nullable List<ServiceListBean.ListBean> data) {
        super(layoutResId, data);
        this.act =act;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder holder, ServiceListBean.ListBean datas) {
        holder.setText(R.id.tv_news_title,datas.getTitle());
        holder.setText(R.id.tv_news_data,datas.getIn_date());
        ImageView img = holder.getView(R.id.im_news);
        if(TextUtils.isEmpty(datas.getSerimg())){
            img.setBackgroundResource(R.drawable.img_sy_fezn);
        }else {
            x.image().bind(img, datas.getSerimg());
        }
        RelativeLayout news = holder.getView(R.id.news);
        news.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(act, WebNew.class);
            intent.putExtra("url", HttpRequestPort.H5_BASE_URL+"WeiLoginController/wxservicede1.do?id="+datas.getId()+"&fixparam=android");
            act.startActivity(intent);
        });
    }
}
