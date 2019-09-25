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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.NotNull;
import org.xutils.x;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.BaseActivity;
import card.com.allcard.activity.CardInfo;
import card.com.allcard.activity.CardOne;
import card.com.allcard.activity.ChangeQuestion;
import card.com.allcard.activity.ForgetPayPass;
import card.com.allcard.activity.FrozenIn;
import card.com.allcard.activity.LoginActivity;
import card.com.allcard.activity.MoneyIn;
import card.com.allcard.activity.MoneyInfo;
import card.com.allcard.activity.MoneyOfCard;
import card.com.allcard.activity.MoreServiceActivity;
import card.com.allcard.activity.TabTwo;
import card.com.allcard.bean.AccStateBean;
import card.com.allcard.bean.TabTwoBean;
import card.com.allcard.net.BaseHttpCallBack;
import card.com.allcard.net.HttpRequestPort;
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
        add.setBackground(ContextCompat.getDrawable(act, R.drawable.btn_fw_tj));
        if (TabTwo.Companion.getEdit() == 1) {
            add.setVisibility(View.VISIBLE);
            for (int i = 0; i < TabTwo.Companion.getTopList().size(); i++) {
                if (data.getId().equals(TabTwo.Companion.getTopList().get(i).getId())) {
                    add.setBackground(ContextCompat.getDrawable(act, R.drawable.btn_fw_tj2));
                    add.setClickable(false);
                }
            }
        } else {
            add.setVisibility(View.GONE);
        }
        add.setOnClickListener(v -> {
            if (TabTwo.Companion.getTopList().size() >= 7) {
                Toast.makeText(act, "最多添加七个服务", Toast.LENGTH_SHORT).show();
            } else {
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
                    Intent intent = new Intent(act, LoginActivity.class);
                    intent.putExtra("from", 1);
                    act.startActivity(intent);
                } else {
                    switch (data.getId()) {
                        case "17":
                            utils.startActivity(MoneyOfCard.class);
                            break;
                        case "20":
                            utils.startActivity(MoneyInfo.class);
                            break;
                        case "18":
                            utils.startActivity(ForgetPayPass.class);
                            break;
                        case "60":
                            utils.startActivity(CardOne.class);
                            break;
                        case "61":
                            utils.startActivity(CardInfo.class);
                            break;
                        case "54":
                            getType("0");
                            break;
                        case "55":
                            getType("1");
                            break;
                        case "58":
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "1");
                            utils.startActivityBy(ChangeQuestion.class, bundle);
                            break;
                        case "15":
                            utils.startActivity(MoneyIn.class);
                            break;
                    }
                }
            } else {
                //跳转不需要登录也能浏览的网页
                switch (data.getId()) {
                    case "17":
                        utils.startActivity(MoneyOfCard.class);
                        break;
                    case "20":
                        utils.startActivity(MoneyInfo.class);
                        break;
                    case "18":
                        utils.startActivity(ForgetPayPass.class);
                        break;
                    case "59":
                        utils.startActivity(MoreServiceActivity.class);
                        break;
                    case "60":
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
        });

    }

    void getType(String type) {
        MMKV mk = BaseActivity.Companion.getMk();
        String userId = mk.decodeString(Tool.INSTANCE.getUSER_ID(), "");
        HttpRequestPort.Companion.getInstance().getAccState(userId, new BaseHttpCallBack(act) {
            @Override
            public void onSuccess(@NotNull String s) {
                super.onSuccess(s);
                AccStateBean bean = JSONObject.parseObject(s, new TypeReference<AccStateBean>() {
                });
                if (bean.getResult().equals("0")) {
                    if (bean.getAccstate().equals("0")) {
                        if (type.equals("0")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "0");
                            utils.startActivityBy(FrozenIn.class, bundle);
                        } else {
                            utils.showToast("您的账户正常，无需解冻");
                        }
                    } else {
                        if (type.equals("0")) {
                            utils.showToast("您的账户已冻结");
                        } else {
                            Bundle bun = new Bundle();
                            bun.putString("type", "1");
                            utils.startActivityBy(FrozenIn.class, bun);
                        }
                    }
                }
            }

            @Override
            public void onError(@NotNull Throwable throwable, boolean b) {
                super.onError(throwable, b);
                utils.showToast("加载失败，请稍后重试");
            }
        });
    }
}
