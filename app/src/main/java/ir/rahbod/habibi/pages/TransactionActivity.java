package ir.rahbod.habibi.pages;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.rahbod.habibi.R;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        bind();
        sendRequest();
        btnBack.setOnClickListener(this);
    }

    private void bind() {
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("وضعیت تراکنش");
        btnBack = findViewById(R.id.btnBack);
    }

    private void sendRequest() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }
}
