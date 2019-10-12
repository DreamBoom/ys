package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tencent.mmkv.MMKV;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.BaseActivity;
import card.com.allcard.activity.PayBind;
import card.com.allcard.activity.PayMoney;
import card.com.allcard.bean.GetNum;
import card.com.allcard.bean.PayListBean;
import card.com.allcard.net.BaseHttpCallBack;
import card.com.allcard.net.HttpRequestPort;
import card.com.allcard.tools.Tool;
import card.com.allcard.utils.ActivityUtils;

public class PayAdapter extends CommonAdapter<PayListBean.MemberlinkListBean> {
    private Activity act;
    public ClickListener click;
    private final ActivityUtils utils;
    private String oldName = "";
    public void setClickListener(ClickListener onClickListener) {
        this.click = onClickListener;
    }

    public interface ClickListener {
        void onClickListener(int point, String name);

        void upName();
    }

    public PayAdapter(Activity act, List<PayListBean.MemberlinkListBean> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
        utils = new ActivityUtils(act);
    }

    @Override
    public void convert(ViewHolder holder, PayListBean.MemberlinkListBean data, int position) {
        ImageView edit = holder.getView(R.id.edit);
        TextView name = holder.getView(R.id.name);
        TextView mo = holder.getView(R.id.more);
        TextView sure = holder.getView(R.id.sure);
        TextView cancel = holder.getView(R.id.cancel);
        TextView money = holder.getView(R.id.money);
        EditText et_name = holder.getView(R.id.et_name);
        name.setText(data.getNickName());
        oldName = data.getNickName();
        if (!TextUtils.isEmpty(data.getCertNo())) {
            mo.setVisibility(View.GONE);
            money.setVisibility(View.VISIBLE);
        }
        edit.setOnClickListener(v -> {
                    name.setVisibility(View.GONE);
                    mo.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);
                    sure.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.VISIBLE);
                    et_name.setVisibility(View.VISIBLE);
                }
        );

        sure.setOnClickListener(v -> {
                    String trim = et_name.getText().toString().trim();
                    if (TextUtils.isEmpty(trim)) {
                        utils.showToast("请输入更改昵称");
                        return;
                    }
                    name.setVisibility(View.VISIBLE);
                    mo.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    sure.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    et_name.setVisibility(View.GONE);
                    click.onClickListener(position, trim);
                    et_name.setText("".toCharArray(), 0, "".length());
                    utils.hideSoftKeyboard();
                    up(trim);
                }

        );
        cancel.setOnClickListener(v -> {
                    name.setVisibility(View.VISIBLE);
                    mo.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    sure.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    et_name.setVisibility(View.GONE);
                    utils.hideSoftKeyboard();
                    et_name.setText("".toCharArray(), 0, "".length());
                }

        );
        money.setOnClickListener(v -> {
            Intent intent = new Intent(act, PayMoney.class);
            intent.putExtra("cardNo","");
            intent.putExtra("name", data.getNickName());
            intent.putExtra("num", data.getCertNo());
            act.startActivity(intent);
                }
        );

        mo.setOnClickListener(v -> {
                    Intent intent = new Intent(act, PayBind.class);
                    intent.putExtra("nickName", data.getNickName());
                    act.startActivity(intent);
                }
        );
    }

    private void up(String newName) {
        MMKV mk = BaseActivity.Companion.getMk();
        utils.getProgress(act);
        String userId = mk.decodeString(Tool.INSTANCE.getUSER_ID(), "");
        HttpRequestPort.Companion.getInstance().upName(userId, oldName, newName, "1",new BaseHttpCallBack(act) {
            @Override
            public void success(String data) {
                super.success(data);
                GetNum bean = JSONObject.parseObject(data, new TypeReference<GetNum>() {
                });
                if (bean.getResult().equals("0")) {
                    click.upName();
                } else {
                    utils.showToast("修改失败，请重试");
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                utils.hindProgress();
            }
        });
    }


}
