package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tencent.mmkv.MMKV;

import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;

import card.com.allcard.R;
import card.com.allcard.activity.BaseActivity;
import card.com.allcard.activity.CardInfo;
import card.com.allcard.activity.CardOne;
import card.com.allcard.activity.ChangeCardPass;
import card.com.allcard.activity.ChangeQuestion;
import card.com.allcard.activity.FrozenIn;
import card.com.allcard.activity.LoginActivity;
import card.com.allcard.activity.MainActivity;
import card.com.allcard.activity.MoneyIn;
import card.com.allcard.activity.MoneyInfo;
import card.com.allcard.activity.MoneyOfCard;
import card.com.allcard.bean.MainImgBean;
import card.com.allcard.tools.Tool;
import card.com.allcard.utils.ActivityUtils;

public class ImgAdapter extends CommonAdapter<MainImgBean.SummarydetailBean> {
    private Context mContext;
    private ArrayList<MainImgBean.SummarydetailBean> data;
    private final ActivityUtils utils;

    public ImgAdapter(Context context, ArrayList<MainImgBean.SummarydetailBean> datas, int layoutId) {
        super(context, datas, layoutId);
        this.data = datas;
        this.mContext = context;
        utils = new ActivityUtils((Activity) mContext);
        int size = datas.size();
    }

    @Override
    public void convert(ViewHolder holder, final MainImgBean.SummarydetailBean datas,
                        final int position) throws IOException {
        super.convert(holder, datas, position);
        ImageView img = holder.getView(R.id.img_view);
        LinearLayout view = holder.getView(R.id.ll_icon);
        x.image().bind(img, datas.getSumd_img());
        holder.setText(R.id.img_name, datas.getSumd_name());
        String isLogin = datas.getIs_login();
        MMKV mk = BaseActivity.Companion.getMk();
        view.setOnClickListener((View view1) -> {
            String userId = mk.decodeString(Tool.INSTANCE.getUSER_ID(), "");
            //判断是否点击了更多
            if (position == data.size() - 1) {
                //点击更多，跳转服务界面
                MainActivity.Companion.getTab().setCurrentTab(1);
            } else {
                //判断是否需要登录才能浏览
                if (isLogin.equals("0")) {
                    //需登录才可浏览
                    if (TextUtils.isEmpty(userId)) {
                        //未登录，去登录
                        utils.startActivity(LoginActivity.class);
                    } else {
                        switch (datas.getId()){
                            case "17":
                                utils.startActivity(MoneyOfCard.class);
                                break;
                            case "20":
                                utils.startActivity(MoneyInfo.class);
                                break;
                            case "18":
                                utils.startActivity(ChangeCardPass.class);
                                break;
                            case "59":
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
                            case "58":
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("type","1");
                                utils.startActivityBy(ChangeQuestion.class,bundle1);
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
                    switch (datas.getId()){
                        case "17":
                            utils.startActivity(MoneyOfCard.class);
                            break;
                        case "20":
                            utils.startActivity(MoneyInfo.class);
                            break;
                        case "18":
                            utils.startActivity(ChangeCardPass.class);
                            break;
                        case "59":
                            utils.startActivity(CardOne.class);
                            break;
                        case "54":
                            utils.startActivity(FrozenIn.class);
                            break;
                        case "15":
                            utils.startActivity(MoneyIn.class);
                            break;
                    }
                }
            }
        });
    }
}

