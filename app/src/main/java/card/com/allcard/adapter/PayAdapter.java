package card.com.allcard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.PayBind;

public class PayAdapter extends CommonAdapter<String> {
    private Activity act;
    public ClickListener click;

    public void setClickListener(ClickListener onClickListener) {
        this.click = onClickListener;
    }

    public interface ClickListener {
        void onClickListener(int point, String name);
    }

    public PayAdapter(Activity act, List<String> data, int layoutId) {
        super(act, data, layoutId);
        this.act = act;
    }

    @Override
    public void convert(ViewHolder holder, String data, int position) {
        ImageView edit = holder.getView(R.id.edit);
        TextView name = holder.getView(R.id.name);
        TextView mo = holder.getView(R.id.more);
        TextView sure = holder.getView(R.id.sure);
        TextView cancel = holder.getView(R.id.cancel);
        TextView money = holder.getView(R.id.money);
        EditText et_name = holder.getView(R.id.et_name);
        name.setText(data);
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
                        Toast.makeText(act, "请输入更改昵称", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    name.setVisibility(View.VISIBLE);
                    mo.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    sure.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    et_name.setVisibility(View.GONE);
                    click.onClickListener(position, trim);
                }

        );
        cancel.setOnClickListener(v -> {
                    name.setVisibility(View.VISIBLE);
                    mo.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    sure.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    et_name.setVisibility(View.GONE);
                }

        );
        money.setOnClickListener(v -> {

                }
        );

        mo.setOnClickListener(v -> {
                    Intent intent = new Intent(act, PayBind.class);
                    act.startActivity(intent);
                }
        );
    }

}
