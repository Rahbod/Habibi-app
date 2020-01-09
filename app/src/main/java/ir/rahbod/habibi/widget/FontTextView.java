package ir.rahbod.habibi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class FontTextView extends AppCompatTextView {
    public FontTextView(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf");
        this.setTypeface(face);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf");
        this.setTypeface(face);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
}
