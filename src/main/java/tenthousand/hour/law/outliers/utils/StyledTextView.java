package tenthousand.hour.law.outliers.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import java.util.HashMap;

import tenthousand.hour.law.outliers.R;

/**
 * Created by jeeyu_000 on 2016-09-17.
 */
public class StyledTextView extends TextView {

    public StyledTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyTypeface(context, attrs);
    }

    public StyledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyTypeface(context, attrs);
    }

    public StyledTextView(Context context) {
        super(context);
        fonts = new HashMap<String, Typeface>();
    }

    HashMap<String, Typeface> fonts;
    private void applyTypeface(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.StyledTextView);
        String typefaceName = arr.getString(R.styleable.StyledTextView_font);
        Typeface typeface = null;
        setIncludeFontPadding(false);
        setGravity(Gravity.CENTER_VERTICAL);
        try {
            if(fonts.containsKey(typefaceName)){
                typeface = fonts.get(typefaceName);
            }else{
                typeface = Typeface.createFromAsset(context.getAssets(), typefaceName);
                fonts.put(typefaceName, typeface);
            }
            setTypeface(typeface);
        } catch(Exception e) {}
    }

}