package card.com.allcard.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import card.com.allcard.R;

/**
 * @author Created by Dream
 */
public class GuidePagerAdapter extends PagerAdapter {
    private ArrayList<Drawable> mData;
    private Context mContext;
    private View.OnClickListener mClickListener;

    public GuidePagerAdapter(View.OnClickListener clickListener, Context context, ArrayList<Drawable> data) {
        mClickListener = clickListener;
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View rootView = View.inflate(mContext, R.layout.item_guide, null);
        ImageView mImgGuide = rootView.findViewById(R.id.img_guide);
        ImageView mStartBtn = rootView.findViewById(R.id.btn_start);
        Drawable drawable = mData.get(position);
        mImgGuide.setImageDrawable(drawable);
        if (position == getCount() - 1) {
            mStartBtn.setVisibility(View.VISIBLE);
            mStartBtn.setOnClickListener(mClickListener);
        }
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
