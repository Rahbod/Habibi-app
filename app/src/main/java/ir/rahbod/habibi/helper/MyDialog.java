package ir.rahbod.habibi.helper;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import ir.rahbod.habibi.R;

public class MyDialog {
    public static Dialog dialog;

    public static void show(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loader, null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void dismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
