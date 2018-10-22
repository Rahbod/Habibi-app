package ir.rahbod.habibi.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ir.rahbod.habibi.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //change back icon
        ImageView back = findViewById(R.id.btnBack);
        back.setImageResource(R.drawable.wrench_icon);
        back.setPadding(34, 34, 34, 34);

        ImageView menu = findViewById(R.id.btnMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "rr", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
