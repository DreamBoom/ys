package card.com.allcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.util.List;

/**
 * @author Created by Dream
 */
public  class CommonAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> mDatas;
    private LayoutInflater mInflater;
    private int layoutId;

    CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext=context;
        this.mInflater= LayoutInflater.from(context);
        this.mDatas=datas;
        this.layoutId=layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder=ViewHolder.get(mContext,convertView,parent,layoutId,position);
        try {
            convert(viewHolder,getItem(position),position);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewHolder.getConvertView();
    }

    public void convert(ViewHolder holder,T t,int position) throws IOException {
        convert(holder,t);
    }

    public void convert(ViewHolder holder,T t) throws IOException {
        
    }
}
