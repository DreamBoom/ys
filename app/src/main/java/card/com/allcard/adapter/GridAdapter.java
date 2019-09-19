package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.mmkv.MMKV;

import org.xutils.x;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.BaseActivity;
import card.com.allcard.activity.CardInfo;
import card.com.allcard.activity.CardOne;
import card.com.allcard.activity.CardProgress;
import card.com.allcard.activity.ChangeCardPass;
import card.com.allcard.activity.FrozenIn;
import card.com.allcard.activity.LoginActivity;
import card.com.allcard.activity.MoneyIn;
import card.com.allcard.activity.MoneyInfo;
import card.com.allcard.activity.MoneyOfCard;
import card.com.allcard.activity.TabTwo;
import card.com.allcard.bean.TabTwoBean;
import card.com.allcard.tools.Tool;
import card.com.allcard.utils.ActivityUtils;

public class GridAdapter extends CommonAdapter<TabTwoBean.ListBean.IconAllBean.SummarydetailListBean> {
    private Activity act;
    public AllClickListener allClick;
    private final ActivityUtils utils;
    void setallClickListener(AllClickListener onClickListener) {
        this.allClick = onClickListener;
    }

    public interface AllClickListener {
        void onClickListener();
    }
    public GridAdapter(Activity act, List<TabTwoBean.ListBean.IconAllBean.SummarydetailListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
        utils = new ActivityUtils(act);
    }

    @Override
    public void convert(ViewHolder holder, TabTwoBean.ListBean.IconAllBean.SummarydetailListBean data) {
        holder.setText(R.id.text, data.getSumd_name());
        ImageView img = holder.getView(R.id.img);
        RelativeLayout grItem = holder.getView(R.id.gr_item);
        x.image().bind(img, data.getSumd_img());
        ImageView add = holder.getView(R.id.add);
        add.setBackground(ContextCompat.getDrawable(act,R.drawable.btn_fw_tj));
        if (TabTwo.Companion.getEdit() == 1) {
            add.setVisibility(View.VISIBLE);
            for (int i = 0; i<TabTwo.Companion.getTopList().size(); i++){
                if(data.getId().equals(TabTwo.Companion.getTopList().get(i).getId())){
                    add.setBackground(ContextCompat.getDrawable(act,R.drawable.btn_fw_tj2));
                    add.setClickable(false);
                }
            }
        } else {
            add.setVisibility(View.GONE);
        }
        add.setOnClickListener(v -> {
            if(TabTwo.Companion.getTopList().size()>=7){
                Toast.makeText(act,"最多添加七个服务",Toast.LENGTH_SHORT).show();
            }else {
                TabTwoBean.ListBean.SummarydetailBean bean = new TabTwoBean.ListBean.SummarydetailBean();
                bean.setId(data.getId());
                bean.setSumd_img(data.getSumd_img());
                bean.setIs_login(data.getIs_login());
                bean.setIs_web(data.getIs_web());
                bean.setSumd_name(data.getSumd_name());
                bean.setSumm_link(data.getSumm_link());
                TabTwo.Companion.getTopList().add(bean);
                allClick.onClickListener();
            }
        });
        MMKV mk = BaseActivity.Companion.getMk();
        grItem.setOnClickListener(v -> {
            //判断网页是否需要登录才能浏览
            String userId = mk.decodeString(Tool.INSTANCE.getUSER_ID(), "");
            if (data.getIs_login().equals("0")) {
                //需登录才可浏览
                if (TextUtils.isEmpty(userId)) {
                    //未登录，去登录
                    Intent intent = new Intent(act,LoginActivity.class);
                    intent.putExtra("from", 1);
                    act.startActivity(intent);
                } else {
                    switch (data.getId()){
                        case "17":
                            utils.startActivity(MoneyOfCard.class);
                            break;
                        case "20":
                            utils.startActivity(MoneyInfo.class);
                            break;
                        case "18":
                            utils.startActivity(ChangeCardPass.class);
                            break;
                        case "10":
                            utils.startActivity(CardOne.class);
                            break;
                        case "54":
                            Bundle bundle = new Bundle();
                            bundle.putString("type","0");
                            utils.startActivityBy(FrozenIn.class,bundle);
                            break;
                        case "55":
                            Bundle bun = new Bundle();
                            bun.putString("type","1");
                            utils.startActivityBy(FrozenIn.class,bun);
                            break;
                        case "44":
                            utils.startActivity(CardProgress.class);
                            break;
                        case "15":
                            utils.startActivity(MoneyIn.class);
                            break;
                        case "11":
                            utils.startActivity(CardInfo.class);
                            break;
                    }
                }
            }else {
                //跳转不需要登录也能浏览的网页
                switch (data.getId()){
                    case "17":
                        utils.startActivity(MoneyOfCard.class);
                        break;
                    case "20":
                        utils.startActivity(MoneyInfo.class);
                        break;
                    case "18":
                        utils.startActivity(ChangeCardPass.class);
                        break;
                    case "10":
                        utils.startActivity(CardOne.class);
                        break;
                    case "54":
                        utils.startActivity(FrozenIn.class);
                        break;
                    case "44":
                        utils.startActivity(CardProgress.class);
                        break;
                    case "15":
                        utils.startActivity(MoneyIn.class);
                        break;
                }
            }
        });
    }
}
