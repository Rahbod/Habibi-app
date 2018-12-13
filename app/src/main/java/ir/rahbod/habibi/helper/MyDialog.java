package ir.rahbod.habibi.helper;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import ir.rahbod.habibi.R;

public class MyDialog {
    public static Dialog dialog;

    public static void show(Context context) {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loader, null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void dismiss() {
        dialog.dismiss();
    }
}
