package ir.rahbod.habibi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

public class FontTextViewBold extends AppCompatTextView {
    public FontTextViewBold(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans_Bold.ttf");
        this.setTypeface(face);
    }

    public FontTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans_Bold.ttf");
        this.setTypeface(face);
    }

    public FontTextViewBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans_Bold.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
}
