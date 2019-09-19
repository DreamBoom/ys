package card.com.allcard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author Created by Dream
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;


    private View mConvertView;

    View getConvertView() {
        return mConvertView;
    }

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);

    }

    /**
     * ViewHolder入口
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 通过ViewId获取控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置TextView的值

     * @param viewId
     * @param value
     * @return
     */
    public ViewHolder setText(int viewId, String value) {
        TextView textView = getView(viewId);
        textView.setText(value);
        return this;
    }
    public ViewHolder setTextColor(int viewId,int color) {
        TextView textView = getView(viewId);
        textView.setTextColor(color);
        return this;
    }
    @SuppressLint("SetTextI18n")
    public ViewHolder setIntText(int viewId, int value) {
        TextView textView = getView(viewId);
        textView.setText(value+"");
        return this;
    }
    public ViewHolder setOnclick(int viewId, boolean bool) {
        TextView textView = getView(viewId);
        textView.setClickable(bool);
        return this;
    }
    public ViewHolder setimageOnclick(int viewId, boolean bool) {
        ImageView imageView = getView(viewId);
        imageView.setClickable(bool);
        return this;
    }
    public ViewHolder setImageResource(int viewId,int resId)
    {
        ImageView view=getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId,Bitmap bitmap)
    {
        ImageView view=getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public void setTextBackground(int viewId, int background) {
        TextView textView = getView(viewId);
        textView.setBackgroundResource(background);
    }

}
