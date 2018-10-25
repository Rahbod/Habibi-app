package ir.rahbod.habibi.pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ir.rahbod.habibi.R;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
    }
}
