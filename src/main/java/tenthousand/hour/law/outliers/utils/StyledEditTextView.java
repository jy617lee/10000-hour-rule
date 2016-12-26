package tenthousand.hour.law.outliers.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.HashMap;

import tenthousand.hour.law.outliers.R;

/**
 * Created by jeeyu_000 on 2016-09-17.
 */
public class StyledEditTextView extends EditText {
    public StyledEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(fonts == null) {
            fonts = new HashMap<String, Typeface>();
        }
        applyTypeface(context, attrs);
    }

    public StyledEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(fonts == null) {
            fonts = new HashMap<String, Typeface>();
        }
        applyTypeface(context, attrs);
    }

    public StyledEditTextView(Context context) {
        super(context);
        if(fonts == null) {
            fonts = new HashMap<String, Typeface>();
        }
    }

    HashMap<String, Typeface> fonts;
    private void applyTypeface(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.StyledTextView);
        String typefaceName = arr.getString(R.styleable.StyledTextView_font);
        Typeface typeface = null;
        setIncludeFontPadding(false);
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
