package card.com.allcard.adapter;

import android.app.Activity;

import java.util.List;

public class WaitPayAdapter extends CommonAdapter<String> {
    private Activity mContext;
    public WaitPayAdapter(Activity act, List<String> data, int layoutId) {
        super(act, data, layoutId);
        this.mContext = act;
    }
    @Override
    public void convert(ViewHolder holder,String datas) {
     //   holder.setText(R.id.type,datas.getTrCodeName());
    }

}
