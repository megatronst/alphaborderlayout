package com.i2nexted.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
/**
 * 带自定义边框样式的布局
 * */
public class AlphaBorderLayout extends LinearLayout {
    private int inRadius = 0;
    private int outRadius = 0;
    private int borderColor;
    private float borderAlpha;
    private int borderWidth;

    private Paint mBorderPaint;
    private Path inPath;
    private Path outPath;

    public AlphaBorderLayout(Context context) {
        super(context, null);
    }

    public AlphaBorderLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AlphaBorderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 布局属性相关初始化
     * */
    private void initAttrs(Context context, AttributeSet attrs){
        // 获取各种自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AlphaBorderLayout);
        inRadius = a.getDimensionPixelSize(R.styleable.AlphaBorderLayout_inside_radius, 0);
        outRadius = a.getDimensionPixelSize(R.styleable.AlphaBorderLayout_outside_radius, 0);
        borderColor = a.getColor(R.styleable.AlphaBorderLayout_border_color, Color.BLACK);
        borderAlpha = a.getFloat(R.styleable.AlphaBorderLayout_border_alpha, 0.5f);
        borderWidth = a.getDimensionPixelOffset(R.styleable.AlphaBorderLayout_border_width,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        5,
                        context.getResources().getDisplayMetrics()));
        a.recycle();
        // 根据边框宽度设置布局的padding
        setPadding(borderWidth, borderWidth, borderWidth, borderWidth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){
            initPaint();
        }
    }

    /**
     * 绘制相关初始化
     * */
    private void initPaint(){
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setAlpha((int)(borderAlpha*255));
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setStyle(Paint.Style.FILL);



        RectF inRect = new RectF(getPaddingLeft(),
                getPaddingTop(),
                getMeasuredWidth() - getPaddingRight(),
                getMeasuredHeight() - getPaddingBottom());
        inPath = new Path();
        inPath.addRoundRect(inRect, inRadius,inRadius, Path.Direction.CW);

        RectF outRect = new RectF(0,
                0,
                getMeasuredWidth(),
                getMeasuredHeight());
        outPath = new Path();
        outPath.addRoundRect(outRect, outRadius, outRadius, Path.Direction.CW);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(inPath);
        super.dispatchDraw(canvas);
        canvas.restore();
        canvas.clipPath(outPath);
        canvas.clipPath(inPath,Region.Op.DIFFERENCE);
        canvas.drawRect(0,0,getMeasuredWidth(), getMeasuredHeight(),mBorderPaint);
    }


}
