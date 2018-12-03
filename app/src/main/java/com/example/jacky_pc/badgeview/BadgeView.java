package com.example.jacky_pc.badgeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 小红点
 */
public class BadgeView extends View {
    public final static int DRAW_RED_POINT = -1;//只画小红点（里面是画空字符）

    private Paint mPaint;
    private int mNumber = DRAW_RED_POINT;//传进来的数字，默认是-1
    private String mShowText = "";//显示的字符

    private Rect mRect;//用来获取文字的宽和高,一位数字或者空字符的时候使用
    private RectF mRectF;//用来获取文字的宽和高，两位数字以上的时候使用

    //一个数字或者空字符的内边距,因为是圆，只需要一个内边距，以字符的最长一边来加上内边距的2倍来计算圆的半径
    private int mCirclePadding;
    private int mTextSize;
    private int mTextColor;
    private int mBackgroundColor;

    public BadgeView(Context context) {
        super(context);
        init(null);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) {
            mCirclePadding = Utils.dp2px(getContext(), 4);
            mTextSize = Utils.sp2px(getContext(), 10);
            mTextColor = Color.WHITE;
            mBackgroundColor = Color.RED;
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BadgeView);
            if (typedArray != null) {
                //获得的值是px
                mCirclePadding = (int) typedArray.getDimension(R.styleable.BadgeView_circle_padding, 4);
                mTextSize = (int) typedArray.getDimension(R.styleable.BadgeView_text_size, 10);
                mTextColor = typedArray.getColor(R.styleable.BadgeView_text_color, Color.WHITE);
                mBackgroundColor = typedArray.getColor(R.styleable.BadgeView_background_color, Color.RED);
                typedArray.recycle();
            }
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRect = new Rect();
        mRectF = new RectF();
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPaint.getTextBounds(mShowText, 0, mShowText.length(), mRect);
        int w = mRect.width();
        int h = mRect.height();
        if (mNumber < 10) {
            //设置控件的大小
            int width = (w > h ? w : h) + mCirclePadding * 2;
            int height = width;
            setMeasuredDimension(width, height);
        }
        if (mNumber >= 10) {
            setMeasuredDimension(w + (mCirclePadding + Utils.dp2px(getContext(), 1)) * 2, h + mCirclePadding * 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mNumber < 10) {
            //先画底色
            mPaint.setColor(mBackgroundColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
            //再画文字
            mPaint.getTextBounds(mShowText, 0, mShowText.length(), mRect);
            int w = mRect.width();
            int h = mRect.height();
            mPaint.setColor(mTextColor);
            //x,y是左下角的坐标
            canvas.drawText(mShowText, getWidth() / 2 - w / 2, getWidth() / 2 + h / 2, mPaint);
        }

        if (mNumber >= 10) {
            //先画底色
            mPaint.setColor(mBackgroundColor);
            mPaint.setStyle(Paint.Style.FILL);
            mRectF.left = 0;
            mRectF.top = 0;
            mRectF.right = getWidth();
            mRectF.bottom = getHeight();
            canvas.drawRoundRect(mRectF, getHeight() / 2, getHeight() / 2, mPaint);
            //再画文字
            mPaint.getTextBounds(mShowText, 0, mShowText.length(), mRect);
            mPaint.setColor(mTextColor);
            //x,y是左下角的坐标
            canvas.drawText(mShowText, mCirclePadding + Utils.dp2px(getContext(), 1), getHeight() - mCirclePadding, mPaint);
        }
    }

    public void setNumber(int number) {
        mNumber = number;
        if (mNumber == -1) {
            mShowText = "";
            requestLayout();
            invalidate();
            setVisibility(VISIBLE);
            return;
        }

        if (mNumber <= 0) {
            mShowText = "";
            setVisibility(GONE);
            return;
        }

        //一位数的时候画圆
        if (mNumber < 10) {
            mShowText = mNumber + "";
            requestLayout();
            invalidate();
            setVisibility(VISIBLE);
        }

        //两位数以上画椭圆
        if (mNumber >= 10) {
            if (mNumber < 100) {
                mShowText = mNumber + "";
            }
            if (mNumber >= 100) {
                mShowText = "99+";
            }
            requestLayout();
            invalidate();
            setVisibility(VISIBLE);
        }

    }

    public int getNumber() {
        return mNumber;
    }
}
