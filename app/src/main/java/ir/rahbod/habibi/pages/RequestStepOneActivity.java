package ir.rahbod.habibi.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ir.rahbod.habibi.R;

public class RequestStepOneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_step_one);

        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
    }
}
