package ir.rahbod.habibi.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ir.rahbod.habibi.R;

public class RequestStepFourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_step_four);

        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
    }
}
