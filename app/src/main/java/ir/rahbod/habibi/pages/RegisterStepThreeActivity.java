package ir.rahbod.habibi.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.widget.ButtonFont;

public class RegisterStepThreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        ButtonFont btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager.getExtrasPref(RegisterStepThreeActivity.this).putExtra("registered", true);

                Intent nextStepIntent = new Intent(RegisterStepThreeActivity.this, MainActivity.class);
                startActivity(nextStepIntent);
            }
        });
    }
}
