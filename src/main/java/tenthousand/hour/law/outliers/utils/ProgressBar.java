package tenthousand.hour.law.outliers.utils;

/**
 * Created by jeeyu_000 on 2016-12-13.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import tenthousand.hour.law.outliers.R;

/**
 * Created by j617.lee on 2016-12-13.
 */
public class ProgressBar extends View {

    private float max;
    private float curAmount;

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getCurAmount() {
        return curAmount;
    }

    public void setCurAmount(float curAmount) {
        this.curAmount = curAmount;
        this.invalidate();
    }

    private float barStart, barEnd, barHeight, barWidth;
    private float maxHeight;
    //    private TextView amountText, unitText;
//    private String font;
//    private float amountTextStart, amountTextEnd, amountTextSize;
//    private float unitTextStart, unitTextEnd, unitTextSize;
    private int barColor, textColor;

    private Paint barPaint, amountPaint, unitPaint, pinPaint;
    private RectF barRectF;

    private final int defaultMax;
    private final int defaultCurAmount;
    private final int defaultBarColor;
    private final int defaultBarWidth;
    public ProgressBar(Context context){
        this(context, null);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.progressBarStyle);

    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressBar,
                defStyleAttr, 0);
        defaultMax = 100;
        defaultCurAmount = 0;
        defaultBarColor = Color.rgb(66, 145, 241);
        defaultBarWidth = getWidth();
        initView(attributes);
        initPaint();
    }

    private void initView(TypedArray attributes){
        max = attributes.getInt(R.styleable.ProgressBar_max, defaultMax);
        curAmount = attributes.getInt(R.styleable.ProgressBar_cur_amount, defaultCurAmount);
        barWidth = attributes.getDimension(R.styleable.ProgressBar_bar_width, defaultBarWidth);
        barColor = attributes.getInt(R.styleable.ProgressBar_bar_color, defaultBarColor);
        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barRectF = new RectF(0, 0, 0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateHeight();
        canvas.drawRect(barRectF, barPaint);
    }

    public void calculateHeight(){
        barHeight = curAmount / max;
        barRectF.left = getPaddingLeft();
        barRectF.right = barRectF.left + barWidth;
        barRectF.bottom = getBottom();
        barRectF.top = barRectF.bottom - getHeight() * barHeight;
    }

    private void initPaint(){
        barPaint.setColor(barColor);
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    private final int px2dp(int px)
    {
        final float scale = getResources().getSystem().getDisplayMetrics().density;
        return (int) (px * scale);
    }
}