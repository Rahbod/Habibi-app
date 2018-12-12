package ir.rahbod.habibi.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.widget.ButtonFont;

public class RegisterStepTowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);

//        ButtonFont btnContinue = findViewById(R.id.btnContinue2);
//        btnContinue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent nextStepIntent = new Intent(RegisterStepTowActivity.this, RegisterStepThreeActivity.class);
//                startActivity(nextStepIntent);
//            }
//        });
    }
}
