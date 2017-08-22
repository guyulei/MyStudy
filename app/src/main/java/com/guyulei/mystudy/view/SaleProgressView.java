package com.guyulei.mystudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.guyulei.mystudy.R;

import java.text.DecimalFormat;

/**
 * Created by A on 2017/8/22.
 */

public class SaleProgressView extends View {

    private int     width;
    private int     height;
    private float   radius;
    private RectF   bgRectF;//背景矩形  大背景
    private float   sideWidth;//边框粗细
    private Paint   sidePaint;//边框所在的矩形
    private int     sideColor;//边框颜色
    private int     textColor;//文字颜色
    private String  nearOverText;
    private String  overText;
    private float   textSize;
    private boolean isNeedAnim;

    private Paint              srcPaint;
    private Paint              textPaint;
    private PorterDuffXfermode mPorterDuffXfermode;
    private float              nearOverTextWidth;
    private float              overTextWidth;
    private float              baseLineY;

    private int    currentCount;//当前卖出数
    private int    progressCount;//动画需要的
    private int    totalCount;//商品总数
    private float  scale;//售出比例
    private Bitmap bgBitmap;
    private Bitmap bgSrc;
    private Bitmap fgSrc;

    public SaleProgressView(Context context) {
        this(context, null);
    }

    public SaleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SaleProgressView);
        sideColor = ta.getColor(R.styleable.SaleProgressView_sideColor, 0xffff3c32);//边框颜色
        sideWidth = ta.getDimension(R.styleable.SaleProgressView_sideWidth, dp2px(2));//边框大小
        textColor = ta.getColor(R.styleable.SaleProgressView_textColor, 0xffff3c32);
        textSize = ta.getDimension(R.styleable.SaleProgressView_textSize, sp2px(16));
        overText = ta.getString(R.styleable.SaleProgressView_overText);//售罄
        nearOverText = ta.getString(R.styleable.SaleProgressView_nearOverText);//即将售罄
        isNeedAnim = ta.getBoolean(R.styleable.SaleProgressView_isNeedAnim, true);
        ta.recycle();
    }

    private void initPaint() {
        //边框画笔
        sidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//anti alias flag
        sidePaint.setStyle(Paint.Style.STROKE);//边框
        sidePaint.setStrokeWidth(sideWidth);
        sidePaint.setColor(sideColor);
        sidePaint.setAntiAlias(true);
        //画里面的背景
        srcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        srcPaint.setAntiAlias(true);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        //画汉字
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        nearOverTextWidth = textPaint.measureText(nearOverText);
        overTextWidth = textPaint.measureText(overText);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取 view 的宽高
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //圆角半径
        radius = height / 2.0f;
        //留出一定的间隙，避免边框被切掉一部分
        if (bgRectF == null) {
            bgRectF = new RectF(sideWidth, sideWidth, width - sideWidth, height - sideWidth);
            //bgRectF = new RectF(0, 0, width - 0, height - 0);
        }
        //画文字 y轴坐标 通用
        if (baseLineY == 0.0f) {
            Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
            baseLineY = height / 2 - (fm.descent / 2 + fm.ascent / 2);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isNeedAnim) {
            progressCount = currentCount;
        }
        if (totalCount == 0) {
            scale = 0.0f;
        } else {
            //格式化数字
            scale = Float.parseFloat(new DecimalFormat("0.00").format((float) progressCount / (float) totalCount));
        }
        drawSide(canvas);//画边框
        drawBg(canvas);//画里面 的背景
        drawFg(canvas);//画进度条
        drawText(canvas);

        //这里是为了演示动画方便，实际开发中进度只会增加
        if (progressCount != currentCount) {
            if (progressCount < currentCount) {
                progressCount++;
            } else {
                progressCount--;
            }
            postInvalidate();
        }
    }

    //绘制背景边框
    private void drawSide(Canvas canvas) {
        canvas.drawRoundRect(bgRectF, radius, radius, sidePaint);
    }

    //绘制背景
    private void drawBg(Canvas canvas) {
        //先创建一个空的 bitmap 装载在一个新的画布上，然后在画布上绘制一个 圆角矩形，以不遮挡之前绘制好的背景边框为准
        if (bgBitmap == null) {
            bgBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//最外层 bitmap
        }
        Canvas bgCanvas = new Canvas(bgBitmap);//新的 画布 画里面的背景
        bgCanvas.drawRoundRect(bgRectF, radius, radius, srcPaint);
        //为srcPaint设置SRC_IN的图形混合模式,在该画布上把我们做好的条纹图绘制进来,之前绘制的圆角矩形就做为dst,后来绘制的条纹图就做为src
        srcPaint.setXfermode(mPorterDuffXfermode);
        if (bgSrc == null) {
            bgSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.bg);//图片 转换成 bitmap
        }
        //重叠的部分始终是圆角矩形范围,条纹图会把这个圆角矩形填充满（因为在绘制条纹图时设置的 dstRectF 和圆角矩形一致）。
        bgCanvas.drawBitmap(bgSrc, null, bgRectF, srcPaint);//src
        canvas.drawBitmap(bgBitmap, 0, 0, null);//dst
        srcPaint.setXfermode(null);
    }

    //绘制进度条
    private void drawFg(Canvas canvas) {
        if (scale == 0.0f) {
            return;
        }
        Bitmap fgBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas fgCanvas = new Canvas(fgBitmap);
        if (fgSrc == null) {
            fgSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.fg);
        }
        fgCanvas.drawRoundRect(
                new RectF(sideWidth, sideWidth, (width - sideWidth) * scale, height - sideWidth),
                radius, radius, srcPaint);
        srcPaint.setXfermode(mPorterDuffXfermode);
        fgCanvas.drawBitmap(fgSrc, null, bgRectF, srcPaint);
        canvas.drawBitmap(fgBitmap, 0, 0, null);
        srcPaint.setXfermode(null);
    }

    //绘制文字信息
    private void drawText(Canvas canvas) {
        String scaleText = new DecimalFormat("#%").format(scale);//百分数  右边
        String saleText = String.format("已抢%s件", progressCount);//左边 汉字
        Bitmap textBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textPaint.setColor(textColor);
        float scaleTextWidth = textPaint.measureText(scaleText);

        if (scale < 0.8f) {
            textCanvas.drawText(saleText, dp2px(10), baseLineY, textPaint);//左边
            textCanvas.drawText(scaleText, width - scaleTextWidth - dp2px(10), baseLineY, textPaint);//右边
        } else if (scale < 1.0f) {
            textCanvas.drawText(nearOverText, width / 2 - nearOverTextWidth / 2, baseLineY, textPaint);//即将售罄
            textCanvas.drawText(scaleText, width - scaleTextWidth - dp2px(10), baseLineY, textPaint);//右边
        } else {
            textCanvas.drawText(overText, width / 2 - overTextWidth / 2, baseLineY, textPaint);//售罄
        }

        //把白色圆角矩形当做src,之前写好的红色文字为dst,显示出重叠部分的  src 就可以实现该效果了。
        textPaint.setXfermode(mPorterDuffXfermode);
        textPaint.setColor(Color.WHITE);
        textCanvas.drawRoundRect(
                new RectF(sideWidth, sideWidth, (width - sideWidth) * scale, height - sideWidth),
                radius, radius, textPaint);//src
        canvas.drawBitmap(textBitmap, 0, 0, null);//dst
        textPaint.setXfermode(null);
    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public void setTotalAndCurrentCount(int totalCount, int currentCount) {
        this.totalCount = totalCount;
        if (currentCount > totalCount) {
            currentCount = totalCount;
        }
        this.currentCount = currentCount;
        postInvalidate();
    }
}
