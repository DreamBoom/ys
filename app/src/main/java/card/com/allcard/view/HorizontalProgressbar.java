package card.com.allcard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import card.com.allcard.R;

/**
 * Created by 49927 on 2017/3/15.
 */

public class HorizontalProgressbar extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;//SP
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#268ce4");
    private static final int DEFAULT_COLOR_UNREACH = Color.parseColor("#e2e2e2");
    private static final int DEFAULT_HEIGTH_UNREACH = 3;//dP
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACH = 3;//dP
    private static final int DEFAULT_TEXT_OFFSET = 8;//dP

    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnReachColor = DEFAULT_COLOR_UNREACH;
    protected int mUnReachHeigth = dp2px(DEFAULT_HEIGTH_UNREACH);
    protected int mReachColor = DEFAULT_COLOR_REACH;
    protected int mReachHeigth = dp2px(DEFAULT_HEIGHT_REACH);
    protected int mTextOffSet = dp2px(DEFAULT_TEXT_OFFSET);

    protected Paint mPaint = new Paint();
    protected int mRealWidth;

    public HorizontalProgressbar(Context context) {
        this(context, null);
    }

    public HorizontalProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);
    }

    //获取自定义属性
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressbar);
        mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgressbar_progress_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.HorizontalProgressbar_progress_text_color, mTextColor);
        mTextOffSet = (int) ta.getDimension(R.styleable.HorizontalProgressbar_progress_text_offset, mTextOffSet);
        mUnReachColor = ta.getColor(R.styleable.HorizontalProgressbar_progress_unreach_color, mUnReachColor);
        mUnReachHeigth = (int) ta.getDimension(R.styleable.HorizontalProgressbar_progress_unreach_heigth, mUnReachHeigth);
        mReachColor = ta.getColor(R.styleable.HorizontalProgressbar_progress_reach_color, mReachColor);
        mReachHeigth = (int) ta.getDimension(R.styleable.HorizontalProgressbar_progress_reach_heigth, mReachHeigth);
        ta.recycle();
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置宽度的模式和值
        //int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        //确定宽和高
        setMeasuredDimension(widthVal,height);
        //绘图区域宽度
        mRealWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        //MeasureSpec.EXACTLY 为确定值 textHeight 为测量值
        if(mode == MeasureSpec.EXACTLY){
            result = size;
        }else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            //Math.max 两者取最大
            result = getPaddingTop()+getPaddingBottom()+ Math.max(Math.max(mReachHeigth,mUnReachHeigth), Math.abs(textHeight));
        }
        //测量值不能超过给定size值 确定最终result值
        if(mode == MeasureSpec.AT_MOST){
            result = Math.min(result,size);
        }
        return (result);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        //确定绘制坐标
        canvas.translate(getPaddingLeft(),getHeight()/2);
        boolean noNeedUnRech = false;
        // 一、绘制当前进度条
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        //当前进度长度
        float radio = getProgress() * 1.0f / getMax();

        float progressX = radio * mRealWidth;
        if(progressX + textWidth >mRealWidth){
            progressX = mRealWidth - textWidth;
            noNeedUnRech = true;
        }
        //真实进度长度
        float endX = progressX - mTextOffSet / 2;
        if(endX > 0){
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeigth);
            canvas.drawLine(0,0,endX,0,mPaint);
        }
        // 二、绘制字体
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent())/2);
        canvas.drawText(text,progressX,y,mPaint);
        // 三、绘制剩余进度
        if(!noNeedUnRech){
            float start = progressX + mTextOffSet / 2 +textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeigth);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }
        canvas.restore();
    }

    //单位转换
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    //单位转换
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
}
