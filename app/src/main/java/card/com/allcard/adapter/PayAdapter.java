package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private ClickListener click;
    private final ActivityUtils utils;

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
        RelativeLayout item = holder.getView(R.id.item);
        RelativeLayout item2 = holder.getView(R.id.item2);
        ImageView edit = holder.getView(R.id.edit);
        TextView name = holder.getView(R.id.name);
        TextView mo = holder.getView(R.id.more);
        TextView sure = holder.getView(R.id.sure);
        TextView cancel = holder.getView(R.id.cancel);
        TextView money = holder.getView(R.id.money);
        EditText et_name = holder.getView(R.id.et_name);
        sure.setEnabled(false);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(name.getText().toString()) || TextUtils.isEmpty(s.toString())) {
                    sure.setEnabled(false);
                    holder.setTextColor(R.id.sure, ContextCompat.getColor(act, R.color.grey91));
                } else {
                    sure.setEnabled(true);
                    holder.setTextColor(R.id.sure, ContextCompat.getColor(act, R.color.blue));
                }
            }
        });

        name.setText(data.getNickName());
        if (!TextUtils.isEmpty(data.getCertNo())) {
            mo.setVisibility(View.GONE);
            money.setVisibility(View.VISIBLE);
        } else {
            mo.setVisibility(View.VISIBLE);
            money.setVisibility(View.GONE);
        }
        edit.setOnClickListener(v -> {
                    item.setVisibility(View.GONE);
                    item2.setVisibility(View.VISIBLE);
                    String s = name.getText().toString();
                    et_name.setText(s.toCharArray(), 0, s.length());
                }
        );

        sure.setOnClickListener(v -> {
                    String trim = et_name.getText().toString().trim();
                    item.setVisibility(View.VISIBLE);
                    item2.setVisibility(View.GONE);
                    click.onClickListener(position, trim);
                    et_name.setText("".toCharArray(), 0, "".length());
                    utils.hideSoftKeyboard();
                    up(name.getText().toString(), trim);
                }

        );
        cancel.setOnClickListener(v -> {
                    item.setVisibility(View.VISIBLE);
                    item2.setVisibility(View.GONE);
                    utils.hideSoftKeyboard();
                    et_name.setText("".toCharArray(), 0, "".length());
                }

        );
        money.setOnClickListener(v -> {
                    Intent intent = new Intent(act, PayMoney.class);
                    intent.putExtra("cardNo", "");
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

    private void up(String oldName, String newName) {
        MMKV mk = BaseActivity.Companion.getMk();
        utils.getProgress(act);
        String userId = mk.decodeString(Tool.INSTANCE.getUSER_ID(), "");
        HttpRequestPort.Companion.getInstance().upName(userId, oldName, newName, "1", new BaseHttpCallBack(act) {
            @Override
            public void success(String data) {
                super.success(data);
                GetNum bean = JSONObject.parseObject(data, new TypeReference<GetNum>() {
                });
                if (bean.getResult().equals("0")) {
                    click.upName();
                } else {
                    click.upName();
                    utils.showToast(bean.getMessage());
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
