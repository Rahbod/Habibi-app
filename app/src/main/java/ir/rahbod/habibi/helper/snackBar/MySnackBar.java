package ir.rahbod.habibi.helper.snackBar;

import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.controller.MyApp;

public class MySnackBar implements SnackPresenter {

    private SnackView snackView;

    public MySnackBar(SnackView view) {
        this.snackView = view;
    }

    private void show(final View view) {
        Snackbar snackbar = Snackbar.make(view, "خطا در اتصال به شبکه", Snackbar.LENGTH_INDEFINITE);
        TextView txtMessage = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        txtMessage.setGravity(Gravity.LEFT);
        TextView txtButton = snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
        Typeface face = Typeface.createFromAsset(MyApp.context.getAssets(), "fonts/IRANSans.ttf");
        txtMessage.setTypeface(face);
        txtButton.setTypeface(face);
        txtMessage.setTextColor(MyApp.context.getResources().getColor(R.color.mdtp_white));
        txtButton.setTextColor(MyApp.context.getResources().getColor(R.color.secondColor));
        snackbar.setAction("تلاش مجدد", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackView.retry();
            }
        });
        snackbar.show();
    }

    @Override
    public void showSnack(View layout) {
        show(layout);
    }
}
