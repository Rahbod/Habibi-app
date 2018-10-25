package ir.rahbod.habibi.widget;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

public class RadioButtonFont extends AppCompatRadioButton {
    public RadioButtonFont(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf");
        this.setTypeface(face);
    }

    public RadioButtonFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf");
        this.setTypeface(face);
    }

    public RadioButtonFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf");
        this.setTypeface(face);
    }
}
