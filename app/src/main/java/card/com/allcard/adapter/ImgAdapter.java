package card.com.allcard.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.NotNull;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;

import card.com.allcard.R;
import card.com.allcard.activity.BaseActivity;
import card.com.allcard.activity.CardInfo;
import card.com.allcard.activity.CardOne;
import card.com.allcard.activity.ChangeQuestion;
import card.com.allcard.activity.ForgetPayPass;
import card.com.allcard.activity.FrozenIn;
import card.com.allcard.activity.LoginActivity;
import card.com.allcard.activity.MainActivity;
import card.com.allcard.activity.MoneyIn;
import card.com.allcard.activity.MoneyInfo;
import card.com.allcard.activity.MoneyOfCard;
import card.com.allcard.activity.MoreServiceActivity;
import card.com.allcard.activity.Search;
import card.com.allcard.activity.WebNew;
import card.com.allcard.activity.WebOther;
import card.com.allcard.bean.AccStateBean;
import card.com.allcard.bean.MainImgBean;
import card.com.allcard.net.BaseHttpCallBack;
import card.com.allcard.net.HttpRequestPort;
import card.com.allcard.tools.Tool;
import card.com.allcard.utils.ActivityUtils;
import card.com.allcard.utils.LogUtils;

public class ImgAdapter extends CommonAdapter<MainImgBean.SummarydetailBean> {
    private Activity mContext;
    private ArrayList<MainImgBean.SummarydetailBean> data;
    private final ActivityUtils utils;

    public ImgAdapter(Activity context, ArrayList<MainImgBean.SummarydetailBean> datas, int layoutId) {
        super(context, datas, layoutId);
        this.data = datas;
        this.mContext = context;
        utils = new ActivityUtils(mContext);
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
                        switch (datas.getId()) {
                            case "15":
                                String s = mk.decodeString(Tool.INSTANCE.getIS_AUTH(), "");
                                if (s.equals("1")) {
                                    utils.showToast("请先进行实名认证");
                                } else {
                                    Bundle b = new Bundle();
                                    b.putString("cardNo", "");
                                    b.putString("nickName", "");
                                    b.putString("flag", "0");
                                    utils.startActivityBy(MoneyIn.class, b);
                                }
                                break;
                            case "16":
                                utils.showToast("敬请期待");
                                break;
                            case "17":
                                String s1 = mk.decodeString(Tool.INSTANCE.getIS_AUTH(), "");
                                if (s1.equals("1")) {
                                    utils.showToast("请先进行实名认证");
                                } else {
                                    utils.startActivity(MoneyOfCard.class);
                                }
                                break;
                            case "18":
                                String s2 = mk.decodeString(Tool.INSTANCE.getIS_AUTH(), "");
                                if (s2.equals("1")) {
                                    utils.showToast("请先进行实名认证");
                                } else {
                                    utils.startActivity(ForgetPayPass.class);
                                }
                                break;
                            case "20":
                                utils.startActivity(MoneyInfo.class);
                                break;
                            case "54":
                                String ss = mk.decodeString(Tool.INSTANCE.getIS_AUTH(), "");
                                if (ss.equals("1")) {
                                    utils.showToast("请先进行实名认证");
                                } else {
                                    getType("0");
                                }
                                break;
                            case "55":
                                String sss = mk.decodeString(Tool.INSTANCE.getIS_AUTH(), "");
                                if (sss.equals("1")) {
                                    utils.showToast("请先进行实名认证");
                                } else {
                                    getType("1");
                                }
                                break;
                            case "57":
                                String q = mk.decodeString(Tool.INSTANCE.getIS_AUTH(), "");
                                if (q.equals("1")) {
                                    utils.showToast("请先进行实名认证");
                                } else {
                                    utils.startActivity(CardInfo.class);
                                }
                                break;
                            case "58":
                                utils.startActivity(CardOne.class);
                                break;
                            case "59":
                                utils.startActivity(Search.class);
                                break;
                            default:
                                if(datas.getIs_post()=="0"){
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("url", datas.getSumm_link());
                                    utils.startActivityBy(WebOther.class, bundle1);
                                }else {
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("url", datas.getSumm_link());
                                    utils.startActivityBy(WebNew.class, bundle1);
                                }
                                break;
                        }
                    }
                } else {
                    //跳转不需要登录也能浏览的网页
                    if ("43".equals(datas.getId())) {
                        utils.startActivity(MoreServiceActivity.class);
                    } else if (datas.getId().equals("44")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "1");
                        utils.startActivityBy(ChangeQuestion.class, bundle);
                    } else {
                        if(datas.getIs_post()=="0"){
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("url", datas.getSumm_link());
                            utils.startActivityBy(WebOther.class, bundle1);
                        }else {
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("url", datas.getSumm_link());
                            utils.startActivityBy(WebNew.class, bundle1);
                        }
                    }
                }
            }
        });
    }

    void getType(String type) {
        MMKV mk = BaseActivity.Companion.getMk();
        String userId = mk.decodeString(Tool.INSTANCE.getUSER_ID(), "");
        HttpRequestPort.Companion.getInstance().getAccState(userId, new BaseHttpCallBack(mContext) {
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

