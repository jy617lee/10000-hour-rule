import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import tenthousand.hour.law.outliers.R;

/**
 * Created by j617.lee on 2016-12-13.
 */
public class ProgressBar extends View {

    private float max;
    private float curAmount;
    private float barHeight, barWidth;
    private String font;
    private float amountTextX, amountTextY, amountTextSize, amountTextWidth;
    String amountText, unitText;
    private float unitTextX, unitTextY, unitTextSize, unitTextWidth;
    private int barColor, textColor;

    private Paint barPaint, amountTextPaint, unitTextPaint;
    private RectF barRectF;

    private final int defaultMax = 100;
    private final int defaultCurAmount = 0;
    private final int defaultBarColor;
    private final int defaultBarWidth;
    private final int defaultTextColor;
    private final int defaultAmountTextSize = 20;
    private final String defaultAmountText = "0";
    private final String defaultUnitText = "hour";
    private final int defaultUnitTextSize = 10;

    //////////////
    private Paint pinPaint;
    private float pinWidth, pinHeight;
    private Drawable pinImg = null;
    private RectF pinRectF;

    private String recentDate;
    private float recentDateX, recentDateY, recentDateSize, recentDateWidth;
    Paint recentDatePaint;


    private final int defaultPinWidth = 30;
    private final int defaultPinHeight = 30;
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

        defaultBarColor = Color.rgb(66, 145, 241);
        defaultBarWidth = getWidth();
        defaultTextColor = getSolidColor();
        initAttrs(attributes);

    }

    private void initAttrs(TypedArray attributes) {
        max = attributes.getInt(R.styleable.ProgressBar_max, defaultMax);
        curAmount = attributes.getInt(R.styleable.ProgressBar_cur_amount, defaultCurAmount);
        if(curAmount > max) curAmount = max;
        barWidth = attributes.getDimension(R.styleable.ProgressBar_bar_width, defaultBarWidth);
        barColor = attributes.getInt(R.styleable.ProgressBar_bar_color, defaultBarColor);
        textColor = attributes.getInt(R.styleable.ProgressBar_text_color, defaultTextColor);
        amountTextSize = attributes.getDimension(R.styleable.ProgressBar_amount_text_size, defaultAmountTextSize);
        amountText = attributes.getString(R.styleable.ProgressBar_amount_text);
        unitTextSize = attributes.getDimension(R.styleable.ProgressBar_unit_text_size, defaultUnitTextSize);
        unitText =  attributes.getString(R.styleable.ProgressBar_unit_text);
        if(unitText == null){
            unitText = defaultUnitText;
        }

        pinImg = attributes.getDrawable(R.styleable.ProgressBar_pin_img);
        if(pinImg != null){
            pinWidth = attributes.getDimension(R.styleable.ProgressBar_pin_width, defaultPinWidth);
            pinHeight = attributes.getDimension(R.styleable.ProgressBar_pin_width, defaultPinHeight);
        }

        //일단
        recentDateSize = unitTextSize;
        if(recentDate == null){
            recentDate = getDate();
        }
    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("mm/dd");
        Date date = new Date();
        return dateFormat.format(date).toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();
        calculateHeight();
        canvas.drawRect(barRectF, barPaint);
        if(amountText != null){
            canvas.drawText(amountText, amountTextX, amountTextY, amountTextPaint);
            canvas.drawText(unitText, unitTextX, unitTextY, unitTextPaint);
        }
        if(pinImg != null){
            Bitmap pinImgBitmap = drawableToBitmap(pinImg);
            canvas.drawBitmap(pinImgBitmap, null, pinRectF, pinPaint);
            canvas.drawText(recentDate, recentDateX, recentDateY, recentDatePaint);
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable){
        Bitmap  bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public void calculateHeight(){
        barHeight = getHeight() * curAmount / max;
        barRectF.left = getPaddingLeft();
        barRectF.right = barRectF.left + barWidth;
        barRectF.bottom = getHeight();
        barRectF.top = barRectF.bottom - barHeight;

        if(amountText != null){
            if(amountTextWidth >= barWidth){
                amountTextX = barRectF.left;
            }else{
                amountTextX = barRectF.left + (barWidth - amountTextWidth) / 2;
            }
            unitTextX = barRectF.left + (barWidth - unitTextWidth) / 2;
            if((curAmount / max)*100 < 30){
                unitTextY = barRectF.top;
                amountTextY = barRectF.top - unitTextSize;
            }else{
                unitTextY = barRectF.bottom - (barHeight/2 - (amountTextSize + unitTextSize)/2);
                amountTextY = unitTextY - unitTextSize;
            }
        }

        if(pinImg != null){
            pinRectF.left = barRectF.right;
            pinRectF.right = pinRectF.left + pinWidth;
            pinRectF.top = barRectF.top - pinHeight / 2;
            pinRectF.bottom = barRectF.top + pinHeight / 2;

            recentDateX = pinRectF.right;
            recentDateY = pinRectF.bottom - recentDateSize/2;
        }
    }

    private void initPaint(){
        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barRectF = new RectF(0, 0, 0, 0);
        barPaint.setColor(barColor);

        if(amountText != null){
            amountTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            amountTextPaint.setColor(textColor);
            amountTextPaint.setTextSize(amountTextSize);
            amountTextWidth = amountTextPaint.measureText(amountText);

            unitTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            unitTextPaint.setColor(textColor);
            unitTextPaint.setTextSize(unitTextSize);
            unitTextWidth = unitTextPaint.measureText(unitText);
        }

        if(pinImg != null){
            pinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            pinRectF = new RectF(0, 0, 0, 0);

            recentDatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            recentDatePaint.setColor(textColor);
            recentDatePaint.setTextSize(unitTextSize);
            recentDateWidth = recentDatePaint.measureText(recentDate);
        }
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public void setCurAmount(int curAmount, String recentDate){
        this.curAmount = curAmount;
        this.amountText = curAmount+"";
        this.recentDate = recentDate;
        this.invalidate();
    }
}